import java.io.*;
import java.util.*;

public class Main {
    static Scanner input = new Scanner(System.in);
    public static BinarySearchTree<Patient> patientBST = new BinarySearchTree<>();

    public static void main(String[] args) {
       new HospitalDashBoard();
        System.out.println("Welcome to Hospital: ");
        System.out.println("1. Sign Up 2. Sign In 3. Exit");
        int choice = input.nextInt();
        input.nextLine();
        if (choice == 1) {
            patientSignup();
        } else if (choice == 2) {
            patientLogin();
        }



    }

    public static void patientSignup(){
        Patient newPatient = new Patient();
        Appointment appointment=new Appointment();
        System.out.print("Full Name: ");
        newPatient.setName(input.nextLine());
        System.out.print("Age: ");
        newPatient.setAge(input.nextInt());
        input.nextLine();
        System.out.println("Do you have any medicalHistory: ");
        newPatient.setMedicalHistory(input.nextLine());
        System.out.println("Please enter your contactInfo: ");
        System.out.println("Enter address: ");
        newPatient.setAddress(input.nextLine());
        System.out.println("Phone number: ");
        newPatient.setPhoneNumber(Integer.parseInt(input.nextLine()));
        System.out.println("relativeNumber: ");
        newPatient.setRelativeNumber(Integer.parseInt(input.nextLine()));
        System.out.print("Email: ");
        newPatient.setEmail(input.nextLine());
        System.out.print("Password: ");
        newPatient.setPassword(input.nextLine());
        if (registerPatient(newPatient.getEmail(), newPatient.getPassword(), newPatient.getName(), newPatient.getPatientID(), newPatient.getAge(), newPatient.getMedicalHistory(),newPatient.getAddress(), newPatient.getPhoneNumber(), newPatient.getRelativeNumber())){
            System.out.println("1. Book an Appointment 2. Cancel Appointment 3. get Info 4. Update contacts 5. Generate Report 6. reschedule 7. Exit");
            int option=input.nextInt();
            input.nextLine();
            while (option!=7){
                switch (option) {
                    case 1 -> {
                        System.out.println("Choose a Department: 1. General 2. Cardiology 3. Pediatrics");
                        int departmentChoice = input.nextInt();
                        input.nextLine();
                        switch (departmentChoice) {
                            case 1 -> appointment.setDepartment("General");
                            case 2 -> appointment.setDepartment("Cardiology");
                            case 3 -> appointment.setDepartment("Pediatrics");
                            default -> System.out.println("Invalid department choice");
                        }
                        f(appointment.getDepartment(), appointment);

                        Billing billing=new Billing(String.valueOf(newPatient.getIDForReport()));
                        System.out.println("The appointment amount is "+appointment.amount+"\n Agree to pay? \n (Yes/No)");
                        String ans=input.next();
                        if (ans.equals("No")) break;
                        else {
                            billing.generateBill(appointment.amount);
                            billing.addPayment(appointment.amount);
                        }
                        if (appointment.schedule(newPatient, appointment)) {
                            System.out.println("Appointment booked successfully!");
                            System.out.println("Appintment ID: "+appointment.getAppointmentID());
                        } else {
                            System.out.println("Failed to book appointment. Try a different date/time.");
                        }
                    }
                    case 2 -> {
                        System.out.println("Enter the Appointment ID to cancel: ");
                        int appointmentID = Integer.parseInt(input.nextLine());
                        if (appointment.cancelAppointment(newPatient, appointmentID)) {
                            System.out.println("Appointment canceled successfully.");
                        } else {
                            System.out.println("Failed to cancel appointment or appointment not found.");
                        }
                    }
                    case 3 -> {
                        System.out.println("Your Information: ");
                        newPatient.getPatientInfo();
                    }
                    case 4 -> {
                        System.out.println("Enter address: ");
                        newPatient.setAddress(input.nextLine());
                        System.out.println("Phone number: ");
                        newPatient.setPhoneNumber(Integer.parseInt(input.nextLine()));
                        System.out.println("relativeNumber: ");
                        newPatient.setRelativeNumber(Integer.parseInt(input.nextLine()));
                        newPatient.updateContactInfo(newPatient.getEmail(), newPatient.getPassword(), newPatient.getAddress(), newPatient.getPhoneNumber(),newPatient.getRelativeNumber());
                    }
                    case 5 -> {
                        System.out.println("1. generate Patient Report 2. generate Appointment Report 3.generate Revenue Report");
                        int ch= input.nextInt();
                        if (ch==1){
                            ReportGenerator patientReportGen = new ReportGenerator("PatientReport");
                            patientReportGen.generatePatientReport(String.valueOf(newPatient.getIDForReport()));
                        }
                        else if (ch==2){
                            ReportGenerator appointmentReportGen = new ReportGenerator("AppointmentReport");
                            appointmentReportGen.generateAppointmentReport(String.valueOf(appointment.getAppointmentID()));
                        }
                        else if (ch==3){
                            ReportGenerator revenueReportGen = new ReportGenerator("RevenueReport");
                            revenueReportGen.generateRevenueReport(String.valueOf(newPatient.getIDForReport()));
                        }
                        else System.out.println("Wrong choice");
                    }
                    case 6 -> {
                        System.out.println("Your Information: ");
                        System.out.println("Enter the Appointment ID to reschedule: ");
                        int appointmentID = Integer.parseInt(input.nextLine());

                        // Extract the department based on the entered Appointment ID
                        String department = getDepartmentFromAppointment(newPatient, String.valueOf(appointmentID));

                        if (department == null) {
                            System.out.println("Invalid Appointment ID. Department not found.");
                            break; // Exit the case if no valid department was found
                        }

                        f(department, appointment); // Pass the extracted department to the method

                        if (appointment.reschedule(newPatient, String.valueOf(appointmentID), appointment)) {
                            System.out.println("Appointment rescheduled successfully!");
                        } else {
                            System.out.println("Failed to reschedule appointment. Try a different date/time.");
                        }
                    }

                    default -> System.out.println("Invalid option. Try again.");
                }
                System.out.println("1. Book an Appointment 2. Cancel Appointment 3. get Info 4. Update contacts 5. Generate Report 6. reschedule 7. Exit");
                    option=input.nextInt();
                    input.nextLine();

            }

        }

    }



