/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

import static eirvid.EirVid.keyboard;
import static eirvid.EirVid.stmt;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ohter
 */
public class loginHandler {
    
    public void signUp() throws SQLException, IOException{
        System.out.println("SIGN UP FORM\n");
                User newUser = new User();
                System.out.print("Please Enter a desired Username: ");
                newUser.userName = keyboard.next();
                System.out.print("\nPlease Enter a desired Password: ");
                newUser.password = keyboard.next();
                ResultSet allUsers = stmt.executeQuery("SELECT * FROM Users");
                while(allUsers.next()){
                    if(allUsers.getString(2)==newUser.userName){
                        System.out.println("User already exists, please login instead");
                    }else{
                    stmt.execute("INSERT INTO Users(userName, password, history) VALUES ('"+newUser.userName+"','"+newUser.password+"','"+newUser.rentalHistory+"')");
                    System.out.println("Signed up successfully!");                   
                    
                }
                }       
    }
    public void login() throws SQLException, IOException{
        System.out.println("LOGIN FORM\n");
                User newUser = new User();
                System.out.print("Please Enter Username: ");
                newUser.userName = keyboard.next();
                System.out.print("\nPlease Enter Password: ");
                newUser.password = keyboard.next();
                ResultSet allUsers = stmt.executeQuery("SELECT * FROM Users WHERE userName='"+newUser.userName+"' AND password='"+newUser.password+"'");
                while(allUsers.next()){
                    if(allUsers!=null){
                        System.out.println("Logged in!");
                        EirVid.openShop(newUser);
                    }else{
                    //stmt.execute("INSERT INTO Users(userName, password, history) VALUES ('"+newUser.userName+"','"+newUser.password+"','"+newUser.rentalHistory+"')");
                    System.out.println("User not found!");
                }
                }        
    }
}
