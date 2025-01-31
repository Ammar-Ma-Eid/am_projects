import java.io.*;
import java.util.*;

public class Patient implements Comparable<Patient>{

    static Scanner input = new Scanner(System.in);
    private int priority;


    private String Email;
    private String Password;
    private String name;
    private int age;
    private String medicalHistory;
    private List<String> visitRecords;




    private String address;
    private int phoneNumber;
    private int relativeNumber;

    public Patient(String email, String password, String name, int age, int i, String medicalHistory, String address, int phoneNumber, int relativeNumber) {
        Email = email;
        Password = password;
        this.name = name;
        this.age = age;
        this.medicalHistory = medicalHistory;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.relativeNumber = relativeNumber;
    }

    public Patient(){

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRelativeNumber() {
        return relativeNumber;
    }

    public void setRelativeNumber(int relativeNumber) {
        this.relativeNumber = relativeNumber;
    }


    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<String> getVisitRecords() {
        return visitRecords;
    }

    public void setVisitRecords(List<String> visitRecords) {
        this.visitRecords = visitRecords;
    }

    public int getIDForReport() {
        String PatientFilePath = "Final/newFinal_Project - Copy/src/Patient";
        int newID = 1;

        try (BufferedReader reader = new BufferedReader(new FileReader(PatientFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > 0) {
                    try {
                        int currentID = Integer.parseInt(parts[3].trim());
                        newID = Math.max(newID, currentID);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid appointment ID format: " + parts[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the appointment file: " + e.getMessage());
        }

        return newID;
    }
    public int getPatientID() {
        String patientFilePath = "Final/newFinal_Project - Copy/src/Patient"; // Path to the patient file
        Random random = new Random();
        HashSet<Integer> existingIDs = new HashSet<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(patientFilePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length > 3) { // Assuming patientID is at index 3
                    try {
                        int existingID = Integer.parseInt(parts[3]);
                        existingIDs.add(existingID);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid patient ID format in file: " + parts[3]);
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading the patient file: " + e.getMessage());
        }

        // Generate a random unique patient ID
        int newPatientID;
        do {
            newPatientID = 10000 + random.nextInt(90000); // Random ID between 10000 and 99999
        } while (existingIDs.contains(newPatientID));

        return newPatientID;
    }

    @Override
    public int compareTo(Patient o) {
        return 0;
    }








    public void updateContactInfo(String email, String password, String newAddress, int newPhoneNumber, int newRelativeNumber) {
        String filePath = "Final/newFinal_Project - Copy/src/Patient"; // Path to the Patient file
        StringBuilder updatedFileContent = new StringBuilder();
        boolean recordUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Locate the patient record using email and password
                if (parts.length > 8 && parts[0].equalsIgnoreCase(email) && parts[1].equals(password)) {
                    // Update address, phone, and relative number (indices 6, 7, 8)
                    parts[6] = newAddress;
                    parts[7] = String.valueOf(newPhoneNumber);
                    parts[8] = String.valueOf(newRelativeNumber);
                    recordUpdated = true;
                }
                // Append the (possibly updated) record to the StringBuilder
                updatedFileContent.append(String.join(",", parts)).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading patient file: " + e.getMessage());
            return;
        }

        // Write the updated content back to the file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(updatedFileContent.toString());
            if (recordUpdated) {
                System.out.println("Contact information updated successfully!");
            } else {
                System.out.println("No matching record found for the given email and password.");
            }
        } catch (IOException e) {
            System.out.println("Error writing to patient file: " + e.getMessage());
        }
    }


    public void getPatientInfo() {
        String patientFilePath = "Final/newFinal_Project - Copy/src/Patient";  // Path to the patient file

        try (BufferedReader reader = new BufferedReader(new FileReader(patientFilePath))) {
            String line;
            // Read through the file line by line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Check if the patient's email and password match
                if (parts[0].equalsIgnoreCase(Email) && parts[1].equals(Password)) {
                    // Display patient information in the desired format
                    System.out.println("Reports of " + parts[3] + ":");
                    System.out.println("Email: " + parts[0]);
                    System.out.println("Password: " + parts[1]);
                    System.out.println("Name: " + parts[2]);
                    System.out.println("Patient ID: " + parts[3]);
                    System.out.println("Age: " + parts[4]);
                    System.out.println("Medical History: " + parts[5]);
                    System.out.println("Address: " + parts[6]);
                    System.out.println("Phone: " + parts[7]);
                    System.out.println("Relative phone: " + parts[8]);

                    // Check and display the patient's appointments if available
                    if (parts.length > 9 && !parts[9].isEmpty()) {
                        System.out.println("Appointments:");
                        for (int i = 9; i < parts.length; i++) {
                            if (!parts[i].isEmpty()) {
                                System.out.println("  - " + parts[i]); // Print each appointment
                            }
                        }
                    } else {
                        System.out.println("No additional appointments.");
                    }
                    return; // Exit after processing the patient
                }
            }

            // If no matching patient found
            System.out.println("Patient not found.");
        } catch (IOException e) {
            System.out.println("Error reading the patient file: " + e.getMessage());
        }
    }







}







