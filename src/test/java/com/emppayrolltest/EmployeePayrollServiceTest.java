package com.emppayrolltest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import com.emppayroll.EmployeePayrollData;
import com.emppayroll.EmployeePayrollService;

public class EmployeePayrollServiceTest {

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(1, " Jeff", 100000.0),
				new EmployeePayrollData(2, "Bill", 200000.0), new EmployeePayrollData(3, "Mark", 300000.0) };
		EmployeePayrollService employeePayrollService;
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		assertEquals(3, entries);
	}
}
