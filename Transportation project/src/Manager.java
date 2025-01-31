import java.io.*;
import java.util.Scanner;
import java.util.InputMismatchException;


public class Manager extends Employee {
    public Manager(int id, String name, String username, String password, String file_name, String EmployeeType) {
        super(id, name, username, password, file_name, EmployeeType);
    }
    public void AddTrip() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Source:");
        String source = input.nextLine();
        System.out.println("Enter Destination:");
        String destination = input.nextLine();
        int n;
        do {
            try {
                System.out.println("1-Internal \n2-External");
                n = input.nextInt();
                if (n != 1 && n != 2) {
                    System.out.println("Wrong input, try again.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine(); // Consume invalid input
            }
        } while (true);

        TripType type = (n == 1) ? TripType.Internal : TripType.External;

        System.out.println("Enter price:");
        double price = input.nextDouble();
        System.out.println("Enter number of stops:");
        int noStops = input.nextInt();
        int availableSeats;
        do {
            try {
                System.out.println("Enter type of the vehicle:\n1-Bus\n2-Mini bus\n3-Limousine");
                int tv = input.nextInt();
                if (tv != 1 && tv != 2 && tv != 3) {
                    System.out.println("Wrong input, try again.");
                    continue;
                }
                availableSeats = (tv == 1) ? 55 : (tv == 2) ? 14 : (tv == 3) ? 2 : 0;
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        } while (true);

        Trip.AddTripToFile(source, destination, type, price, noStops, availableSeats);
    }

    public static void CancelTrip() {
        Trip.displayTrips();
        Scanner input = new Scanner(System.in);
        System.out.println("\nEnter trip ID to cancel:");
        int IdRemove;
        do {
            try {
                IdRemove = input.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        } while (true);
        Trip.removeLineById("trip.txt", IdRemove);
        System.out.println("Trip removed successfully.");
    }


    public static void AssignDriver(String Name, int TripID) {

        StringBuilder s = new StringBuilder();
        boolean lineReplaced = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("trip.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    if (TripID == Integer.parseInt(parts[0])) {
                        parts[7] = Name;
                        line = String.join(",", parts);
                        lineReplaced = true;
                    }
                } else if (parts.length == 7) {
                    line = String.join(",", parts) + "," + Name;
                    lineReplaced = true;
                } else {
                    System.err.println("Invalid data format in the file for line: " + line);
                }
                s.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!lineReplaced) {
            System.err.println("Failed to find the ID in the file.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("trip.txt"))) {
            writer.write(s.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Driver.AssignTripToDriver(Name, TripID);
    }

    public static void AddVehicle() {
        Scanner input = new Scanner(System.in);
        int n;
        do {
            try {
                System.out.println("1-Bus \n2-Minibus\n3-Limousine");
                n = input.nextInt();
                if (n != 1 && n != 2 && n != 3) {
                    System.out.println("Wrong input, try again.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        } while (true);

        Vtype type = (n == 1) ? Vtype.Bus : (n == 2) ? Vtype.Minibus : Vtype.Limousine;

        System.out.println("Enter Capacity:");
        int capacity = input.nextInt();
        input.nextLine();
        System.out.println("Enter Licence Plate:");
        String licencePlate = input.nextLine();

        Vehicle.AddVehicleToFile(type, capacity, licencePlate);

        System.out.println("Added successfully");
    }

    public static void GenerateReport() {
        System.out.println("Vehicles:");
        Vehicle.displayVehicles();
        System.out.println("\n____________________________\n");
        System.out.println("Managers:");
        displayManager();
        System.out.println("\n____________________________\n");
        System.out.println("Drivers:");
        Driver.displayDriver();
        System.out.println("\n____________________________\n");
        System.out.println("Trips:");
        Trip.displayTrips();
    }

    public static void displayManager() {
        try (BufferedReader br = new BufferedReader(new FileReader("manager.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void AddEmployee() {
        Scanner input = new Scanner(System.in);
        int type;
        do {
            try {
                System.out.println("Choose type:\n2-Manager\n3-Driver");
                type = input.nextInt();
                if (type != 2 && type != 3) {
                    System.out.println("Wrong input, try again.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        } while (true);
        input.nextLine();
        System.out.println("Enter name:");
        String name = input.nextLine().trim();
        System.out.println("Enter username:");
        String username = input.nextLine().trim();
        System.out.println("Enter password:");
        String password = input.nextLine().trim();
        User user=new User();
        if (!Exists(username,type)){
        user.Registration(name, username, password, type);
        System.out.println("Employee added successfully.");}
        else {
            System.out.println("Employee is already exist");
        }
    }


    private static boolean Exists(String username, int type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(getFileName(type)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].equals(username)) {
                    return true; // Username exists
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



    public void run() {
        Scanner in = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\nChoose:");
            System.out.println("1-Add Trip");
            System.out.println("2-Add Vehicle");
            System.out.println("3-Add Employee");
            System.out.println("4-Cancel Trip");
            System.out.println("5-Assign Trip to Driver");
            System.out.println("6-Generate Report");
            System.out.println("7-Display info");
            System.out.println("0-Exit");
            choice = in.nextInt();
            switch (choice) {
                case 1:
                    AddTrip();
                    break;
                case 2:
                    AddVehicle();
                    break;
                case 3:
                    AddEmployee();
                    break;
                case 4:
                    CancelTrip();
                    break;
                case 5:
                    System.out.println("Select Trip by ID:");
                    Trip.displayTrips();
                    int tripId = in.nextInt();
                    in.nextLine();
                    System.out.println("Want to display drivers?");
                    System.out.println("1-Yes  2-No");
                    int n= in.nextInt();
                    if (n==1)Driver.displayDriver();
                    in.nextLine();
                    System.out.println("Select Driver by Name:");
                    String driverName = in.nextLine().trim();
                    AssignDriver(driverName, tripId);
                    break;
                case 6:
                    System.out.println("HOGWARTS EXPRESS");
                    System.out.println("----------------------------");
                    System.out.println();
                    GenerateReport();
                    break;
                case 7:
                    displayManager();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Unexpected value: " + choice);
            }
        } while (true);
    }
}
