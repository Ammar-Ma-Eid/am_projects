import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Appointment {
    WaitingList waitingList;


    private  static Patient patient;
    private String date;
    private String time;
    static double amount=100;
    private String status;
    private String department;

    private int injuryLevel;
    public Appointment(int appointmentID, Patient patient, String date, String time, String status , int injuryLevel) {
        Appointment.patient = patient;
        this.date = date;
        this.time = time;
        this.status = status;
        this.injuryLevel=injuryLevel;
    }
    public Appointment(){

        this.status = "Scheduled";
    }
    public int getAppointmentID() {
        String appointmentFilePath = "";
        int newAppointmentID = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(appointmentFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > 0) {
                    try {
                        int currentID = Integer.parseInt(parts[0].trim());
                        newAppointmentID = Math.max(newAppointmentID, currentID+1);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid appointment ID format: " + parts[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the appointment file: " + e.getMessage());
        }

        return newAppointmentID;
    }

    public static Patient getPatient() {
        return patient;
    }

    public static void setPatient(Patient patient) {
        Appointment.patient = patient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }






    public boolean schedule(Patient patient, Appointment appointment) {
        String patientFilePath = "Final/newFinal_Project - Copy/src/Patient"; // Replace with actual path
        String appointmentFilePath = "Final/newFinal_Project - Copy/src/Appointment"; // Replace with actual path
        String waitingListFilePath = "Final/newFinal_Project - Copy/src/WaitingList"; // Waiting List file path

        boolean appointmentScheduled = false;
        boolean patientFound = false;
        boolean appointmentAvailable = false;
        boolean anotherPatientBooked = false;
        String appointmentID = "";
        List<String> bookedPatients = new ArrayList<>();
        int appointmentCount = 0; // To count existing appointments for the day

        try {
            // Step 1: Check appointment availability and collect booking information
            try (BufferedReader appointmentReader = new BufferedReader(new FileReader(appointmentFilePath))) {
                String line;
                while ((line = appointmentReader.readLine()) != null) {
                    String[] parts = line.split(",", -1);

                    if (parts.length >= 4 &&
                            parts[1].equals(appointment.getDepartment()) &&
                            parts[2].equals(appointment.getDate()) &&
                            parts[3].contains(appointment.getTime())) {

                        appointmentAvailable = true;
                        appointmentID = parts[0];

                        if (parts.length > 4) {
                            bookedPatients = Arrays.asList(parts[4].split(";"));
                        }
                    }

                    // Count appointments for the specific department and date
                    if (parts.length >= 4 && parts[1].equals(appointment.getDepartment()) && parts[2].equals(appointment.getDate())) {
                        appointmentCount++;
                    }
                }
            }

            if (!appointmentAvailable) {
                System.out.println("Selected date and time are not available in the appointment schedule.");
                return false;
            }

            // Step 2: Check if the patient has already booked this appointment
            if (bookedPatients.contains(patient.getEmail())) {
                System.out.println("Patient has already booked this appointment.");
                return false;
            }

            // Step 3: Check if another patient has booked the same appointment
            try (BufferedReader reader = new BufferedReader(new FileReader(patientFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",", -1);

                    // Skip the current patient's record
                    if (parts[0].equalsIgnoreCase(patient.getEmail()) && parts[1].equals(patient.getPassword())) {
                        continue;
                    }

                    // Check if the other patient has booked the same appointment
                    if (parts.length > 8 && parts[8] != null) {
                        String[] existingAppointments = parts[8].split(",");
                        for (String appointmentDetails : existingAppointments) {
                            String[] appointmentData = appointmentDetails.split(";");
                            if (appointmentData[0].equals(appointmentID)) {
                                anotherPatientBooked = true;
                                break;
                            }
                        }
                    }
                }
            }

            // Step 4: Check appointment limit
            if (appointmentCount >= 10) {


                // Add to waiting list
                WaitingList waitingList = new WaitingList(waitingListFilePath);
                waitingList.addToWaitingList(appointmentID, appointment.getDepartment(), appointment.getDate(), appointment.getTime(), patient, injuryLevel);
                System.out.println("You have been added to the waiting list ");
                return false; // Return false as the appointment was not scheduled
            }

            // Step 5: Save appointment details in patient's file
            StringBuilder patientSb = new StringBuilder();
            try (BufferedReader patientReader = new BufferedReader(new FileReader(patientFilePath))) {
                String line;
                while ((line = patientReader.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    if (parts[0].equalsIgnoreCase(patient.getEmail()) && parts[1].equals(patient.getPassword())) {
                        patientFound = true;

                        // Ensure the patient doesn't already have the same appointment in their record
                        if (parts.length > 8 && parts[8] != null) {
                            String[] existingAppointments = parts[8].split(",");
                            for (String existingAppointment : existingAppointments) {
                                String[] appointmentDetails = existingAppointment.split(";");
                                if (appointmentDetails[0].equals(appointmentID)) {
                                    System.out.println("Patient already has this appointment in their record.");
                                    return false;
                                }
                            }
                        }

                        // Add the new appointment to the patient's record
                        String newAppointmentDetails = appointmentID + ";" +
                                appointment.getDepartment() + ";" +
                                appointment.getDate() + ";" +
                                appointment.getTime() + ";" +
                                appointment.getStatus();

                        String appointments = parts.length > 8 ? parts[8] : "";
                        appointments = appointments.isEmpty() ? newAppointmentDetails : appointments + "," + newAppointmentDetails;

                        if (parts.length < 9) {
                            parts = Arrays.copyOf(parts, 9); // Ensure space for appointments field
                        }
                        parts[8] = appointments;
                    }

                    patientSb.append(String.join(",", parts)).append("\n");
                }
            }

            if (patientFound) {
                try (FileWriter patientWriter = new FileWriter(patientFilePath)) {
                    patientWriter.write(patientSb.toString());
                }
                appointmentScheduled = true;

                // If another patient has booked, add to the waiting list
                if (anotherPatientBooked) {
                    WaitingList waitingList = new WaitingList(waitingListFilePath);
                    waitingList.addToWaitingList(appointmentID, appointment.getDepartment(), appointment.getDate(), appointment.getTime(), patient ,injuryLevel);
                    System.out.println("Another patient has booked this appointment. Added to the waiting list.");
                } else {
                    System.out.println("Appointment successfully scheduled!");
                    System.out.println("Appointment ID: " + appointmentID);
                }
            } else {
                System.out.println("No matching patient found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading/writing files: " + e.getMessage());
        }

        return appointmentScheduled;
    }


    public static boolean cancelAppointment(Patient patient, int appointmentID) {
        String patientFilePath = "Final/newFinal_Project - Copy/src/Patient"; // Path to the patient file
        boolean appointmentFound = false;
        boolean patientFound = false;
        String line;

        try {

            // Step 2: Update the Patient file (remove the appointment from the patient's list)
            // Step 2: Update the Patient file (remove the appointment from the patient's list)
            BufferedReader patientReader = new BufferedReader(new FileReader(patientFilePath));
            StringBuilder updatedPatientRecords = new StringBuilder();

            while ((line = patientReader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if the record matches the patient's email and password
                if ((parts[0] + parts[1]).equals(patient.getEmail() + patient.getPassword())) {
                    patientFound = true;

                    // Extract appointment data (assuming stored as comma-separated after index 8)
                    List<String> updatedAppointments = new ArrayList<>();
                    for (int i = 9; i < parts.length; i++) {
                        if (parts[i] != null && !parts[i].isEmpty()) {
                            String[] patientAppointments = parts[i].split(",");

                            for (String bookedAppointment : patientAppointments) {
                                String[] appointmentDetails = bookedAppointment.split(";");

                                // Skip the entire appointment entry if the ID matches
                                if (!appointmentDetails[0].equals(String.valueOf(appointmentID))) {
                                    updatedAppointments.add(bookedAppointment); // Add all valid appointments
                                }
                            }
                        }
                    }

                    // Rebuild the patient record with updated appointments
                    String updatedRecord = String.join(",", Arrays.copyOfRange(parts, 0, 9)) +
                            (updatedAppointments.isEmpty() ? "" : "," + String.join(",", updatedAppointments));
                    updatedPatientRecords.append(updatedRecord).append("\n");
                } else {
                    updatedPatientRecords.append(line).append("\n");
                }
            }

            patientReader.close();

            if (!patientFound) {
                System.out.println("No matching patient found.");
                return false;
            }

// Write the updated patient records back to the file
            FileWriter patientWriter = new FileWriter(patientFilePath);
            patientWriter.write(updatedPatientRecords.toString());
            patientWriter.close();

            System.out.println("Patient record updated successfully!");

            return true;

        } catch (IOException e) {
            System.out.println("Error reading/writing files: " + e.getMessage());
            return false;
        }
    }




    public boolean reschedule(Patient patient, String appointmentID, Appointment appointment) {
        String filePath = "Final/newFinal_Project - Copy/src/Patient"; // Path to the patient file
        List<String> updatedLines = new ArrayList<>();
        boolean isRescheduled = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                // Split the line into parts (General patient fields + Appointments)
                String[] data = line.split(",", 9); // Split the patient record, assuming appointments start at index 8
                if (data.length < 9) {
                    // No appointment data, add line as-is
                    updatedLines.add(line);
                    continue;
                }

                // Check if the line corresponds to the given patient
                if ((data[0] + data[1]).equals(patient.getEmail() + patient.getPassword())) {
                    // Split the appointments into individual records
                    String[] appointments = data[8].split(",");
                    List<String> updatedAppointments = new ArrayList<>();

                    for (String singleAppointment : appointments) {
                        // Split each appointment item into fields for ID, department, date, time, status
                        String[] appointmentDetails = singleAppointment.split(";");
                        if (appointmentDetails.length >= 5 && appointmentDetails[0].equals(appointmentID)) {
                            // Match found, update to the new date, time, and status
                            appointmentDetails[2] = appointment.getDate();   // Update date
                            appointmentDetails[3] = appointment.getTime();   // Update time
                            appointmentDetails[4] = "Rescheduled";           // Update status
                            isRescheduled = true;
                        }
                        // Reassemble the updated or existing appointment and add to the list
                        updatedAppointments.add(String.join(";", appointmentDetails));
                    }

                    // Update the patient's record with the modified appointments
                    data[8] = String.join(",", updatedAppointments);
                    updatedLines.add(String.join(",", data));
                } else {
                    // Add other lines as-is
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the patient file: " + e.getMessage());
            return false;
        }

        // Write the updated file contents back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the patient file: " + e.getMessage());
            return false;
        }

        return isRescheduled;
    }






}
