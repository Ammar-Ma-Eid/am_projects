import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowPatient extends JFrame implements ActionListener {
    private JButton bookAppointmentButton, cancelAppointmentButton, generateReportButton, updateContactsButton, logoutButton;
    Patient patient;
    private Appointment appointment;
    private PatientService patientService;
    private JPanel panel;

    public ShowPatient(Patient patient, PatientService patientService) {
        this.patient = patient;
        this.patientService = patientService;

        setTitle("Patient Dashboard");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImageIcon image = new ImageIcon("src/ll.jpeg");
        setIconImage(image.getImage());
        bookAppointmentButton = new JButton("Book Appointment");
        cancelAppointmentButton = new JButton("Cancel Appointment");
        generateReportButton = new JButton("Generate Report");
        updateContactsButton = new JButton("Update Contacts");
        logoutButton = new JButton("Logout");
        bookAppointmentButton.addActionListener(this);
        cancelAppointmentButton.addActionListener(this);
        generateReportButton.addActionListener(this);
        updateContactsButton.addActionListener(this);
        logoutButton.addActionListener(this);
        setButtonStyle(bookAppointmentButton);
        setButtonStyle(cancelAppointmentButton);
        setButtonStyle(generateReportButton);
        setButtonStyle(updateContactsButton);
        setButtonStyle(logoutButton);
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.add(bookAppointmentButton);
        panel.add(cancelAppointmentButton);
        panel.add(generateReportButton);
        panel.add(updateContactsButton);
        panel.add(logoutButton);
        add(panel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setButtonStyle(JButton button) {
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 14));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookAppointmentButton) {
            new BookAppointmentGUI(patient, patientService);
        } else if (e.getSource() == cancelAppointmentButton) {
            new CancelAppointmentGUI(patient, patientService);
        }
        else if(e.getSource()==logoutButton){
            dispose();
            System.exit(0);
        }
    }
}
