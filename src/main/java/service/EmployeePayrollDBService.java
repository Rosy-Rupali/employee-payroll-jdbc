package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import exception.DatabaseConnectionException;
import model.EmployeePayrollData;

public class EmployeePayrollDBService {
	private static EmployeePayrollDBService employeePayrollDBService;
	private PreparedStatement preparedStatement;

	public EmployeePayrollDBService() {
	}

	public static EmployeePayrollDBService getInstance() {
		if (employeePayrollDBService == null) {
			employeePayrollDBService = new EmployeePayrollDBService();
		}
		return employeePayrollDBService;
	}

	public Connection getConnection() throws DatabaseConnectionException {
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

	public int updateEmployeeDataUsingStatement(String name, double salary) throws DatabaseConnectionException {
		String sql = "(update employee_payroll set salary=? where name='?');";
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowsAffected = statement.executeUpdate(sql);
			return rowsAffected;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable To update data in database");
		}
	}

	public int updateEmployeePayrollDataUsingPreparedStatement(String name, double salary) throws DatabaseConnectionException {
		if (this.preparedStatement == null) {
			this.prepareStatementForEmployeePayroll();
		}
		try {
			preparedStatement.setDouble(1, salary);
			preparedStatement.setString(2, name);
			int rowsAffected = preparedStatement.executeUpdate();
			return rowsAffected;
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to use prepared statement");
		}
	}

	private void prepareStatementForEmployeePayroll() throws DatabaseConnectionException {
		try {
			Connection connection = this.getConnection();
			String query = "(update employee_payroll set salary=? where name='?');";
			this.preparedStatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			throw new DatabaseConnectionException("Unable to prepare statement");
		}
	}

	public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws DatabaseConnectionException {
		String query = "(select * from employee_payroll where name=?);";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
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
