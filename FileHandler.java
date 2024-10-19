import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileHandler {


  // This method uses the file name that it is passed and creates a new file.
  // If the file already exists, it prints an "already exists" message to the user.
    void CreateUserFile(String fileToCreate){
      try {

        // create a new instance of File
        File createdFile = new File(fileToCreate);

        // returns true if the file does not already exist; prints a message telling
        // the user that the file was created.
        if (createdFile.createNewFile()) {
          System.out.println("File created.\n");
          

        } else {
          System.out.println("That file already exists.\n");
          
        }

        // prints an error message if the file couldn't be created.
      } catch (IOException exception) {
        System.out.println("An error occurred.");
        exception.getStackTrace();
      }
    }

    // This method writes the user's entered login information to the specified file.
    // The boolean "forLogin" parameter looks for true. If it is passed true, the
    // method will write the username and password to the file in a special format.
    void WriteUserFile(String fileName, String site, String userName, String password, boolean forLogin){
      try{

        // If the information provided is not for master login information for the
        // file, it will write it in a normal format.
        if (!forLogin){
          FileWriter userFile = new FileWriter(fileName, true);
          userFile.append("," + "~" + site + "," + userName + "," + password.trim() + "\n");
          userFile.close();
        }

        // If the information is for the master login, it is in a special format.
        else{
          FileWriter userFile = new FileWriter(fileName, false);
          userFile.write("|" + userName + ","  + "|" + password + "\r");
          userFile.close();
        }
        
        // error catching
      } catch (IOException exception) {
          System.out.println("Couldn't save file; please try again.");
          exception.getStackTrace();
      }
    }

      // This method returns a HashMap to update the one storing a user's
      // usernames and passwords for each site. It reads from a specified
      // file and adds the information to the HashMap in a pre-determined
      // format.
    HashMap<String, ArrayList<String>> ReadFile(String passwordFileName){

      // This HashMap stores the site/app name as the string. The ArrayList
      // is made up of the username and password. The HashMap uses an ArrayList
      // that is created just after the HashMap is created.
      HashMap<String, ArrayList<String>> passwordData = new HashMap<>();
      ArrayList<String> userCredentials = new ArrayList<>();
      String siteName = "";
      int i = 0;
      

        // This creates a new File instance that allows the Scanner to
        // read through the contents of the user-provided file.
        try {

          File file = new File(passwordFileName);

          Scanner fileReader = new Scanner(file);
          
          // The file reader uses commas as a delimiter to determine where
          // the next word starts.
          fileReader.useDelimiter(",");

          // This while loop is used to determine if there are more characters
          // to read.
          while (fileReader.hasNext()) {

            // This stores the current word.
            String passwordInfo = fileReader.next();
            
            // The "|" symbol is only used for master login information, and
            // it uses a different format, so this check is making sure that
            // that is not included in the HashMap.
            if (!passwordInfo.startsWith("|")) {
                
              // The site/app name is stored with the "~" symbol to show that
              // it is separate.
              if (passwordInfo.startsWith("~")){
                  
                siteName = passwordInfo;
                
                // This replaces that special character with whitespace, and
                // the name is trimmed on the next line to get rid of the whitespace.
                siteName = siteName.replace("~", " ");
                siteName = siteName.strip();

              }
              
              else {
                
                // The max amount of info for the ArrayList should be 2 entries.
                if (userCredentials.size() < 2){
  
                  userCredentials.add(passwordInfo);

                  if (userCredentials.size() == 2) {

                    passwordData.put(siteName, userCredentials);

                    if (fileReader.hasNext()) {
                      userCredentials = new ArrayList<>();
                    }
                    
                    else {
                      break;
                    }  
                  }
                }
                
                // Once the two entries are added, the site/app name is added to
                // the HashMap as the key, and the username and password are added
                // as its values.
              
              }
            } 
          }
            fileReader.close();
            return passwordData;
            // error catching
        } catch (FileNotFoundException exception) {
          System.out.println("An error occurred.");
          exception.getStackTrace();
      }
      
      // The method returns a HashMap.
      return passwordData;
    }

    // This method returns an ArrayList containing a user's master
    // login information for the file. Most other parts of the
    // method are the same as the previous method.
    ArrayList<String> ReadUserLogin(String passwordFileName){

      // This array contains the user's master login information.
      ArrayList<String> userLogin = new ArrayList<String>();

      // This array is empty and is only used in the error
      // catch block so that it has an ArrayList for the return statement.
      ArrayList<String> errorArray = new ArrayList<String>();

      try {
        File file = new File(passwordFileName);
        Scanner fileReader = new Scanner(file);

        fileReader.useDelimiter(",");

        while (fileReader.hasNext()) {
          String userLoginInfo = fileReader.next();

          // This checks for the "|" character, which denotes a master login element.
          if (userLoginInfo.startsWith("|")) {

            // The "|" symbol is replaced with whitespace, which is them trimmed away.
            userLoginInfo = userLoginInfo.replace("|", " ");
            userLoginInfo = userLoginInfo.trim();

            // The master username and password are added to the userLogin ArrayList.
            userLogin.add(userLoginInfo);
          }

          // returns the userLogin Arraylist
          else{
            return userLogin;
          }
        }

        fileReader.close();

        // Error catching
      } catch (FileNotFoundException exception) {
          System.out.println("An error occurred.");
          exception.getStackTrace();
          return errorArray;
        }
      return userLogin;
    }
}
