package com.myapp.editor.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Method to register a new user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Check if the connection was established
            if (connection == null) {
                System.err.println("Failed to establish database connection.");
                return false;
            }

            // Set the username and password in the prepared statement
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());  // Password should be hashed before storing

            // Execute the query and check the result
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if the registration was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Return false in case of any error
        }
    }

    // Method to check if a username already exists in the database
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Check if the connection was established
            if (connection == null) {
                System.err.println("Failed to establish database connection.");
                return false;
            }

            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;  // If count > 0, the username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Return false if no result or exception occurs
    }

    // Method to authenticate a user during login
    public boolean loginUser(User user) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Check if the connection was established
            if (connection == null) {
                System.err.println("Failed to establish database connection.");
                return false;
            }

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());  // In practice, this should be hashed and compared

            ResultSet resultSet = stmt.executeQuery();

            // If a result is found, the login is successful
            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;  // Return false if no matching user was found
    }
}
