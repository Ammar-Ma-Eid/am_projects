import java.io.*;
import java.util.Scanner;

public class PatientService {
    public static BinarySearchTree<Patient> patientBST = new BinarySearchTree<>();

    public boolean registerPatient(Patient newPatient) {
        if (patientBST.contains(newPatient)) {
            return false;
        }
        patientBST.insert(newPatient);
        savePatientToFile(newPatient);
        return true;
    }

    private void savePatientToFile(Patient newPatient) {
        File file = new File("Final/newFinal_Project - Copy/src/Patient");
        try (FileWriter fileWriter = new FileWriter(file, true)) {

            fileWriter.write(newPatient.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean loginPatient(String email, String password) {
        File file = new File("Final/newFinal_Project - Copy/src/Patient");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data[0].equalsIgnoreCase(email) && data[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Patient getPatientByEmail(String email) {
        File file = new File("Final/newFinal_Project - Copy/src/Patient");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data[0].equalsIgnoreCase(email)) {

                    return new Patient(data[0], data[1], data[2], Integer.parseInt(data[3]), Integer.parseInt(data[4]),
                            data[5], data[6], Integer.parseInt(data[7]), Integer.parseInt(data[8]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

