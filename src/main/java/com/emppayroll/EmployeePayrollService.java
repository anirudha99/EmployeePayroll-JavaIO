package com.emppayroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	public List<EmployeePayrollData> employeePayrollList;

	public EmployeePayrollService() {
		System.out.println("Welcome to Employee Payroll Service.");
	}

	public EmployeePayrollService(List<EmployeePayrollData> employeePayrollList) {
		this.employeePayrollList = employeePayrollList;
	}

	/**
	 * method to read employee detail from the user
	 * @param fileIo
	 */
	public void writeEmployeePayrollData(EmployeePayrollService.IOService fileIo) {
		if (fileIo.equals(IOService.CONSOLE_IO)) {
			System.out.println("\n Writing Employee Payroll Roaster to Console\n" + employeePayrollList);
		} else if (fileIo.equals(IOService.FILE_IO)) {
			new EmployeePayrollFileIOService().writeData(employeePayrollList);
		}
	}
	/**
	 * @param ioService
	 * @return count
	 */
	public long countEntries(IOService ioService)
	{  
		return new EmployeePayrollFileIOService().countEntries();
	}

	/**
	 * method to read employee detail from the console
	 * @param consoleInputReader
	 */
	private void readEmployeePayrollData(Scanner consoleInputReader) {
		System.out.println("Enter employee ID: ");
		int id = consoleInputReader.nextInt();
		System.out.println("Enter employee name: ");
		String name = consoleInputReader.next();
		System.out.println("Enter employee salary: ");
		double salary = consoleInputReader.nextDouble();
		employeePayrollList.add(new EmployeePayrollData(id, name, salary));
	}

	/*
	 * method to print employee detail to the console
	 */
	private void writeEmployeePayrollData(Scanner consoleInputReader) {
		System.out.println("\nWriting Employee Payroll to Console \n" + employeePayrollList);
	}

	public static void main(String[] args) {
		ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		Scanner consoleInputReader = new Scanner(System.in);
		employeePayrollService.readEmployeePayrollData(consoleInputReader);
		employeePayrollService.writeEmployeePayrollData(consoleInputReader);
		consoleInputReader.close();
	}
}
