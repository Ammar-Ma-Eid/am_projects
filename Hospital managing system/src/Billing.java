import java.io.*;
import java.util.*;

class Billing {
    // Attributes
    private String patientID;
    private double billingAmount;
    private List<Double> paymentHistory;

    // Constructor
    public Billing(String patientID) {
        this.patientID = patientID;
        this.paymentHistory = new ArrayList<>();
        loadData();
    }

    // Method to load the data from the file
    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Final/newFinal_Project - Copy/src/billing.txt"))) {
            String line;
            boolean patientFound = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(patientID)) {
                    billingAmount = Double.parseDouble(parts[1]);
                    patientFound = true;
                }
            }

            if (!patientFound) {
                billingAmount = 0.0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save data to the file
    private void saveToFile() {
        List<String> lines = new ArrayList<>();
        boolean patientFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("Final/newFinal_Project - Copy/src/billing.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(patientID)) {
                    lines.add(patientID + "," + billingAmount);
                    patientFound = true;
                } else {
                    lines.add(line);
                }
            }

            if (!patientFound) {
                lines.add(patientID + "," + billingAmount);
            }

            // Write all lines back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Final/newFinal_Project - Copy/src/billing.txt"))) {
                for (String str : lines) {
                    writer.write(str);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to generate the bill
    public void generateBill(double amount) {
        billingAmount = amount;
        System.out.println("Bill generated for patient " + patientID + ": $" + billingAmount);
        saveToFile();
    }

    // Method to add a payment
    public void addPayment(double amount) {
        paymentHistory.add(amount);
        billingAmount -= amount;
        System.out.println("Payment of $" + amount + " added. Remaining balance: $" + billingAmount);
        saveToFile();
    }

    // Method to get the payment status
    public String getPaymentStatus() {
        return billingAmount <= 0 ? "Paid" : "Unpaid";
    }

}
