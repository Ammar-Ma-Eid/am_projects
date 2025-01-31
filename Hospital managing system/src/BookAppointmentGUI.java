import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class BookAppointmentGUI extends JFrame implements ActionListener {
    private JButton generalButton, cardiologyButton, pediatricsButton, cancelButton;
    private String selectedDepartment = null;
    private Patient patient;
    private PatientService patientService;

    public BookAppointmentGUI(Patient patient, PatientService patientService) {
        this.patient = patient;
        this.patientService = patientService;

        // Set up the frame
        ImageIcon image = new ImageIcon("src/ll.jpeg");
        this.setIconImage(image.getImage());
        this.setTitle("Book Appointment");
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        // Department selection label
        JLabel departmentLabel = new JLabel("Choose a Department:");
        departmentLabel.setAlignmentX(CENTER_ALIGNMENT);
        departmentLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        this.add(Box.createVerticalStrut(20));
        this.add(departmentLabel);

        // Department buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        generalButton = new JButton("General");
        cardiologyButton = new JButton("Cardiology");
        pediatricsButton = new JButton("Pediatrics");

        styleButton(generalButton);
        styleButton(cardiologyButton);
        styleButton(pediatricsButton);

        buttonPanel.add(generalButton);
        buttonPanel.add(cardiologyButton);
        buttonPanel.add(pediatricsButton);

        this.add(buttonPanel);

        // Cancel button
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton);
        cancelButton.setAlignmentX(CENTER_ALIGNMENT);
        this.add(cancelButton);
        this.add(Box.createVerticalStrut(20));

        // Add action listeners to buttons
        generalButton.addActionListener(this);
        cardiologyButton.addActionListener(this);
        pediatricsButton.addActionListener(this);
        cancelButton.addActionListener(this);

        // Display the GUI
        this.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setBackground(new Color(59, 89, 182));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generalButton) {
            selectedDepartment = "General";
            handleDepartmentSelection();
        } else if (e.getSource() == cardiologyButton) {
            selectedDepartment = "Cardiology";
            handleDepartmentSelection();
        } else if (e.getSource() == pediatricsButton) {
            selectedDepartment = "Pediatrics";
            handleDepartmentSelection();
        } else if (e.getSource() == cancelButton) {
            // Close the window
            this.dispose();
        }
    }

    private void handleDepartmentSelection() {
        if (selectedDepartment != null) {
            System.out.println("Selected Department: " + selectedDepartment);

            // Check availability using the `f` method
            Appointment appointment = new Appointment();
            appointment.setDepartment(selectedDepartment);

            // Call the `f` method to check for availability
            boolean isDepartmentAvailable = f(appointment.getDepartment(), appointment);

            if (!isDepartmentAvailable) {
                JOptionPane.showMessageDialog(this, "No available appointments for the selected department.");
                return;
            }

            // Select a preferred date
            String selectedDate = selectOption("Choose a Preferred Date", getAvailableDates(selectedDepartment));
            if (selectedDate == null) return;

            // Fetch available times
            String selectedTime = selectOption("Choose a Preferred Time", getAvailableTimes(selectedDepartment, selectedDate));
            if (selectedTime == null) return;

            // Set date and time for the appointment
            appointment.setDate(selectedDate);
            appointment.setTime(selectedTime);

            // Process billing and payment
            processBilling(appointment);
        }
    }

    private List<String> getAvailableDates(String department) {
        List<String> dates = new ArrayList<>();
        String path = "Final/newFinal_Project - Copy/src/Appointment.java";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 2 && data[1].equalsIgnoreCase(department)) {
                    String date = data[2];
                    if (!dates.contains(date)) {
                        dates.add(date);
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading appointment file: " + ex.getMessage());
        }

        return dates;
    }

    private List<String> getAvailableTimes(String department, String selectedDate) {
        List<String> times = new ArrayList<>();
        String path = "Final/newFinal_Project - Copy/src/Appointment.java";

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 3 && data[1].equalsIgnoreCase(department) && data[2].equals(selectedDate)) {
                    String[] timeSlots = data[3].split(";");
                    times.addAll(Arrays.asList(timeSlots));
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading appointment file: " + ex.getMessage());
        }

        return times;
    }

    private String selectOption(String message, List<String> options) {
        String[] optionArray = options.toArray(new String[0]);
        return (String) JOptionPane.showInputDialog(
                this,
                message,
                "Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                optionArray,
                optionArray[0]);
    }

    private void processBilling(Appointment appointment) {
        // Display payment confirmation
        int response = JOptionPane.showConfirmDialog(this,
                "The appointment amount is " + appointment.amount + ". Agree to pay?",
                "Payment Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            // Simulate billing and scheduling
            Billing billing = new Billing(String.valueOf(patient.getIDForReport()));
            billing.generateBill(appointment.amount);
            billing.addPayment(appointment.amount);

            if (appointment.schedule(patient, appointment)) {
                JOptionPane.showMessageDialog(this, "Appointment booked successfully! Appointment ID: " + appointment.getAppointmentID());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to book the appointment. Try a different date/time.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Payment declined. Booking cancelled.");
        }
    }













    public static boolean f(String department, Appointment appointment) {
        String path = "Final/newFinal_Project - Copy/src/Appointment"; // Update this to a proper relative path
        Map<String, List<String>> dateTimeMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 3 && data[1].equalsIgnoreCase(department)) {
                    String date = data[2];
                    String[] times = data[3].split(";");
                    dateTimeMap.putIfAbsent(date, new ArrayList<>());
                    dateTimeMap.get(date).addAll(Arrays.asList(times));
                }
            }

            if (dateTimeMap.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No appointments available for this department.", "No Availability", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // Create a list from the available dates
            List<String> availableDates = new ArrayList<>(dateTimeMap.keySet());

            // Use JOptionPane to let the user choose the date
            String selectedDate = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose Preferred Date:",
                    "Date Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    availableDates.toArray(),
                    availableDates.get(0)
            );

            if (selectedDate == null) {
                JOptionPane.showMessageDialog(null, "No date selected.", "Selection Cancelled", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // Get the available times for the selected date
            List<String> availableTimes = dateTimeMap.get(selectedDate);
            if (availableTimes == null || availableTimes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No available times on the selected date.", "No Times Available", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // Use JOptionPane to let the user choose the time
            String selectedTime = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose Preferred Time:",
                    "Time Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    availableTimes.toArray(),
                    availableTimes.get(0)
            );

            if (selectedTime == null) {
                JOptionPane.showMessageDialog(null, "No time selected.", "Selection Cancelled", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // Set the selected date and time in the Appointment object
            appointment.setDate(selectedDate);
            appointment.setTime(selectedTime);

            // Confirmation dialog
            JOptionPane.showMessageDialog(null, "You have selected:\nDate: " + selectedDate + "\nTime: " + selectedTime, "Appointment Confirmed", JOptionPane.INFORMATION_MESSAGE);

            return true;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Appointment file not found: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading the appointment file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}