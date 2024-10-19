import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


class PasswordKeeper {
    public static void main(String[] args) throws InterruptedException {

        Scanner userInput = new Scanner(System.in); // receives user input (used often in the program)
        FileHandler userFile = new FileHandler(); // object for accessing FileHandler methods 
        String fileName = ""; // empty String that is populated and used often
        HashMap <String, ArrayList<String>> userData = new HashMap<>(); // empty HashMap for storing password info.
        ArrayList<String> loginArray = new ArrayList<>(); // empty ArrayList for storing master username and password for HashMap.
        PasswordGroup passwordGroup = new PasswordGroup(); // object for accessing PasswordGroup methods
        String fileCheck = ""; // empty String that holds user response to whether a file exists
        String menuChoice = ""; // empty String that holds user response to menu options
        boolean isLoginTestPass = false; // boolean for determining whether a login attempt was successful
        boolean needToCreatePassword = false;

        // This while loop prompts the user for a file to write to. If one exists the user will be
        // prompted to log in.

        // If the file doesn't exist, the user will be offered another chance to enter a file name.
        // If the user gives up on entering the file name and selects no, the program will move on.
        // If the file name exists, the user will be prompted to log in.

        // If the user answers that a file has not been made yet, the program will prompt the user for
        /// a name, and it will create a new file to write to.
        while (!fileCheck.equals("y") && !fileCheck.equals("n") && !fileCheck.equals("done")) {
            System.out.print("Do you already have a password file? (Y/N) ");

            fileCheck = userInput.nextLine();
            while (fileCheck.equalsIgnoreCase("y")) {

                ClearScreen();
                System.out.print("What is the name of the file? (do not include .txt, .csv, etc.) ");

                fileName = userInput.nextLine();

                fileName += ".csv"; // converts the user's entered name to a CSV file name

                File potentialFile = new File(fileName);

                if (potentialFile.exists()) {

                    // This may occur if the user created a CSV file and quit the program
                    // before setting up a password, or the user's CSV file got emptied.
                    if (potentialFile.length() == 0) { 

                        ClearScreen();                                                      
                        System.out.println("You don't have login credentials yet. You will now be prompted to create some.");
                        TimeUnit.SECONDS.sleep(2);
                        needToCreatePassword = true;
                        fileCheck = "n";
                    }

                    else{
                        loginArray = userFile.ReadUserLogin(fileName); // Reads in user's master login info from the CSV file 
                        passwordGroup.SetMasterUserName(loginArray.get(0));
                        passwordGroup.SetMasterPassword(loginArray.get(1));
                        
                        ClearScreen();
                        isLoginTestPass = passwordGroup.loginAttempt(loginArray); // returns a boolean
                        fileCheck = "done";
                    }
                }

                else {

                    boolean isNewAttempt = false; // If this is set to true, it will run through the main loop again.

                    while (!isNewAttempt) {
                        ClearScreen();
                        System.out.println("That file doesn't exist.");
                        System.out.print("Would you like to try a different file name? (Y/N) ");

                        String newAttemptResponse = userInput.nextLine();

                        // User has chosen to attempt to enter another file name.
                        if (newAttemptResponse.equalsIgnoreCase("Y")) {

                            isNewAttempt = true;

                        }

                        // User has chosen not to attempt to enter a file name
                        else if (newAttemptResponse.equalsIgnoreCase("N")) {

                            needToCreatePassword = false;
                            fileCheck = "n"; // This is set to "n" to allow the user to create a file
                            
                            break;
                        }

                        else {

                            // error handling
                            System.out.println("That is not an option. Please enter 'Y' or 'N'");
                        }
                        
                    }
                }
                
            }

            if (fileCheck.equalsIgnoreCase("n")) {
                
                if (!needToCreatePassword) {
                    ClearScreen();
                    System.out.println("You will need to create a file to proceed.");
                    System.out.print("\nWhat would you like to name the file? (do not include .txt, .csv, etc.) ");

                    fileName = userInput.nextLine();
                    fileName += ".csv";

                    File file = new File(fileName);

                    userFile.CreateUserFile(fileName);

                    // This will allow the user to create an initial password.
                    needToCreatePassword = true;

                    // If the user has entered the name of an existing file, the program will
                    // prompt the user to log in. This ensures that the user cannot bypass
                    // logging in.
                    if (file.exists() && file.length() != 0) {

                        loginArray = userFile.ReadUserLogin(fileName);


                        ClearScreen();
                        System.out.println("That file already exists.");
                        System.out.println("\nYou will now be asked to login");

                        TimeUnit.SECONDS.sleep(2);

                        ClearScreen();
                        
                        isLoginTestPass = passwordGroup.loginAttempt(loginArray); // determines whether the user has entered correct login info
                    }
                }


            if (needToCreatePassword && !isLoginTestPass) {
                
                String challengeUserName = "|"; // These Strings start with "|" to allow the while loops to start
                String challengePassword = "|";

                // The following explains why these symbols can't be used in usernames and passwords:

                // Master login information contains "|" at the start to set it apart from the other information.
                // Site or app names contain "~" at the start to set them apart from username and passwords for the sites/apps
                // Commas are used as delimiters.

                // While loops are put in place to stop users from using the special symbols in credentials.
                while (challengeUserName.contains("|") || challengeUserName.contains("~") || challengeUserName.contains(",")) {
                    ClearScreen();
                    System.out.print("Please choose a username (Cannot contain '|', '~', or ','): ");
                    challengeUserName = userInput.nextLine();
                }

                while (challengePassword.contains("|") || challengePassword.contains("~") || challengePassword.contains(",")) {
                    ClearScreen();
                    System.out.print("Please choose password (Cannot contain '|', '~', or ','): ");
                    challengePassword = userInput.nextLine();
                }

                passwordGroup.SetMasterUserName(challengeUserName);
                passwordGroup.SetMasterPassword(challengePassword);

                userFile.WriteUserFile(fileName, "", challengeUserName, challengePassword, true);

                ClearScreen();
                isLoginTestPass = true;
                break;

            }

        }

            else if (!fileCheck.equalsIgnoreCase("y") && !fileCheck.equalsIgnoreCase("n") && !fileCheck.equalsIgnoreCase("done")) {
                ClearScreen();
                System.out.println("That is not an option\n");
                fileCheck = "";
            }

            else {

                ClearScreen();

                // If the user's login information is correct, the program will run; otherwise, it will print "Access Denied" twice, and the program will end.
                if (isLoginTestPass) {
                    System.out.println("Welcome!\n");
                }

                else {


                    for (int i = 0; i < 2; i++){
                    
                        userInput.close();
                        PasswordKeeper.ClearScreen();
                        System.out.print("Access");
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(" Denied");
                        TimeUnit.SECONDS.sleep(1);
        
                    }

                    break;
                }

            }
        }
        
        // Main Menu
// -------------------------------------------------------------------------------------

        ArrayList<String> menuOptions = new ArrayList<>(); // This ArrayList contains the menu options
        menuOptions.add("V");
        menuOptions.add("S");
        menuOptions.add("A");
        menuOptions.add("E");
        menuOptions.add("D");
        menuOptions.add("U");
        menuOptions.add("Q");
        menuOptions.add("q");

        File file = new File(fileName);
        
        // The menu will continue until the member chooses to quit the program.
        // In order for the menu to run, the user must pass the login attempt.
        while (!menuChoice.equalsIgnoreCase("Q") && isLoginTestPass) {

            userData = userFile.ReadFile(fileName);
            passwordGroup.SetUserData(userData); // This reads the file in so that it can be used.

            System.out.println("\nWhat would you like to do? ");

            System.out.println("(V) View sites/apps\n(S) Site/app login info\n(A) Add site/app & login info\n(E) Edit login info\n(D) Delete site/app login\n(U) Change master login\n(Q) Quit");

            System.out.print("\n-- ");
            

            menuChoice = userInput.nextLine();
            String userChoice = "";
            
            // shows a list of site/app names
            if (menuChoice.equalsIgnoreCase("V")) {

                ClearScreen();

                // populates the HashMap "userData" with the user's password information.
                userData = passwordGroup.GetUserData();

                if (file.length() == 0 || userData == null || userData.isEmpty()) {

                    System.out.println("The file is empty; nothing to display.");

                }

                else {

                    passwordGroup.ViewApps(fileName);

                    // This gives users time to view the information because it won't move on until enter has been pressed.
                    System.out.println("\nPress enter to continue.");

                    userChoice = userInput.nextLine();

                    userChoice = "";

                    ClearScreen();
                }
            }

            // shows a list of site/app names and allows users to type in the name of a site/app in the list
            // to view login credentials.
            else if (menuChoice.equalsIgnoreCase("S")) {
                
                ClearScreen();

                userData = passwordGroup.GetUserData();

                // check to see if the file is empty or the HashMap is empty (appears often)

                if (file.length() == 0 || userData == null || userData.isEmpty()) {

                    System.out.println("The file is empty; nothing to display.");

                }

                else {

                    userData = passwordGroup.GetUserData();

                    // If the user's option isn't in the list, the program will continue to ask for input till it is.
                    while (!userData.containsKey(userChoice)) {

                        ClearScreen();
                        passwordGroup.ViewApps(fileName);

                        System.out.print("\nPlease type the name of the site/app to view the login info OR type 'back' to cancel: ");

                        userChoice = userInput.nextLine();

                        if (userChoice.equalsIgnoreCase("back")) {
                            break;
                        }

                    }

                    // returns login info from the file
                    if (!userChoice.equalsIgnoreCase("back")) {
                        ArrayList<String> requestedData = passwordGroup.ViewLoginInfo(userData, userChoice);

                        System.out.println("\n" + userChoice);
                        System.out.println("Username: " + requestedData.get(0));
                        System.out.println("Password: " + requestedData.get(1));

                        System.out.println("\nPress enter to continue.");

                        userChoice = userInput.nextLine();
                        userChoice = "";
                    }

                    ClearScreen();
                }

            }

            // add site/app & login info
            else if (menuChoice.equalsIgnoreCase("A")) {

                ClearScreen();
                userData = passwordGroup.GetUserData();

                // writes user info to the file
                if (file.length() == 0 || userData == null) {

                    userFile.WriteUserFile(fileName, "", passwordGroup.GetMasterUserName(), passwordGroup.GetMasterPassword(), true);

                }

                String addAppName = "|";
                String addUserName = "|";
                String addPassword = "|";

                // This block gathers info to add to the file. The while loops ensure that special symbols can't be added.
                while (addAppName.contains("|") || addAppName.contains("~") || addAppName.contains(",")) {

                    ClearScreen();
                    System.out.println("Type 'back' to cancel adding.\n");

                    System.out.print("What is the name of the site/app to be added?" + " (Cannot contain '|', '~', or ','): ");
                    addAppName = userInput.nextLine();

                    if (addAppName.equalsIgnoreCase("back")) {
                        break;
                    }

                }

                if (!addAppName.equalsIgnoreCase("back")) {
                    while (addUserName.contains("|") || addUserName.contains("~") || addUserName.contains(",")) {
                        
                        ClearScreen();
                        System.out.print("\nWhat is your username for " + addAppName + "?" + " (Cannot contain '|', '~', or ','): ");
                        addUserName = userInput.nextLine();
                    }

                    while (addPassword.contains("|") || addPassword.contains("~") || addPassword.contains(",")) {
                        
                        ClearScreen();
                        System.out.print("\nWhat is your password for " + addAppName + "?" + " (Cannot contain '|', '~', or ','): ");
                        addPassword = userInput.nextLine();
                    }


                    userFile.WriteUserFile(fileName, addAppName, addUserName, addPassword, false); // method call to add elements to the file

                }

                ClearScreen();
            }

            // edit login info
            else if (menuChoice.equalsIgnoreCase("E")) {

                ClearScreen();

                String editAppName = "";

                userData = passwordGroup.GetUserData();

                // determines if the file is empty
                if (file.length() == 0 || userData == null || userData.isEmpty()) {

                    ClearScreen();
                    System.out.println("The file is empty; nothing to change.");

                }

                else{

                    ClearScreen();
                    passwordGroup.ViewApps(fileName);

                    // If the user's option isn't in the list, the program will continue to ask for input till it is.
                    while (!userData.containsKey(editAppName)) {

                        System.out.print("\nPlease type the name of the site/app you would like to edit OR type 'back' to cancel: ");

                        editAppName = userInput.nextLine();
                        
                        if (editAppName.equalsIgnoreCase("back")) {
                            break;
                        }

                        // If it's not, the program will warn the user and restart the loop.
                        else if (!userData.containsKey(editAppName) || !editAppName.equalsIgnoreCase("back")) {

                            passwordGroup.ViewApps(fileName);

                            System.out.print("\nThat site/app does not exist. Try typing the name exactly as it appears in the list above.\n");

                        }
                    }

                    String editUserName = "|";
                    String editPassword = "|";

                    // These while loops get the new login information from the user to replace it in the file.
                    // They prohibit the use of the special symbols.
                    if (!editAppName.equalsIgnoreCase("back")) {
                        while (editUserName.contains("|") || editUserName.contains("~") || editUserName.contains(",")) {

                            ClearScreen();
                            System.out.print("\nWhat should the username be for " + editAppName + "? " + "(Cannot contain '|', '~', or ','): ");
                            editUserName = userInput.nextLine();

                        }

                        while (editPassword.contains("|") || editPassword.contains("~") || editPassword.contains(",")) {
                            
                            ClearScreen();
                            System.out.print("\nWhat should the password be for " + editAppName + "? " + "(Cannot contain '|', '~', or ','): ");
                            editPassword = userInput.nextLine();
                        }

                        ArrayList<String> tempArray = new ArrayList<>(); // temporary array for holding changes to information
                        
                        tempArray.add(editUserName); // These two lines add the info to the array.
                        tempArray.add(editPassword);
                        
                        // This replaces the old array with the new ones, allowing for replacement of the info.
                        userData.replace(editAppName, userData.get(editAppName), tempArray);

                        // This clears the file and adds the master login info back in.
                        userFile.WriteUserFile(fileName, "", passwordGroup.GetMasterUserName(), passwordGroup.GetMasterPassword(), true); 

                        // This for loop goes through each entry in the HashMap and writes it to the file.
                        for (Map.Entry<String, ArrayList<String>> map : userData.entrySet()) {

                            userFile.WriteUserFile(fileName, map.getKey(), map.getValue().get(0), map.getValue().get(1), false);

                            ClearScreen();
                        }
                    }
                }
            }

            // delete site/app login
            else if (menuChoice.equalsIgnoreCase("D")) {

                ClearScreen();
                String appToDelete = "";

                // determines if the file is empty
                if (file.length() == 0 || userData == null || userData.isEmpty()) {

                    System.out.println("The file is empty; nothing to delete.");

                }

                else{

                    ClearScreen();
                    passwordGroup.ViewApps(fileName);

                    // If the user's input isn't in the HashMap, the program will continue to ask for input till it is.
                    while (!userData.containsKey(appToDelete)) {

                        if (!userData.isEmpty()) {

                            System.out.print("Please type the name of the site/app you would like to delete OR type 'back' to cancel: ");

                            appToDelete = userInput.nextLine();

                            ClearScreen();

                            if (appToDelete.equalsIgnoreCase("back")) {
                                menuChoice = "";
                                break;
                            }

                            else if (!userData.containsKey(appToDelete)) {

                                passwordGroup.ViewApps(fileName);

                                System.out.print("\nThat site/app does not exist. Try typing the name exactly as it appears in the list.\n");
                            }
                        }

                        else {
                            System.out.println("The file is empty; nothing to delete.");
                            break;
                        }
                    }

                    // removes selected login data from the HashMap
                    userData.remove(appToDelete);

                    // updates the file
                    passwordGroup.UpdateUserFile(userData, fileName);

                    ClearScreen();
                }
            }

            else if (menuChoice.equalsIgnoreCase("U")) {
                

                String changeLogin = "";

                while (!changeLogin.equalsIgnoreCase("Y") || !changeLogin.equalsIgnoreCase("N")) {

                    ClearScreen();
                    System.out.print("Are you sure you want to change your master login information? (Y/N): ");

                    changeLogin = userInput.nextLine();

                    if (changeLogin.equalsIgnoreCase("Y")) {

                        ClearScreen();
                        System.out.println("You will now be asked to log in with your current login information.");
                        TimeUnit.SECONDS.sleep(2);

                        ClearScreen();

                        boolean canChangeMasterLogin = passwordGroup.loginAttempt(loginArray);

                        if (canChangeMasterLogin) {

                            ClearScreen();

                            System.out.print("What is your new master login username? ");

                            String newMasterUsername = userInput.nextLine();
                            passwordGroup.SetMasterUserName(newMasterUsername);

                            System.out.print("\nWhat is your new master password? ");
                            
                            String newMasterPassword = userInput.nextLine();
                            passwordGroup.SetMasterPassword(newMasterPassword);

                            passwordGroup.UpdateUserFile(userData, fileName);

                            ClearScreen();
                            System.out.println("Master login information changed.");
                            TimeUnit.SECONDS.sleep(1);

                            ClearScreen();
                            break;
                        }

                        else if (!canChangeMasterLogin) {
                            
                            System.out.println("Login information not recognized. Change attempt canceled.");
                            TimeUnit.SECONDS.sleep(1);

                            ClearScreen();
                            break;
                        }
                    }

                    else if (changeLogin.equalsIgnoreCase("N")) {
                        
                        System.out.println("Change attempt canceled.");
                        TimeUnit.SECONDS.sleep(1);

                        ClearScreen();
                        break;
                    }

                    else {
                        System.out.println("That is not an option.");
                    }
                    
                }


            }

            else if (!menuOptions.contains(menuChoice)) {
                System.out.println("\nThat is not an option\n");
                TimeUnit.SECONDS.sleep(1);
                ClearScreen();
            }

            else{
                userInput.close();
                PasswordKeeper.ClearScreen();
                System.out.println("Thank you for using PasswordKeeper!");
                TimeUnit.SECONDS.sleep(2);
                ClearScreen();
            }
        }
    }
    
    // This method clears the console screen to create a cleaner experience
    // for the user.
    static void ClearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}