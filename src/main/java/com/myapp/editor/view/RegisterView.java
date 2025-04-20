package com.myapp.editor.view;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.myapp.editor.model.dao.User;
import com.myapp.editor.model.dao.UserDAO;


public class RegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterView(Runnable onRegisterSuccess) {
        setTitle("Register");
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

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(100, 100, 100, 25);
        add(registerButton);

        registerButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            User newUser = new User(user, pass);
            UserDAO dao = new UserDAO();
            boolean success = dao.registerUser(newUser);
            if (success) {
                JOptionPane.showMessageDialog(null, "Registration Successful");
                dispose();
                new LoginView(onRegisterSuccess); // go to login screen
            } else {
                JOptionPane.showMessageDialog(null, "Registration Failed");
            }
        });

        setVisible(true);
    }
}
