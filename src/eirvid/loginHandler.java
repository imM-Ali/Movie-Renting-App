package eirvid;

import static eirvid.EirVid.keyboard;
import static eirvid.EirVid.stmt;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

public class loginHandler {

    ResultSet allUsers;

    /*
    Author : Pedro
     */
    public void signUp() throws SQLException, IOException {
        System.out.println("SIGN UP FORM\n");
        String userName;
        String password;

        //validating email
        do {
            System.out.print("Please Enter a valid email for your Username: ");
            userName = keyboard.next();
        } while (validateEmail(userName) == false);

        //validating password
        do {
            System.out.print("\nPlease Enter a desired Password: ");
            System.out.println("It must contain at least 8 characters, 1 uppercase letter, 1 special character and 1 number");
            password = keyboard.next();
        } while (validatePass(password) == false);

        User newUser = new User(userName, password);
        allUsers = stmt.executeQuery("SELECT * FROM Users");
        int done = 0;
        do {
            if (allUsers.next()) {
                if (allUsers.getString(2).equalsIgnoreCase(userName)) {
                    System.out.println("----------------------------------------");
                    System.out.println("User already exists, please login instead");
                    System.out.println("----------------------------------------\n");
                    done = 1;
                }
            } else {
                stmt.execute("INSERT INTO Users(userName, password, history) VALUES ('" + userName + "','" + password + "','" + newUser.rentalHistory + "')");
                System.out.println("----------------------------------------\n");
                System.out.println("Signed up successfully!");
                System.out.println("----------------------------------------\n");
                done = 1;
            }
        } while (done != 1);

    }

    public void login() throws SQLException, IOException, FileNotFoundException, ParseException {
        System.out.println("----------------------------------------");
        System.out.println("LOGIN FORM\n");
        String userName;
        String password;
        System.out.print("Please Enter Email/Username: ");
        userName = keyboard.next();
        System.out.print("Please Enter Password: ");
        password = keyboard.next();

        User newUser = new User(userName, password);
        allUsers = stmt.executeQuery("SELECT * FROM Users WHERE userName='" + userName + "' AND password='" + password + "'");
        int done = 0;
        do {
            if (allUsers.next()) {
                System.out.println("----------------------------------------");
                System.out.println("Logged in!");
                System.out.println("----------------------------------------\n");
                newUser.id = allUsers.getInt(1);
                newUser.rentalHistory = allUsers.getString(4);
                EirVid.openShop(newUser);
                done = 1;
            } else {
                System.out.println("User not found! Try again");
                done = 1;
            }

        } while (done != 1);

    }

    public boolean validateEmail(String email) {
        //https://regexr.com/3e48o
        boolean result = false;
        result = email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        return result;
    }

    public boolean validatePass(String password) {
        //https://www.regextester.com/110035
        boolean result = false;
        result = password.matches("(?=[A-Za-z0-9@#$%^&+!=]+$)^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+!=])(?=.{8,}).*$");
        return result;
    }
}
