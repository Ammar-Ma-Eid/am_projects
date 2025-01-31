import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String firstName=null, LastName=null ,address=null,city=null,mobileNumber =null;

        try {
            // FName,LName,Street,City,PhoneNumber
          Scanner scanner = null;
                        int choice;
                        String filename=null;

                        do {
                            System.out.println("0.Click to create File");
                           System.out.println("1.Click to Load");
                           System.out.println("2.Click to Query");
                           System.out.println("3.Click to Add:");
                           System.out.println("4.Click to Modify");
                           System.out.println("5.Click to Delete:");
                           System.out.println("6.Click to Print:");
                           System.out.println("7.Click to Sava:");
                           System.out.println("8.Click to QUIT:");
                           choice = in.nextInt();
                           switch (choice) {
                               case 0:
                                   System.out.println("Enter File name");
                                   in.nextLine();
                                   String newFile= in.nextLine();
                                   File CreateFile = new File(newFile);
                                   if (!CreateFile.exists()) {
                                       try {
                                           CreateFile.createNewFile();
                                           System.out.println("File created Successfully");
                                           System.out.println();
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }
                                   }
                                   break;

                             case 1:
                                 in.nextLine();
                                 System.out.println("1. We will load the Data  : ");
                                 System.out.println("Enter the file name:");
                                  filename= in.nextLine();
                                 try {
                                     File reader1 = new File(filename);
                                     scanner = new Scanner(reader1);
                                     System.out.println("found the file");
                                 }catch (IOException ex){
                                     System.out.println("No file with that name :");
                                     System.out.println("Try again");
                                 }

                             break;

                             case 2:
                                 if (filename==null){ System.out.println("Please load a file First"); break;}
                             System.out.println( " 2.You have selected the query ");
                             System.out.print(" Enter last name to search: ");
                            String lastName = new Scanner(System.in).nextLine();
                            boolean ok = false;
                            while (scanner.hasNextLine()) {
                                String mahmoud = scanner.nextLine();
                                   if (mahmoud.contains(lastName)) {
                                   System.out.println("__Record found successfully__");
                                  System.out.println(" Information is: " + mahmoud);
                                    ok = true;
                                } }
                          if (!ok) {
                              System.out.println("not found ___bye bye ☹__");
                         }
                                        break;

                       case 3:
                           if (filename==null){ System.out.println("Please load a file First"); break;}
                           System.out.println("3.You chose add  ");
                           System.out.println(" Enter first name : ");
                            firstName= in.next();
                           System.out.println("Enter last name :" );
                            LastName= in.next();
                           System.out.println("Enter the address");
                            address = in.next();
                           System.out.println("Enter the city");
                            city = in.next();
                           System.out.println("Enter phone Number : ");
                            mobileNumber = in.next();

                        break;
                    case 4:
                        if (filename==null){ System.out.println("Please load a file First"); break;}
                        try {
                            in.nextLine();
                            // FName,LName,Street,City,PhoneNumber

                            System.out.println(" 4.you chose MODIFY ");
                            System.out.println("enter Last name to search about it");
                            String Search = in.next();

                            BufferedReader file = new BufferedReader(new FileReader(filename));

                            ArrayList<String> lines = new ArrayList<>();
                            boolean Found = false;
                            String line;
                            while ((line = file.readLine()) != null) {

                                String[]parts=line.split(",");
                                if (Search.equals(parts[1])) {
                                    int choose = 0;
                                    while (choose!=6) {
                                        System.out.println("choose what do you want to modify");
                                        System.out.println("1, First name");
                                        System.out.println("2, Last name");
                                        System.out.println("3, Street");
                                        System.out.println("4, City");
                                        System.out.println("5, Phone Number");
                                        System.out.println("6, done");
                                        choose =in.nextInt();
                                        in.nextLine();

                                    switch (choose){
                                        case 1:
                                            System.out.println("Enter First Name:");
                                            parts[0]= in.nextLine();
                                            break;
                                        case 2:
                                            System.out.println("Enter Last Name:");
                                            parts[1]= in.nextLine();
                                            break;
                                        case 3:
                                            System.out.println("Enter Street:");
                                            parts[2]= in.nextLine();
                                            break;
                                        case 4:
                                            System.out.println("Enter City");
                                            parts[3]= in.nextLine();
                                            break;
                                        case 5:
                                            System.out.println("Enter Phone Number");
                                            parts[4]= in.nextLine();
                                            break;
                                        case 6:
                                            System.out.println("Done modifying");
                                            break;
                                        default:
                                            System.out.println("Wrong input"+choose);
                                    }}
                                }
                                lines.add(line);
                            }
                            file.close();
                            if (!Found) {
                                System.out.println("Failed to find the name.");
                                return;
                            }
                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                                for (String modifiedLine : lines) {
                                    writer.write(modifiedLine + "\n");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            System.out.println("Modification complete.");


                        } catch (IOException e) {
                            System.out.println("Error: " );
                            e.printStackTrace();
                        }

                        break;
                      case 5:

                          if (filename==null){ System.out.println("Please load a file First"); break;}

                              in.nextLine();
                              System.out.println("you chose to delete");
                              System.out.println("Enter first Name");
                              String inFname =in.nextLine();
                              System.out.println("Enter last Name");
                              String inLname =in.nextLine();
                          String filePath = filename;
                          try {
                              File inputFile = new File(filePath);
                              List<String> lines = new ArrayList<>();
                              BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                              String currentLine;
                              while ((currentLine = reader.readLine()) != null) {
                                  lines.add(currentLine);
                              }
                              reader.close();

                              List<String> filteredLines = new ArrayList<>();
                              for (String line : lines) {
                                  String[] parts = line.split(",");
                                  if (!(parts.length >= 2 && parts[0].trim().equals(inFname) && parts[1].trim().equals(inLname))) {
                                      filteredLines.add(line);
                                  }
                              }

                              BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile));
                              for (String line : filteredLines) {
                                  writer.write(line + System.getProperty("line.separator"));
                              }
                              writer.close();
                              System.out.println("Record removed successfully.");
                          } catch (IOException e) {
                              System.out.println("Error reading/writing the file: " + e.getMessage());
                          }
                              break;



                          case 6 :
                              if (filename==null){ System.out.println("Please load a file First"); break;}
                 System.out.println("print was chosen");
                try {
                    BufferedReader lm = new BufferedReader(new FileReader( filename));
                    String file;
                    int filemallaboro = 1;
                    while ((file = lm.readLine()) != null){
                        System.out.println( filemallaboro +"___" + file );
                        filemallaboro++;
                    }
                    System.out.println("print done ");
                }catch (Exception e){
                    System.out.println("problem    ");
                }
                         break;
                        case 7:
                            System.out.println("you press to save ");
                            try {
                                FileWriter reader2 = new FileWriter(filename,true);
                                reader2.write("\n"+firstName + "," + LastName + "," + address +"," +city+","+ mobileNumber) ;
                                System.out.println("data added successfully ");
                                reader2.close();
                            }catch (Exception k ){
                                System.out.println("Eror 404!"); }
                  break;
                        case 8:
                            System.out.println("bye..bye");
                            break;
                        default:
                            System.out.println("Please choose number from 1 to 8");
                            break;
                           }


                } while (choice != 8);

         }
         catch (Exception m) {
             System.out.println("An error occurred ");
         }



    }
}
