// imports for various tools
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// This class holds the methods and variables for password information.
class PasswordGroup {

        // member variables
        private String _masterUserName;
        private String _masterPassword;
        private HashMap<String, ArrayList<String>> _userData;

        // basic constructor
        public PasswordGroup() {
        }

        // This method populates _userData
        public void SetUserData(HashMap<String, ArrayList<String>> userData){
            _userData = userData;
        }

        // This method returns _userData for access and use;
        public HashMap<String, ArrayList<String>> GetUserData() {
            return _userData;
        }

        // This method sets the master login username.
        public void SetMasterUserName(String masterUserName) {
            _masterUserName = masterUserName;
        }

        // This method returns the master login username.
        public String GetMasterUserName() {
            return _masterUserName;
        }

        // This method sets the master login password.
        public void SetMasterPassword(String masterPassword) {
            _masterPassword = masterPassword;
        }

        // This method returns the master login password.
        public String GetMasterPassword() {
            return _masterPassword;
        }


        // This method creates and returns an ArrayList of a user's login credentials.
        // The credentials will be written to the top row of a file in a special format.
        public ArrayList<String> CreateCredentialsArray(String loginUserName, String loginPassword){

            ArrayList<String> storedCredentials = new ArrayList<>();
            storedCredentials.add(loginUserName);
            storedCredentials.add(loginPassword);

            return storedCredentials;
        }

        // This method gets compares the user's entered login information to the current master
        // login information. If the two match, it return true. Otherwise, it returns false.
        public boolean loginAttempt(ArrayList<String> loginArray) throws InterruptedException {

            // gets the user-supplied login information for the login attempt.
            Scanner userInput = new Scanner(System.in);
            System.out.print("Please enter your username: ");
            String challengeUserName = userInput.nextLine();

            System.out.print("Please enter your password: ");
            String challengePassword = userInput.nextLine();

            int attempts = 0;

            // The user only gets 3 attemps (The initial one, and the two in this while loop).
            while (attempts < 2){
                
                if (!challengeUserName.equals(loginArray.get(0)) || !challengePassword.equals(loginArray.get(1))){
                    
                    PasswordKeeper.ClearScreen();
                    System.out.println("That username or password is incorrect.");
                    System.out.print("\nPlease enter your username: ");
                    challengeUserName = userInput.nextLine();

                    System.out.print("Please enter your password: ");
                    challengePassword = userInput.nextLine();

                    // This updates the number of attempts.
                    attempts += 1;
                }

                else {

                    // If the user gets the username and password right, the program breaks out of this loop and moves on.
                    break;
                }
                
            }

            // if the password is correct, the method returns true; otherwise, it returns false.
            if (challengeUserName.equals(loginArray.get(0)) && challengePassword.equals(loginArray.get(1))){
                
                PasswordKeeper.ClearScreen();
                return true;
            }

            else {
                return false;
            }
        }

        // This method populates a HashMap and loops through the user's login information from the file, printing it out for viewing.
        void ViewApps(String fileName){

            // creates a new FileHandler object to read through the user's file
            FileHandler userFile = new FileHandler();
            
            // creates an empty HashMap to temporarily store the information
            HashMap<String, ArrayList<String>> userData = new HashMap<>();

            // reads the user's data from the file with the file name that was passed in
            userData = userFile.ReadFile(fileName);

            // sets the user data in the PasswordGroup class.
            SetUserData(userData);

            // Format for printing out the list of site or app names
            System.out.println("  Sites / Apps");
            System.out.println("-----------------");

            // loops through the keys of the HashMap and prints them
            for (String site : userData.keySet()){
                
                System.out.println(" " + site);

            }

            System.out.println("-----------------");
        }
        
        // This method gets the user's requested login information and provides it to a block of code in the main program.
        // The other block of code displays the information.
        ArrayList<String> ViewLoginInfo(HashMap<String, ArrayList<String>> map, String userChoice){

            PasswordKeeper.ClearScreen();
            return map.get(userChoice);

        }

        // This method is passed a HashMap containing login information and the name of the file to update.
        // It creates a new FileHandler object, which contains methods for writing to files.
        public void UpdateUserFile(HashMap<String, ArrayList<String>> userData, String fileName) {

            // new FileHandler object
            FileHandler userFile = new FileHandler();
            
            // overwrites the user's current data and adds the master login data back in
            userFile.WriteUserFile(fileName, "", GetMasterUserName(), GetMasterPassword(), true); 

                    // This for loop goes through each entry in the HashMap and writes it to the file.
                    for (Map.Entry<String, ArrayList<String>> map : userData.entrySet()) {
                        
                        // This is unnecessary, but it makes the WriteUserFile() method call look cleaner.
                        ArrayList<String> values = map.getValue();

                        // This appends the given info to the file instead of overwriting it.
                        userFile.WriteUserFile(fileName, map.getKey(), values.get(0), values.get(1), false);

                    }
                    
                    // updates the userData HashMap within the PasswordGroup class
                    SetUserData(userData);
        }
    }