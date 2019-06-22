package gui.frames;

import controller.LoginListener;
import gui.frames.gameFrame.gameViewPieces.PreserveAspectRatioLayout;
import com.google.gson.Gson;
import gui.frames.gameFrame.gameViewPieces.PreserveAspectRatioLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginFrame extends JFrame {
    private ArrayList<LoginListener> loginListeners = new ArrayList<>();
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextArea errorResponseArea;
    private JButton loginButton;
    private JButton createUserButton;

    public LoginFrame() {
        // set up frame
        super("Team 4\'s Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 150);

        // initialize elements
        userNameLabel = new JLabel("UserName");
        passwordLabel = new JLabel("Password");
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        errorResponseArea = new JTextArea(2, 15);
        errorResponseArea.setEditable(false);
        errorResponseArea.setLineWrap(true);
        errorResponseArea.setWrapStyleWord(true);
        // create a Border around errorResponseArea
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        errorResponseArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        loginButton = new JButton("Log In");
        loginButton.addActionListener(new LoginButtonListener());
        createUserButton = new JButton("Create New");
        createUserButton.addActionListener(new CreateUserButtonListener());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        JPanel inputPanel = new JPanel(); // holds the userName and password inputs
        inputPanel.setLayout(new BorderLayout());
        // put the username field and label at the top of the panel
        JPanel userNamePanel = new JPanel();
        userNamePanel.setLayout(new BorderLayout());
        userNamePanel.add(userNameLabel, BorderLayout.PAGE_START);
        userNamePanel.add(usernameField, BorderLayout.PAGE_END);
        inputPanel.add(userNamePanel, BorderLayout.PAGE_START);
        // put the password field and label in the center of the panel
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BorderLayout());
        passwordPanel.add(passwordLabel, BorderLayout.PAGE_START);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        inputPanel.add(passwordPanel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        // put the login and create user buttons at the bottom of the panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(loginButton, BorderLayout.LINE_START);
        buttonsPanel.add(createUserButton, BorderLayout.LINE_END);
        mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);

        mainPanel.add(errorResponseArea, BorderLayout.LINE_END);
        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void addLoginListener(LoginListener l) {
        loginListeners.add(l);
    }

    public void publishLogin() {
        for(LoginListener l : loginListeners) {
            l.loginChanged(usernameField.getText(), passwordField.getText());
        }
    }

    public void publishNewUser() {
        for(LoginListener l : loginListeners) {
            l.newUserChanged(usernameField.getText(), passwordField.getText());
        }
    }

    public void incorrectUsernameMessage() {
        EventQueue.invokeLater(
                () -> {
                    errorResponseArea.setText("Login: UserName incorrect.");
                    usernameField.setText("");
                    passwordField.setText("");
                }
        );
    }

    public void incorrectPasswordMessage() {
        EventQueue.invokeLater(
                () -> {
                    errorResponseArea.setText("Login: Password incorrect.");
                    passwordField.setText("");
                }
        );
    }

    public void incorrectNewUsernameMessage() {
        EventQueue.invokeLater(
                () -> {
                    errorResponseArea.setText("Create User: User with that name" +
                            " already exists.");
                    usernameField.setText("");
                    passwordField.setText("");
                }
        );
    }

    private class LoginButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            publishLogin();
        }
    }

    private class CreateUserButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent l) {
            // make sure the username does not have underscores, which cause some code not to work
            String uName = usernameField.getText();
            if(uName.contains("_")){
                EventQueue.invokeLater(
                        () -> {
                            errorResponseArea.setText("Create User: username cannot contain " +
                                    "underscores.");
                            usernameField.setText("");
                            passwordField.setText("");
                        }
                );
            }
            else {
                publishNewUser();
            }
        }
    }
}
