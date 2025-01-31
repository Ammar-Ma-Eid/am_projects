import java.io.*;
import java.util.Scanner;

public class Driver extends Employee {


    public Driver(int id, String name, String username, String password ,String file_name,String EmployeeType) {
        super(id,name,username,password ,file_name ,EmployeeType);
    }



    public static void displayDriver() {
        try (BufferedReader br = new BufferedReader(new FileReader("driver.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void AssignTripToDriver(String Name,int ID){
        StringBuilder s = new StringBuilder();
        boolean lineReplaced = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("driver.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    if (parts[1].equalsIgnoreCase(Name)) {
                        line +=  "," + ID;
                        lineReplaced = true;
                    }
                } else {
                    System.err.println("Invalid data format in the file for line: " + line);
                }
                s.append(line+"\n");/*.append("\n");*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!lineReplaced) {
            System.err.println("Failed to find the user in the file.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("driver.txt"))) {
            writer.write(s.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void findTrips() {
        try (Scanner scanner = new Scanner(new File(File_Name))) {
            String tripDetailsFilePath = "trip.txt";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 4 && username.equals(parts[2]) && password.equals(parts[3])) {
                    for (int i = 4; i < parts.length; i++) {
                        String[] tripId = parts[i].split("-");
                        String type =tripId[1];
                        if (!tripId[0].isEmpty()) {
                            displayTripDetails(tripId[0], tripDetailsFilePath);
                        }
                    }
                    return;
                }
                else System.out.println("No Trips assigned");
            }
            System.out.println("No trips found for the provided username and password.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void displayTripDetails(String tripId, String tripDetailsFilePath) {
        try (Scanner scanner = new Scanner(new File(tripDetailsFilePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2 && tripId.equals(parts[0])) {
                    System.out.println();
                    System.out.println();
                    System.out.println("Trip ID: " + tripId);
                    System.out.println("Trip Source: " + parts[1]);
                    System.out.println("Trip Destination: " + parts[2]);
                    System.out.println("Trip number of stops: " + parts[3]);
                    System.out.println("Trip Type: " + parts[4]);
                    System.out.println("Trip Available Seats: " + parts[6]);
                    return;
                }
            }
            System.out.println("No details found for trip ID: " + tripId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        int n;
        do {
            System.out.println();
            System.out.println("Choose:");
            System.out.println("1- View assigned trips");
            System.out.println("2-View info");
            System.out.println("0-Exit");
            n = in.nextInt();

            switch (n) {
                case 1:
                    findTrips();
                    break;
                case 2:
                    DisplayInfo();
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Unexpected value: " + n);
            }
        } while (n!= 10);
    }

}