    public static boolean f(String department, Appointment appointment) {
        String path = "Final/newFinal_Project - Copy/src/Appointment"; // Path to the appointment file
        List<String> availableDates = new ArrayList<>();
        Map<String, List<String>> dateTimeMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            // Read the file and populate availableDates and dateTimeMap
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 3 && data[1].equalsIgnoreCase(department)) {
                    String date = data[2]; // Date is at index 2
                    String[] times = data[3].split(";"); // Times are at index 3, split by ";"

                    if (!availableDates.contains(date)) {
                        availableDates.add(date);
                    }
                    dateTimeMap.put(date, Arrays.asList(times));
                }
            }

            // If no dates are available
            if (availableDates.isEmpty()) {
                System.out.println("No appointments available for this department.");
                return false;
            }

            // Display available dates
            System.out.println("Choose Preferred Date:");
            for (int i = 0; i < availableDates.size(); i++) {
                System.out.println((i + 1) + ". " + availableDates.get(i));
            }

            // User selects a date
            Scanner input = new Scanner(System.in);
            int dateChoice = input.nextInt();
            input.nextLine(); // Consume the newline character

            if (dateChoice > 0 && dateChoice <= availableDates.size()) {
                String selectedDate = availableDates.get(dateChoice - 1);
                System.out.println("You have selected: " + selectedDate);

                // Display available times for the selected date
                List<String> availableTimes = dateTimeMap.get(selectedDate);
                System.out.println("Choose Preferred Time:");
                for (int i = 0; i < availableTimes.size(); i++) {
                    System.out.println((i + 1) + ". " + availableTimes.get(i));
                }

                // User selects a time
                int timeChoice = input.nextInt();
                input.nextLine(); // Consume the newline character

                if (timeChoice > 0 && timeChoice <= availableTimes.size()) {
                    String selectedTime = availableTimes.get(timeChoice - 1);
                    System.out.println("You have selected: " + selectedTime);

                    // Set the selected date and time in the Appointment object
                    appointment.setDate(String.valueOf(selectedDate));
                    appointment.setTime(String.valueOf(selectedTime));
                } else {
                    System.out.println("Invalid time choice.");
                }
            } else {
                System.out.println("Invalid date choice.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Appointment file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading the appointment file: " + e.getMessage());
        }
        return true;
    }



    public static boolean registerPatient(String email, String password,String name,int ID,int age,String medicalHistory,String address,int phone,int relativeNumber) {
        Patient newPatient = new Patient(email, password, name, ID, age, medicalHistory,address,phone, relativeNumber);

        // Check for duplicate email
        if (patientBST.contains(newPatient)) {
            System.out.println("Email already exists. Please use a different email.");
            return false;
        }

        // Add to the BST
        patientBST.insert(newPatient);
        System.out.println("Patient registered successfully in BST.");

        // Append to the file
        File file = new File("Final/newFinal_Project - Copy/src/Patient");
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(email + "," + password + "," + name + "," + ID + "," + age + "," + medicalHistory + "," + address +"," + phone+"," +relativeNumber+ "\n");
            System.out.println("Patient saved to file.");
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to patient records: " + e.getMessage());
        }

        return false;
    }



    public static void patientLogin(){
        Appointment appointment = new Appointment();
        Patient newPatient = new Patient();
        System.out.println("Enter your Email: ");
        newPatient.setEmail(input.nextLine());
        System.out.println("Enter your Password: ");
        newPatient.setPassword(input.nextLine());
        if (!patientLogin(newPatient.getEmail(), newPatient.getPassword())) {
            System.out.println("Invalid Email or Password. Please try again.");
        } else {
            System.out.println("1. Book an Appointment 2. Cancel Appointment 3. get Info 4. Update contacts 5. Generate Report 6.reschedule 7. Exit");
            int option=input.nextInt();
            input.nextLine();
            while (option!=7){
                switch (option) {
                    case 1 -> {
                        System.out.println("Choose a Department: 1. General 2. Cardiology 3. Pediatrics");
                        int departmentChoice = input.nextInt();
                        input.nextLine();
                        switch (departmentChoice) {
                            case 1 -> appointment.setDepartment("General");
                            case 2 -> appointment.setDepartment("Cardiology");
                            case 3 -> appointment.setDepartment("Pediatrics");
                            default -> System.out.println("Invalid department choice");
                        }
                        f(appointment.getDepartment(),appointment);
                        Billing billing=new Billing(String.valueOf(newPatient.getIDForReport()));
                        System.out.println("The appointment amount is "+appointment.amount+"\n Agree to pay? \n (Yes/No)");
                        String ans=input.next();
                        if (ans.equals("No")) break;
                        else {
                            billing.generateBill(appointment.amount);
                            billing.addPayment(appointment.amount);
                        }
                        if (appointment.schedule(newPatient,appointment)) {
                            System.out.println("Appointment booked successfully!");
//                            System.out.println("Appintment ID: "+appointment.getAppointmentID());
                        } else {
                            System.out.println("Failed to book appointment. Try a different date/time.");
                        }
                    }
                    case 2 -> {
                        System.out.println("Enter the Appointment ID to cancel: ");
                        int appointmentID = Integer.parseInt(input.nextLine());
                        if (appointment.cancelAppointment(newPatient,appointmentID)) {
                            System.out.println("Appointment canceled successfully.");
                        } else {
                            System.out.println("Failed to cancel appointment or appointment not found.");
                        }
                    }
                    case 3 -> {
                        System.out.println("Reports:");
                        newPatient.getPatientInfo();
                    }
                    case 4 -> {
                        System.out.println("Enter address: ");
                        newPatient.setAddress(input.nextLine());
                        System.out.println("Phone number: ");
                        newPatient.setPhoneNumber(Integer.parseInt(input.nextLine()));
                        System.out.println("relativeNumber: ");
                        newPatient.setRelativeNumber(Integer.parseInt(input.nextLine()));
                        newPatient.updateContactInfo(newPatient.getEmail(), newPatient.getPassword(), newPatient.getAddress(), newPatient.getPhoneNumber(),newPatient.getRelativeNumber());                    }
                    case 5 -> {
                        System.out.println("1. generate Patient Report 2. generate Appointment Report 3.generate Revenue Report");
                        int ch= input.nextInt();
                        if (ch==1){
                            ReportGenerator patientReportGen = new ReportGenerator("PatientReport");
                            patientReportGen.generatePatientReport(String.valueOf(newPatient.getIDForReport()));
                        }
                        else if (ch==2){
                            ReportGenerator appointmentReportGen = new ReportGenerator("AppointmentReport");
                            appointmentReportGen.generateAppointmentReport(String.valueOf(appointment.getAppointmentID()));
                        }
                        else if (ch==3){
                            ReportGenerator revenueReportGen = new ReportGenerator("RevenueReport");
                            revenueReportGen.generateRevenueReport(String.valueOf(newPatient.getIDForReport()));
                        }
                        else System.out.println("Wrong choice");
                    }
                    case 6 -> {
                        System.out.println("Your Information: ");
                        System.out.println("Enter the Appointment ID to reschedule: ");
                        int appointmentID = Integer.parseInt(input.nextLine());

                        // Extract the department based on the entered Appointment ID
                        String department = getDepartmentFromAppointment(newPatient, String.valueOf(appointmentID));

                        if (department == null) {
                            System.out.println("Invalid Appointment ID. Department not found.");
                            break; // Exit the case if no valid department was found
                        }

                        f(department, appointment); // Pass the extracted department to the method

                        if (appointment.reschedule(newPatient, String.valueOf(appointmentID), appointment)) {
                            System.out.println("Appointment rescheduled successfully!");
                        } else {
                            System.out.println("Failed to reschedule appointment. Try a different date/time.");
                        }
                    }
                    default -> System.out.println("Invalid option. Try again.");

                }
                System.out.println("1. Book an Appointment 2. Cancel Appointment 3. get Info 4. Update contacts 5. Generate Report 6. reschedule 7. Exit");
                option=input.nextInt();
                input.nextLine();
            }

        }
    }

    public static boolean patientLogin(String email, String password) {
        File file = new File("Final/newFinal_Project - Copy/src/Patient");

        try (FileReader myReader = new FileReader(file);
             Scanner scanner = new Scanner(myReader)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data[0].equalsIgnoreCase(email) && data[1].equals(password)) {
                    System.out.println("Login successful! Welcome, " + data[2]);
                    return true;
                }
            }

        } catch (IOException e) {
            System.out.println("Error accessing patient records: " + e.getMessage());
        }

        System.out.println("Invalid email or password.");
        return false;
    }
    //










    private static String getDepartmentFromAppointment(Patient patient, String appointmentID) {
        String filePath = "Final/newFinal_Project - Copy/src/Patient"; // Path to the patient file

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 9); // Assuming appointments are stored in index 8
                if ((data[0] + data[1]).equals(patient.getEmail() + patient.getPassword())) {
                    // Found the matching patient
                    if (data.length > 8) {
                        String[] appointments = data[8].split(","); // Split appointments by ","
                        for (String singleAppointment : appointments) {
                            String[] appointmentDetails = singleAppointment.split(";"); // Split details by ";"
                            if (appointmentDetails.length >= 2 && appointmentDetails[0].equals(appointmentID)) {
                                return appointmentDetails[1]; // Return the department
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the patient file: " + e.getMessage());
        }

        return null; // Return null if no matching appointment or department is found
    }

}