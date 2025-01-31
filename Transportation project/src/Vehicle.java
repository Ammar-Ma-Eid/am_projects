import java.io.*;

public class Vehicle {
    Vtype type;
    int Capacity;
    String LicencePlate;


    public Vehicle(Vtype type, int capacity, String licencePlate) {
        this.type = type;
        Capacity = capacity;
        LicencePlate = licencePlate;
    }

    public static void AddVehicleToFile(Vtype type, int Capacity, String LicencePlate) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicle.txt", true))) {
            writer.write(type+","+Capacity+","+LicencePlate);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayVehicles() {
        try (BufferedReader br = new BufferedReader(new FileReader("vehicle.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setType(Vtype type) {
        this.type = type;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public void setLicencePlate(String licencePlate) {
        LicencePlate = licencePlate;
    }
}
