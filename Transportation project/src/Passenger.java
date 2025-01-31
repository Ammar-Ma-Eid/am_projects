import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Passenger extends User {


    public Passenger(int id, String name, String username, String password, String file_name) {
        super(id,name,  username, password, file_name );
    }

    public void BookTrip(int ID) {
        if (Trip.modAvailableSeats(ID, -1)){
        try (BufferedReader reader = new BufferedReader(new FileReader("passenger.txt"))) {
            Scanner input = new Scanner(System.in);
            System.out.println("1-One Way \n2-Round Trip");
            int nn = input.nextInt();
            while (nn != 1 && nn != 2) {
                System.out.println("wrong input try again");
                System.out.println("1-One Way \n2-Round Trip");
                nn = input.nextInt();
            }
            nWay way = (nn == 1) ? nWay.OneWay : nWay.Round_Trip;

            ArrayList<String> lines = new ArrayList<>();
            boolean userFound = false;

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (username.equals(parts[2]) && password.equals(parts[3])) {

                    line += "," + ID + "-" + way;
                    userFound = true;
                }
                lines.add(line);
            }

            if (!userFound) {
                System.err.println("Failed to find the user in the file.");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("passenger.txt"))) {
                for (String modifiedLine : lines) {
                    writer.write(modifiedLine + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }}else {
            System.out.println("There is no available seats in this trip");
        }
    }


    public void CancelTrip(int IDtoremove) {
        String filePath = "passenger.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter("passenger_tmp.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && Integer.parseInt(parts[0]) == ID) {
                    if (parts.length > 4) {
                        for (int i = 4; i < parts.length; i++) {
                            String[] trip = parts[i].split("-");
                            if (Integer.parseInt(trip[0].trim()) == IDtoremove) {
                                parts[i] = "";
                                break;
                            }
                        }
                        StringBuilder newLine = new StringBuilder();
                        for (String part : parts) {
                            if (!part.isEmpty()) {
                                newLine.append(part).append(",");
                            }
                        }
                        if (newLine.length() > 0) {
                            newLine.deleteCharAt(newLine.length() - 1);
                        }
                        line = newLine.toString();
                    }
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader tempReader = new BufferedReader(new FileReader("passenger_tmp.txt"));
             BufferedWriter originalWriter = new BufferedWriter(new FileWriter(filePath))) {

            String line;
            while ((line = tempReader.readLine()) != null) {
                originalWriter.write(line);
                originalWriter.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Trip.modAvailableSeats(IDtoremove, 1);
    }

    public static int SelectTrip() {
        Scanner input = new Scanner(System.in);
        System.out.println("Select trip by entering trip ID");
        Trip.displayTrips();
        int id = input.nextInt();
        return id;
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
                            displayTripDetails(tripId[0], tripDetailsFilePath, type);
                        }
                    }
                    return;
                }

            }
            System.out.println("No trips found for the provided username and password.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void displayTripDetails(String tripId, String tripDetailsFilePath, String Way) {
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
                    System.out.println("Ticket Type: " + Way);
                    if (Way.equals("Round_Trip"))System.out.println("Trip price: " +( Integer.parseInt(parts[5])*2) );
                    else System.out.println("Trip price: "+parts[5]);
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
            System.out.println("1-Select and book trip");
            System.out.println("2-Cancel trip");
            System.out.println("3-Display my trips");
            System.out.println("4-Display info");
            System.out.println("0-Exit");
            n = in.nextInt();

            switch (n) {
                case 1:
                    BookTrip(SelectTrip());
                    break;
                case 2:
                    System.out.println("Want to view your trips first?\n1-Yes 2-No");
                    int v = in.nextInt();
                    if (v == 1) {
                        findTrips();
                    }
                    System.out.println("Enter trip ID");
                    int id = in.nextInt();
                    CancelTrip(id);
                    break;
                case 3:
                    findTrips();
                    break;
                case 4:
                    System.out.println(DisplayInfo());
                    break;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Unexpected value: " + n);
            }
        } while (true);
    }





}
