package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollDBService {
	/**
	 * created getConnection() method to make connection with mysql database
	 * @return connection
	 * @throws DatabaseConnectionException
	 */
	private Connection getConnection() throws DatabaseConnectionException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String user = "root";
		String password = "Database123@";
		Connection connection;
		System.out.println("Connecting to database: " + jdbcURL);
		try {
			connection = DriverManager.getConnection(jdbcURL, user, password);
			System.out.println("Connection is SuccessFull!!! " + connection);
			return connection;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to establish the connection");
		}
	}

	/**
	 * created a readData() to read data from database table
     * added try and catch block to throw sql exception
	 * @return employeepayrollList
	 * @throws DatabaseConnectionException
	 */
	public List<EmployeePayrollData> readData() throws DatabaseConnectionException {
		String sql = "select * from employee_payroll; ";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to get data.Please check table");
		}

		return employeePayrollList;
	}

	/**
	 * @param name
	 * @param salary
	 * @return
	 * @throws DatabaseConnectionException
	 */
	public int updateEmployeeDataUsingStatement(String name, double salary) throws DatabaseConnectionException {
		String sql = String.format("update employee_payroll set salary=%.2f where name='%s'", salary, name);
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(sql);
			return rowsAffected;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable To update data in database");
		}
	}

	/**
	 * created getEmployeePayrollData method to get data from database
     * added try and catch block to throw sql exception
	 * @param name : first argument of the method
	 * @return employeepayrollList
	 * @throws DatabaseConnectionException
	 */
	public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws DatabaseConnectionException {
		String sql = String.format("select * from employee_payroll where name='%s'", name);
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String objectname = resultSet.getString("name");
				double salary = resultSet.getDouble("salary");
				LocalDate start = resultSet.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, objectname, salary, start));
			}
			return employeePayrollList;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to get data from database");
		}
	}
}
