import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
    protected String name;
    private static int nextID = readIDFromFile();
    protected int ID;
    protected String username;
    protected String password;
    protected String File_Name;

    public User(int ID, String name, String username, String password, String file_Name) {
        this.name = name;
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.File_Name = file_Name;
    }
    public User(){}

    public void findTrips() {
        String username = this.username;
        String password = this.password;
        String filePath = File_Name;
        String tripDetailsFilePath = "trip.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) { // Ensure the line is valid
                    String storedUsername = parts[2];
                    String storedPassword = parts[3];
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {

                        for (int i = 4; i < parts.length; i++) {
                            String tripId = parts[i];
                            if (!tripId.isEmpty()) {
                                displayTripDetails(tripId, tripDetailsFilePath);
                            }
                        }
                        return;
                    }
                }
            }
            System.out.println("No trips found for the provided username and password.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayTripDetails(String tripId, String tripDetailsFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(tripDetailsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Ensure the line is valid
                    String storedTripId = parts[0];
                    if (tripId.equals(storedTripId)) {
                        System.out.println();
                        System.out.println();
                        System.out.println("Trip ID: " + tripId);
                        System.out.println("Trip Source: " + parts[1]);
                        System.out.println("Trip Destination: " + parts[2]);
                        System.out.println("Trip number of stops: " + parts[3]);
                        System.out.println("Trip Type: " + parts[4]);
                        System.out.println("Ticket Type: " + parts[5]);
                        System.out.println("Trip price" + parts[6]);
                        System.out.println("Trip Available Seats: " + parts[7]);

                        return;
                    }
                }
            }
            System.out.println("No details found for trip ID: " + tripId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String DisplayInfo() {
        String type = "";
        if (File_Name.equals("passenger.txt")) {
            type = "Passenger";
        } else if (File_Name.equals("manager.txt")) {
            type = "Manager";
        } else if (File_Name.equals("driver.txt")) {
            type = "Driver";
        }

        return "Hogwarts Express\n" +
                "------------------------" +
                "\nAccount type:" + type +
                "\nname='" + name +
                "\nID=" + ID +
                "\nusername='" + username +
                "\npassword='" + password;
    }

    public void login() {
        Scanner input = new Scanner(System.in);
        int choose;
        try {
            System.out.println("Choose type 1-Passenger 2-Manager 3-Driver");
            choose = input.nextInt();
            while (choose != 1 && choose != 2 && choose != 3) {
                System.out.println("Wrong input. Try again.");
                System.out.println("Choose type 1-Passenger 2-Manager 3-Driver");
                choose = input.nextInt();
            }
            input.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            input.nextLine();
            login();
            return;
        }

        System.out.println("Enter your username:");
        String username = input.nextLine().trim();
        System.out.println("Enter your password:");
        String password = input.nextLine().trim();
        String log = authenticate(choose, username, password);
        if (log != null) {
            System.out.println("Login Successful!");
        } else {
            System.out.println("Invalid username or password. Please try again.");
            login();
        }
    }

    public String authenticate(int type, String username, String password) {
        String EmployeeType = null;
        if (type == 1) {
            File_Name = "passenger.txt";
            EmployeeType = "Passenger";
        } else if (type == 2) {
            File_Name = "manager.txt";
            EmployeeType = "Manager";
        } else if (type == 3) {
            File_Name = "driver.txt";
            EmployeeType = "Driver";
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(File_Name))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String storedUsername = parts[2];
                    String storedPassword = parts[3];
                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        if (type == 1) {
                            Passenger user1 = new Passenger(id, name, username, password, File_Name);
                            user1.run();
                        } else if (type == 2) {
                            Manager user2 = new Manager(id, name, username, password, File_Name, EmployeeType);
                            user2.run();
                        } else if (type == 3) {
                            Driver user3 = new Driver(id, name, username, password, File_Name, EmployeeType);
                            user3.run();
                        }
                        return "Login success";
                    }
                } else {
                    System.err.println("Invalid data format in the file for line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format in the file.");
        }
        return null;
    }

    public void registerUser() {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose type: 1-Passenger 2-Manager 3-Driver");
        int choose = validateChoice(input);
        input.nextLine();

        System.out.println("Enter your name:");
        String name = input.nextLine().trim();

        System.out.println("Enter your username:");
        String username = validateUsername(input, choose);

        System.out.println("Enter your password:");
        String password = validatePassword(input);

        Registration(name, username, password, choose);
        System.out.println("Registration Successful! Now you can login.");
        System.out.println();
        System.out.println();
        login();
    }

    private int validateChoice(Scanner input) {
        int choose;
        while (true) {
            choose = input.nextInt();
            if (choose >= 1 && choose <= 3) {
                break;
            } else {
                System.out.println("Invalid input. Choose type: 1-Passenger 2-Manager 3-Driver");
            }
        }
        return choose;
    }

    private String validateUsername(Scanner input, int type) {
        String username;
        while (true) {
            username = input.nextLine().trim();
            if (!username.isEmpty() && !usernameExists(username, type)) {
                break;
            } else {
                System.out.println("Username cannot be empty or already exists. Please enter again:");
            }
        }
        return username;
    }

    private boolean usernameExists(String username, int type) {
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

    public static String getFileName(int type) {
        if (type == 1) {
            return "passenger.txt";
        } else if (type == 2) {
            return "manager.txt";
        } else {
            return "driver.txt";
        }
    }

    private String validatePassword(Scanner input) {
        String password;
        while (true) {
            password = input.nextLine().trim();
            if (password.length() >= 6) {
                break;
            } else {
                System.out.println("Password must be at least 6 characters long. Please enter again:");
            }
        }
        return password;
    }
    public void Registration(String name, String username, String password, int type) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.ID = nextID++;
        saveToFile(type);
    }

    public void saveToFile(int type) {
        File_Name = getFileName(type);
        File file = new File(File_Name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(File_Name, true))) {
            writer.write(ID + "," + name + "," + username + "," + password);
            writer.newLine();
            writer.flush();
            updateIDFile(nextID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int readIDFromFile() {
        File file = new File("id.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 1;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static void updateIDFile(int id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("id.txt"))) {
            writer.write(Integer.toString(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
