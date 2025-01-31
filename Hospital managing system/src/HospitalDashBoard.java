//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class HospitalDashBoard extends JFrame implements ActionListener {
    private PatientService patientService = new PatientService();
    static Patient patient;
    private JButton Loginbutton;
    private JButton signupButton;

    public HospitalDashBoard() {
        this.setTitle("AIU Hospital System");
        this.setDefaultCloseOperation(3);
        this.setSize(450, 650);
        this.setLocationRelativeTo((Component)null);
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon("src/pp.png");
                Image backgroundImage = backgroundIcon.getImage();
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        ImageIcon image = new ImageIcon("src/ll.jpeg");
        this.setIconImage(image.getImage());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        mainPanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to AIU Hospital.", 0);
        welcomeLabel.setFont(new Font("Arial", 1, 18));
        welcomeLabel.setForeground(Color.BLACK);
        mainPanel.add(welcomeLabel, "North");
        JPanel buttonPanel = new JPanel(new FlowLayout(1, 20, 10));
        buttonPanel.setOpaque(false);
        this.Loginbutton = new JButton("LOGIN");
        this.styleButton(this.Loginbutton);
        this.signupButton = new JButton("SIGNUP");
        this.styleButton(this.signupButton);
        buttonPanel.add(this.Loginbutton);
        buttonPanel.add(this.signupButton);
        mainPanel.add(buttonPanel, "Center");
        this.add(mainPanel);
        this.setVisible(true);
        this.Loginbutton.addActionListener(this);
        this.signupButton.addActionListener(this);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(100, 40));
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", 1, 14));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Loginbutton) {
            this.showLoginDialog();
        } else if (e.getSource() == this.signupButton) {
            this.showSignupDialog();
        }

    }

    private void showLoginDialog() {
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPanel loginPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        UIManager.put("Button.background", new Color(72, 201, 176));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", Color.GRAY);
        int result = JOptionPane.showConfirmDialog(this, loginPanel, "Login", 2, -1);
        if (result == 0) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (this.patientService.loginPatient(email, password)) {
                patient = this.patientService.getPatientByEmail(email);
                if (patient != null) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    System.out.println("Login successfully");
                    System.out.println("Hello " + patient.getEmail());
                    new ShowPatient(patient, this.patientService);
                } else {
                    JOptionPane.showMessageDialog(this, "Patient not found.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Email or Password. Please try again.");
            }
        }

    }

    private void showSignupDialog() {
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField historyField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField relativePhoneField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPanel signupPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        signupPanel.add(new JLabel("Full Name:"));
        signupPanel.add(nameField);
        signupPanel.add(new JLabel("Age:"));
        signupPanel.add(ageField);
        signupPanel.add(new JLabel("Do you have any MedicalHistory? :"));
        signupPanel.add(historyField);
        signupPanel.add(new JLabel("Address:"));
        signupPanel.add(addressField);
        signupPanel.add(new JLabel("PhoneNumber :"));
        signupPanel.add(phoneField);
        signupPanel.add(new JLabel("Relative PhoneNumber :"));
        signupPanel.add(relativePhoneField);
        signupPanel.add(new JLabel("Email:"));
        signupPanel.add(emailField);
        signupPanel.add(new JLabel("Password:"));
        signupPanel.add(passwordField);
        UIManager.put("Button.background", new Color(72, 201, 176));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focus", Color.GRAY);
        int result = JOptionPane.showConfirmDialog(this, signupPanel, "Signup", 2, -1);
        if (result == 0) {
            try {
                Patient newPatient = new Patient();
                newPatient.setName(nameField.getText());
                newPatient.setAge(Integer.parseInt(ageField.getText()));
                newPatient.setMedicalHistory(historyField.getText());
                newPatient.setAddress(addressField.getText());
                newPatient.setPhoneNumber(Integer.parseInt(phoneField.getText()));
                newPatient.setRelativeNumber(Integer.parseInt(relativePhoneField.getText()));
                newPatient.setEmail(emailField.getText());
                newPatient.setPassword(new String(passwordField.getPassword()));
                if (Main.registerPatient(newPatient.getEmail(), newPatient.getPassword(), newPatient.getName(), newPatient.getIDForReport(), newPatient.getAge(), newPatient.getMedicalHistory(), newPatient.getAddress(), newPatient.getPhoneNumber(), newPatient.getRelativeNumber())) {
                    JOptionPane.showMessageDialog(this, "Signup Successful!");
                } else {
                    JOptionPane.showMessageDialog(this, "Email already registered!");
                }
            } catch (NumberFormatException var12) {
                JOptionPane.showMessageDialog(this, "Invalid input format. Please try again.");
            }
        }

    }
}
