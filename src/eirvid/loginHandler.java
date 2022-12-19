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

    public void signUp() throws SQLException, IOException {
        System.out.println("SIGN UP FORM\n");
        User newUser = new User();
        System.out.print("Please Enter a desired Username: ");
        newUser.userName = keyboard.next();
        System.out.print("\nPlease Enter a desired Password: ");
        newUser.password = keyboard.next();
        allUsers = stmt.executeQuery("SELECT * FROM Users");
        int done = 0;
        do {
            if (allUsers.next()) {
                if (allUsers.getString(2).equalsIgnoreCase(newUser.userName)) {
                    System.out.println("----------------------------------------");
                    System.out.println("User already exists, please login instead");
                    System.out.println("----------------------------------------\n");
                    done =1;
                }
            } else {
                stmt.execute("INSERT INTO Users(userName, password, history) VALUES ('" + newUser.userName + "','" + newUser.password + "','" + newUser.rentalHistory + "')");
                System.out.println("----------------------------------------\n");
                System.out.println("Signed up successfully!");
                System.out.println("----------------------------------------\n");
                done=1;
            }
        } while (done != 1);

    }

    public void login() throws SQLException, IOException, FileNotFoundException, ParseException {
        System.out.println("----------------------------------------");
        System.out.println("LOGIN FORM\n");
        User newUser = new User();
        System.out.print("Please Enter Username: ");
        newUser.userName = keyboard.next();
        System.out.print("Please Enter Password: ");
        newUser.password = keyboard.next();
        allUsers = stmt.executeQuery("SELECT * FROM Users WHERE userName='" + newUser.userName + "' AND password='" + newUser.password + "'");
        int done = 0;
        do{
            if(allUsers.next()){
                System.out.println("----------------------------------------");
                System.out.println("Logged in!");
                System.out.println("----------------------------------------\n");
                newUser.id = allUsers.getInt(1);
                newUser.rentalHistory = allUsers.getString(4);
                EirVid.openShop(newUser);
                done=1;
            }else{
                System.out.println("User not found! Try again");
                done=1;
            }
            
        }while(done!=1);
       
    }
}
