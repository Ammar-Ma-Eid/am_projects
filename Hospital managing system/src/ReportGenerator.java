import java.io.*;
import java.util.*;

class ReportGenerator {
    // Attributes
    private String reportType;
    private List<String> data; // A list to hold data for reports (e.g., patient info, appointment data)

    // Constructor
    public ReportGenerator(String reportType) {
        this.reportType = reportType;
        this.data = new ArrayList<>();
    }

    // Method to read data from Patient file
    private void loadPatientData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Final/newFinal_Project - Copy/src/Patient"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line); // Each line contains data for a patient (e.g., patientID,name,visit history)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read data from Appointment file
    private void loadAppointmentData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Final/newFinal_Project - Copy/src/Appointment"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line); // Each line contains appointment data (e.g., patientID, date, revenue)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadBillingData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Final/newFinal_Project - Copy/src/billing.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line); // Each line contains appointment data (e.g., patientID, date, revenue)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Merge Sort implementation for sorting data
    private void mergeSort(List<String> list) {
        if (list.size() > 1) {
            int mid = list.size() / 2;
            List<String> left = new ArrayList<>(list.subList(0, mid));
            List<String> right = new ArrayList<>(list.subList(mid, list.size()));

            mergeSort(left);
            mergeSort(right);

            merge(list, left, right);
        }
    }

    private void merge(List<String> list, List<String> left, List<String> right) {
        int i = 0, j = 0, k = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                list.set(k++, left.get(i++));
            } else {
                list.set(k++, right.get(j++));
            }
        }

        while (i < left.size()) {
            list.set(k++, left.get(i++));
        }

        while (j < right.size()) {
            list.set(k++, right.get(j++));
        }
    }

    // Quick Sort implementation for sorting data
    private void quickSort(List<String> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    private int partition(List<String> list, int low, int high) {
        String pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }

        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    // Generate Patient Report
    public void generatePatientReport(String patientID) {
        loadPatientData();
        // Filter data for the specified patient
        List<String> patientData = new ArrayList<>();
        for (String line : data) {
            if (line.contains(patientID)) {
                patientData.add(line); // Store the patient's visit history
            }
        }

        // Sort the data (e.g., by visit date)
        mergeSort(patientData); // You can replace with quickSort if needed

        // Generate and display report
        System.out.println("Patient Report for " + patientID);
        for (String line : patientData) {
            System.out.println(line); // Print visit history
        }
    }

    // Generate Appointment Report
    public void generateAppointmentReport(String AppointmentID) {
        loadAppointmentData();
        // Filter data for the specified patient
        List<String> appointmentData = new ArrayList<>();
        for (String line : data) {
            if (line.startsWith(AppointmentID)) {
                appointmentData.add(line); // Store the appointment details
            }
        }

        // Sort the data (e.g., by appointment date)
        mergeSort(appointmentData);

        // Generate and display report
        System.out.println("Appointment Report for " + AppointmentID);
        for (String line : appointmentData) {
            System.out.println(line); // Print appointment details
        }
    }

    // Generate Revenue Report
    public void generateRevenueReport(String patientID) {
        loadBillingData();
        // Calculate total revenue
        double totalRevenue = 0.0;
        for (String line : data) {
            String[] parts = line.split(",");
            if (line.startsWith(patientID)) {
                for (int i=1;i<parts.length;i++)
                totalRevenue += Double.parseDouble(parts[i]); // Assuming revenue is in the 3rd column
            }

        }

        // Display the revenue summary
        System.out.println("Total Revenue: $" + totalRevenue);
    }

}
