import java.io.*;

public class Trip {
    protected static String  FILE_NAME= "trip.txt";
    private static int TnextID = readIDFromFile();
    protected static int id=TnextID++;




    public static void AddTripToFile(String source, String destination, TripType type, double price,  int noStops, int availableSeats ) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(id+","+source+","+destination+","+noStops+","+type+","+price+","+availableSeats);
            writer.newLine();
            writer.flush();
            TupdateIDFile(TnextID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Boolean  modAvailableSeats( int Tid , int amount) {
        StringBuilder Stb = new StringBuilder();
        boolean lineReplaced = false; // Flag to check if the line is replaced
        try (BufferedReader reader = new BufferedReader(new FileReader("trip.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                    String storedId = parts[0];
                    if (Tid== Integer.parseInt(storedId)) {
                        if (amount==-1&&Integer.parseInt(parts[6])==0) {
                            return false;
                        }else {
                        line = parts[0]+","+parts[1]+","+parts[2]+","+parts[3]+","+parts[4]+","+parts[5]+","+(Integer.parseInt(parts[6])+amount);  // Append trip information to the line
                        if (parts.length>7&&parts[7]!=null)line= line +","+parts[7];
                        lineReplaced = true;
                        return true;
                        }

                    }
                Stb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!lineReplaced) {
            System.err.println("Failed to find the user in the file.");

        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("trip.txt"))) {
            writer.write(Stb.toString());
            System.out.println("Assigned successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    return  true;
    }




    private static int readIDFromFile() {
        File file = new File("Tid.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 1; // Default starting ID
    }
    private static void TupdateIDFile(int id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Tid.txt"))) {
            writer.write(Integer.toString(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void removeLineById(String filePath, int idToRemove) {
            File inputFile = new File(filePath);
            File tempFile = new File("temp.txt");

            try {
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String currentLine;

                while ((currentLine = reader.readLine()) != null) {
                    String[] parts = currentLine.split(",");
                    int id = Integer.parseInt(parts[0]);

                    if (id != idToRemove) {
                        writer.write(currentLine + "\n");
                    }
                }

                writer.close();
                reader.close();

                if (!inputFile.delete()) {
                    System.out.println("Could not delete the original file.");
                    return;
                }

                if (!tempFile.renameTo(inputFile)) {
                    System.out.println("Could not rename the temporary file.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    public static void displayTrips() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

