/**************************************************************
 * Purpose JDBC Demo to perform various operations in database. 
 * @author Rosy Rupali
 * @Version 1.0
 * @since 06-07-2021
 *
 ************************************************************/
package service;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import exception.DatabaseConnectionException;

public class DatabaseService {

	
	/**
	 * This method connects program to database.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws DatabaseConnectionException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = "Database123@";
		Connection connection ;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded");
		} catch (Exception e) {
			throw new DatabaseConnectionException("Cannot find the driver in the classpath:");
		}
		listDrivers();
		try {
			System.out.println("Connecting database!!!" + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			System.out.println("Connection is successful!!!" + connection);
		} catch (Exception e) {
			throw new DatabaseConnectionException(e.getMessage());
		}
	}

	/**
	 *This method shows the list of drivers it contains 
	 *in enumeration list
	 */
	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println(" " + driverClass.getClass().getName());
		}
	}

}
