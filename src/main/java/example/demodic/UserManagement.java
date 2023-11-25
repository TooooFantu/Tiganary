package example.demodic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class UserManagement {
    private static String currentUser;
    private static List<String> listUserName = new ArrayList<>();
    private static Hashtable<String, User> userManagement = new Hashtable<>();

    public static void loadFromFile() throws IOException {
        //
        listUserName.clear();
        userManagement.clear();
        //
        File file = new File("users\\userInfo\\userAccount.txt");
        Scanner sc = new Scanner(file);
        String userNameTarget = new String();
        String userPassword = new String();

        while (sc.hasNext()) {
            String string = sc.nextLine();
            if (string.startsWith("@end")) {
                listUserName.add(userNameTarget);
                User user = new User();
                user.setUserName(userNameTarget);
                user.setPassword(userPassword);
                user.setFilePath(userNameTarget);
                userManagement.put(userNameTarget, user);
            } else if (string.startsWith("@userName:")) {
                userNameTarget = string.substring(10);
            } else if (string.startsWith("@userPassword:")) {
                userPassword = string.substring(14);
            }
        }
    }

    public static void exportToFile() throws IOException {
        File file = new File("users\\userInfo\\userAccount.txt");
        FileWriter fileWriter = new FileWriter(file);
        for (int i = 0; i < listUserName.size(); i++) {
            String curUser = listUserName.get(i);
            fileWriter.write(userManagement.get(curUser).toString());
        }
        fileWriter.close();
    }

    public static boolean addNewUser(String userName, String userPassword) throws IOException {
        if (userName.isEmpty() || userName == null || userPassword == null || userPassword.isEmpty()) {
            return false;
        }
        listUserName.add(userName);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(userPassword);
        user.setFilePath(userName);
        user.createNewFileUSer();
        Dictionary.loadToNewUserData(user.getFilePath());
        userManagement.put(userName, user);
        exportToFile();
        return true;
    }

    public static boolean removeUser() throws IOException {
        User curUser = getCurrentUser();
        curUser.deleteDataFile();
        listUserName.remove(currentUser);
        userManagement.remove(currentUser);
        exportToFile();
        currentUser = new String();
        return true;
    }

    public static boolean contain(String userName) {
        return listUserName.contains(userName);
    }

    public static User getUser(String userNameTarget) {
        if (userNameTarget == null || userNameTarget.isEmpty() || !contain(userNameTarget)) {
            System.out.println("Khong ton tai tai khoan");
            return new User();
        }
        return userManagement.get(userNameTarget);
    }

    public static void setCurrentUser(String curUser) {
        if (!contain(curUser)) {
            System.out.println("Invalid CurUser");
            return;
        }
        currentUser = curUser;
    }

    public static User getCurrentUser() {
        return userManagement.get(currentUser);
    }

    public static void changePassword(String userNameTarget, String newPassword) {
        if (userNameTarget == null || userNameTarget.isEmpty() || newPassword.isEmpty() || newPassword == null) {
            System.out.println("Fail to change password");
            return;
        }
        userManagement.get(userNameTarget).setPassword(newPassword);
    }
}
