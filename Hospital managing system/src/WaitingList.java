import java.io.*;
import java.util.List;

public class WaitingList {
    private PriorityQueue queue;
    private String waitingListFilePath;

    public WaitingList(String waitingListFilePath) {
        this.waitingListFilePath = waitingListFilePath;
        this.queue = new PriorityQueue();
        loadWaitingList();
    }

    public void addToWaitingList(String appointmentID, String department, String date, String time, Patient patient, int injuryLevel) {
        boolean recordExists = false;
        boolean injuryLevelUpdated = false;

        List<PatientPriority> patients = queue.getAllSorted();
        for (PatientPriority existingPatient : patients) {
            if (existingPatient.getEmail().equals(patient.getEmail())) {
                if (existingPatient.getInjuryLevel() == injuryLevel) {
                    recordExists = true;
                    break;
                } else {
                    // Update injury level in the queue
                    queue.insert(new PatientPriority(patient.getEmail(), patient.getName(), injuryLevel));
                    queue.remove(existingPatient);
                    injuryLevelUpdated = true;
                    break;
                }
            }
        }

        if (recordExists) {
            System.out.println("There is a record with the same data.");
        } else if (injuryLevelUpdated) {
            System.out.println("Injury level updated for patient: " + patient.getName());
        } else {
            // Add new patient to the queue
            queue.insert(new PatientPriority(patient.getEmail(), patient.getName(), injuryLevel));
            System.out.println("Added new patient to the waiting list: " + patient.getName());
        }

        writeQueueToFile(appointmentID, department, date, time); // Pass appointment details
    }

    public PatientPriority pop() {
        PatientPriority nextPatient = queue.extractMin();
        if (nextPatient != null) {
            writeQueueToFile("", "", "", ""); // Clear the patient from the file
        } else {
            System.out.println("No patients in the waiting list.");
        }
        return nextPatient;
    }

    private void loadWaitingList() {
        try (BufferedReader waitingListReader = new BufferedReader(new FileReader(waitingListFilePath))) {
            String line;
            while ((line = waitingListReader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    String appointmentID = parts[0];
                    String email = parts[2];
                    String name = parts[1];
                    int injuryLevel = Integer.parseInt(parts[4]);
                    queue.insert(new PatientPriority(email, name, injuryLevel));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading waiting list: " + e.getMessage());
        }
    }

    private void writeQueueToFile(String appointmentID, String department, String date, String time) {
        List<PatientPriority> sortedPatients = queue.getAllSorted();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(waitingListFilePath))) {
            for (PatientPriority patient : sortedPatients) {
                writer.write(appointmentID + "," + department + "," + date + "," + time + "," + patient.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to waiting list file: " + e.getMessage());
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
