
import javax.imageio.IIOException;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        System.out.println("----------------------------");
        System.out.println("Welcome to HOGWARTS EXPRESS");
        System.out.println("----------------------------");
        System.out.println();
        Scanner input = new Scanner(System.in);
        System.out.println("1- Login\n2- Sign Up");
        int op ;
        try {
            op= Integer.parseInt(input.next());
            while (op!=1&&op!=2){
                System.out.println("wrong input try again");
                System.out.println("1-Login\n2-sign Up");
                op=input.nextInt();
            }
            if (op==1){
                User user =new User();
                user.login();
            }else {
                User user =new User();
                user.registerUser();
            }

        }catch (NumberFormatException e) {
            System.err.println("Invalid ID format: ");


        }






    }
}