package com.myapp.editor.view;

import com.myapp.editor.model.dao.User;
import com.myapp.editor.model.dao.UserDAO;

import javax.swing.*;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginView(Runnable onLoginSuccess) {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 160, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 60, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 160, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 100, 80, 25);
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(180, 100, 90, 25);
        add(registerButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = new User(username, password);
            UserDAO dao = new UserDAO();

            if (dao.loginUser(user)) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                dispose(); // close login window
                onLoginSuccess.run(); // call the editor
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
            }
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterView(onLoginSuccess); // go to RegisterView, reuse same onLoginSuccess
        });

        setVisible(true);
    }
}
