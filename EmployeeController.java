package application;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

/**
 * Controller class for managing employee-related functionalities.
 */
public class EmployeeController {

    // FXML fields injected from the FXML file
    @FXML
    private TextField employeeIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField departmentField;

    // Instance of the EmployeeDB class for database operations
    private EmployeeDB employeeDB;

    /**
     * Constructor to initialize the EmployeeDB object.
     */
    public EmployeeController() {
        employeeDB = new EmployeeDB();
    }

    /**
     * Method to create the employee table in the database.
     */
    @FXML
    private void createEmployeeTable() {
        employeeDB.createEmployeeTable();
    }

    /**
     * Method to register a new employee.
     */
    @FXML
    private void registerEmployee() {
        try {
            // Retrieve employee details from input fields
            int id = Integer.parseInt(employeeIdField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String email = emailField.getText();
            String department = departmentField.getText();

            // Check if the employee ID already exists
            if (employeeDB.employeeExists(id)) {
                showAlert(AlertType.ERROR, "Error", "Employee with ID " + id + " already exists.");
                return;
            }

            // Register the employee in the database
            employeeDB.registerEmployee(id, name, age, email, department);

            // Clear input fields after registration
            clearFields();

            // Show confirmation message
            showAlert(AlertType.INFORMATION, "Employee Registered", "Employee has been registered successfully.");
        } 
        catch (SQLException e) {
            // Handle SQL exceptions
            if (e.getSQLState().equals("42S02")) {
                showAlert(AlertType.ERROR, "Error", "Employee table not found. Please create the employee table first.");
            } else {
                showAlert(AlertType.ERROR, "Error", "Failed to register employee: " + e.getMessage());
            }
        }
        catch (NumberFormatException e) {
            // Handle number format exceptions
            showAlert(AlertType.ERROR, "Error", "Please enter a valid age.");
        } 
    }

    /**
     * Method to view all employees.
     */
    @FXML
    private void viewEmployees() {
        try {
            // Retrieve employees' information from the database
            String employeesInfo = employeeDB.getEmployeesInfoAsString();
            // Show employees in a custom window
            showEmployeesWindow("Employees List", employeesInfo);
        } catch (SQLException e) {
            // Handle SQL exceptions
            showAlert(AlertType.ERROR, "Error", "Failed to view employees: " + e.getMessage());
        }
    }

    /**
     * Method to update an employee's information.
     */
    @FXML
    private void updateEmployee() {
        try {
            // Retrieve employee details from input fields
            int id = Integer.parseInt(employeeIdField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String email = emailField.getText();
            String department = departmentField.getText();

            // Check if the employee exists before updating
            if (employeeDB.employeeExists(id)) {
                // Update the employee information in the database
                employeeDB.updateEmployee(id, name, age, email, department);

                // Show confirmation message
                showAlert(AlertType.INFORMATION, "Employee Updated", "Employee information updated successfully.");

                // Clear input fields after update
                clearFields();

                // Show the updated view
                viewEmployees();
            } else {
                showAlert(AlertType.ERROR, "Error", "Employee with ID " + id + " does not exist.");
            }
        } catch (NumberFormatException e) {
            // Handle number format exceptions
            showAlert(AlertType.ERROR, "Error", "Please enter a valid ID and age.");
        } catch (SQLException e) {
            // Handle SQL exceptions
            showAlert(AlertType.ERROR, "Error", "Failed to update employee: " + e.getMessage());
        }
    }
    
    /**
     * Method to delete the employee table from the database.
     */
    @FXML
    private void deleteEmployeeTable() {
        // Display confirmation dialog before deleting the table
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete the employee table? This action cannot be undone.");

        // Wait for user response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete the employee table
                employeeDB.deleteEmployeeTable();
                showAlert(AlertType.INFORMATION, "Success", "Employee table deleted successfully.");
            }
        });
    }

    /**
     * Method to clear input fields.
     */
    private void clearFields() {
        nameField.clear();
        ageField.clear();
        emailField.clear();
        departmentField.clear();
    }

    /**
     * Method to show an alert dialog.
     * @param alertType The type of alert
     * @param title The title of the alert
     * @param message The message of the alert
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method to show a window displaying employee information.
     * @param title The title of the window
     * @param content The content to be displayed in the window
     */
    private void showEmployeesWindow(String title, String content) {
        // Create a new stage
        Stage stage = new Stage();
        stage.setTitle(title);

        // Create a text area to display the employee information
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Split the content by newline to separate each employee
        String[] employees = content.split("\n");

        // Append each employee's information with attributes on separate lines
        StringBuilder formattedContent = new StringBuilder();
        for (String employee : employees) {
            formattedContent.append(employee).append("\n--------------------------------\n");
        }
        textArea.setText(formattedContent.toString());

        // Create a layout for the scene
        VBox layout = new VBox(10);
        layout.getChildren().addAll(textArea);
        layout.setAlignment(Pos.CENTER);

        // Create a scene and set it to the stage
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);

        // Show the stage
        stage.show();
    }
}
