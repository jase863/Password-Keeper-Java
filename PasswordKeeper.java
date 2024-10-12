import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

class PasswordKeeper {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Username: ");
        String rawUserName = "|" + userInput.nextLine();
        System.out.print("New User Name: ");
        String userName1 = userInput.nextLine();
        String userName = rawUserName.replace(rawUserName, userName1);

        // userName = userName.trim();
        System.out.println("Your user name was: " + rawUserName);
        System.out.println("Your user name is now: " + userName);
    }

}