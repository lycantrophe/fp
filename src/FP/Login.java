/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
class Login extends JFrame implements ActionListener {

    private JButton SUBMIT;
    private JPanel panel;
    private JLabel labelUsername, labelPassword;
    private JTextField textUser, textPass;
    Query query;
    Connection connection;

    Login(Connection connection) {
        this.connection = connection;
        query = new Query();
        labelUsername = new JLabel();
        labelUsername.setText("Username:");
        textUser = new JTextField(15);

        labelPassword = new JLabel();
        labelPassword.setText("Password:");
        textPass = new JPasswordField(15);

        SUBMIT = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(3, 1));
        panel.add(labelUsername);
        panel.add(textUser);
        panel.add(labelPassword);
        panel.add(textPass);
        panel.add(SUBMIT);
        add(panel, BorderLayout.CENTER);
        SUBMIT.addActionListener(this);
        setTitle("LOGIN FORM");
    }

    public void actionPerformed(ActionEvent ae) {
        String uname = textUser.getText();
        String pass = textPass.getText();

        String success = "";
        try {
            connection.send(uname);
            connection.send(pass);

            success = connection.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (success.equals("Login successful")) {
            try {
                // Starts the actual program and destroyes the login window
                Client.loginSuccessful();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("enter the valid username and password");
            JOptionPane.showMessageDialog(this, "Incorrect login or password",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
