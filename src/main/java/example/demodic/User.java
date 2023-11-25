package example.demodic;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class User {
    private String userName;
    private String password;
    private String filePath;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFilePath(String fileName) {
        this.filePath = "users\\userData\\" + fileName + ".txt";
    }

    @Override
    public String toString() {
        StringBuilder userInfo = new StringBuilder();
        userInfo.append("@userName:").append(userName).append("\n");
        userInfo.append("@userPassword:").append(password).append("\n");
        userInfo.append("@end").append("\n");

        return userInfo.toString();
    }

    public void createNewFileUSer() {
        File file = new File(this.filePath); //initialize File object and passing path as argument
        boolean result;
        try
        {
            result = file.createNewFile();  //creates a new file
            if(result)      // test if successfully created a new file
            {
                System.out.println("file created "+file.getCanonicalPath()); //returns the path string
            }
            else
            {
                System.out.println("File already exist at location: "+file.getCanonicalPath());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();    //prints exception if any
        }
    }

    public void deleteDataFile() {
        File myObj = new File(this.filePath);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }
}
