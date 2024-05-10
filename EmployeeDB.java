package application;

import java.sql.*;

/**
 * Class responsible for database operations related to employees.
 */
public class EmployeeDB {
    // Database connection details
    private static final String URL = "jdbc:mariadb://localhost:3306/EmployeeDetails";
    private static final String USERNAME = "Ramitha";
    private static final String PASSWORD = "test123";

    private Connection connection;

    /**
     * Constructor to establish a database connection.
     */
    public EmployeeDB() {
        try {
            // Establish the database connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // Handle connection errors
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    /**
     * Method to retrieve the database connection.
     * @return The database connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Method to create the employee table in the database.
     */
    public void createEmployeeTable() {
        try {
            // Create a statement for executing SQL queries
            Statement statement = connection.createStatement();
            // Define SQL query for creating the employee table
            String createTableQuery = "CREATE TABLE IF NOT EXISTS employeesdetails ("
                    + "id INT PRIMARY KEY,"
                    + "name VARCHAR(30),"
                    + "age INT,"
                    + "email VARCHAR(30),"
                    + "department VARCHAR(50)"
                    + ")";
            // Execute the create table query
            statement.executeUpdate(createTableQuery);
            System.out.println("Employee table created successfully.");
        } catch (SQLException e) {
            // Handle table creation errors
            System.err.println("Error creating employee table: " + e.getMessage());
        }
    }

    /**
     * Method to register a new employee in the database.
     * @param id The employee ID
     * @param name The employee name
     * @param age The employee age
     * @param email The employee email
     * @param department The employee department
     */
    public void registerEmployee(int id, String name, int age, String email, String department) {
        try {
            // Define SQL query for inserting employee data
            String insertQuery = "INSERT INTO employeesdetails (id, name, age, email, department) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, age);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, department);
            // Execute the insert query
            preparedStatement.executeUpdate();
            System.out.println("Employee registered successfully.");
        } catch (SQLException e) {
            // Handle registration errors
            System.err.println("Error registering employee: " + e.getMessage());
        }
    }

    // Method to retrieve all employees' information as a string
    public String getEmployeesInfoAsString() throws SQLException {
        StringBuilder employeesInfo = new StringBuilder();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM employeesdetails");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");
                String department = resultSet.getString("department");

                employeesInfo.append("ID: ").append(id).append(", Name: ").append(name).append(", Age: ").append(age)
                        .append(", Email: ").append(email).append(", Department: ").append(department).append("\n");
            }
        }
        return employeesInfo.toString();
    }

    /**
     * Method to update employee information in the database.
     * @param id The employee ID
     * @param name The employee name
     * @param age The employee age
     * @param email The employee email
     * @param department The employee department
     */
    public void updateEmployee(int id, String name, int age, String email, String department) {
        try {
            // Define SQL query for updating employee data
            String updateQuery = "UPDATE employeesdetails SET name=?, age=?, email=?, department=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, department);
            preparedStatement.setInt(5, id);
            // Execute the update query
            preparedStatement.executeUpdate();
            System.out.println("Employee information updated successfully.");
        } catch (SQLException e) {
            // Handle update errors
            System.err.println("Error updating employee information: " + e.getMessage());
        }
    }

    /**
     * Method to delete the employee table from the database.
     */
    public void deleteEmployeeTable() {
        try {
            // Create a statement for executing SQL queries
            Statement statement = connection.createStatement();
            // Define SQL query for deleting the employee table
            statement.executeUpdate("DROP TABLE IF EXISTS employeesdetails");
            System.out.println("Employee table deleted successfully.");
        } catch (SQLException e) {
            // Handle table deletion errors
            System.err.println("Error deleting employee table: " + e.getMessage());
        }
    }
    
    // Method to close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // Handle connection closing errors
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }

    /**
     * Method to check if an employee with the given ID exists in the database.
     * @param id The employee ID
     * @return True if the employee exists, false otherwise
     * @throws SQLException If an SQL exception occurs
     */
    public boolean employeeExists(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM employeesdetails WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    }
}
