package com.emppayroll;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;

public class Java8WatchService {

	private final WatchService watcher;
	private final Map<WatchKey, Path> dirWatchers;

	/**
	 * Creates a WatchService and registers all the directory
	 * @param dir contains the path
	 * @throws IOException
	 */
	public Java8WatchService(Path dir) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.dirWatchers = new HashMap<WatchKey, Path>();
		scanAndRegisterDirectories(dir);
	}

	/**
	 * Register the given directory with the watchService 
	 * @param dir contains path
	 * @throws IOException
	 */
	private void registerDirWatchers(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		dirWatchers.put(key, dir);
	}


	/**
	 * Register the given directory, and all its sub-directories
	 * @param start
	 * @throws IOException
	 */
	private void scanAndRegisterDirectories(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerDirWatchers(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 *  Process all events for keys queued to the watcher
	 */
	public void processEvents() {
		while (true) {
			WatchKey Key;// wait for key to be signalled
			try {
				Key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}
			Path dir = dirWatchers.get(Key);
			if (dir == null)
				continue;
			for (WatchEvent<?> event : Key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				Path name = ((WatchEvent<Path>) event).context();
				Path child = dir.resolve(name);
				System.out.format("%s: %s\n", event.kind().name(), child); // print out event

				// if directory is created ,then register it and its sub-directories
				if (kind == ENTRY_CREATE) {
					try {
						if (Files.isDirectory(child))
							scanAndRegisterDirectories(child);

					} catch (IOException x) {
					}
				} else if (kind.equals(ENTRY_DELETE)) {
					if (Files.isDirectory(child))
						dirWatchers.remove(Key);

				}
			}
			// reset key and remove from set if directory no longer accessible
			boolean valid = Key.reset();
			if (!valid) {
				dirWatchers.remove(Key);
				if (dirWatchers.isEmpty())
					break;
			}
		}
	}
}
