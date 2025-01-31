//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CancelAppointmentGUI extends JFrame implements ActionListener {
    private JTextField appointmentIDField;
    private JButton cancelButton;
    private JButton cancelBtn;
    private Patient patient;
    private PatientService patientService;
    private Appointment appointment;

    public CancelAppointmentGUI(Patient patient, PatientService patientService) {
        this.patient = patient;
        this.patientService = patientService;
        ImageIcon image = new ImageIcon("src/ll.jpeg");
        this.setIconImage(image.getImage());
        this.setTitle("Cancel Appointment");
        this.setSize(400, 200);
        this.setLayout(new GridLayout(3, 2, 10, 10));
        this.setDefaultCloseOperation(2);
        this.add(new JLabel("Enter Appointment ID to cancel:"));
        this.appointmentIDField = new JTextField();
        this.add(this.appointmentIDField);
        this.cancelButton = new JButton("Cancel Appointment");
        this.cancelButton.addActionListener(this);
        this.add(this.cancelButton);
        this.cancelBtn = new JButton("Cancel");
        this.cancelBtn.addActionListener(this);
        this.add(this.cancelBtn);
        this.setLocationRelativeTo((Component)null);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.cancelButton) {
            try {
                int appointmentID = Integer.parseInt(this.appointmentIDField.getText());
                if (Appointment.cancelAppointment(this.patient, appointmentID)) {
                    JOptionPane.showMessageDialog(this, "Appointment canceled successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to cancel appointment or appointment not found.");
                }
            } catch (NumberFormatException var3) {
                JOptionPane.showMessageDialog(this, "Invalid Appointment ID entered. Please try again.");
            }
        } else if (e.getSource() == this.cancelBtn) {
            this.dispose();
        }

    }
}
