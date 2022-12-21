/*

 */

package eirvid;
import static eirvid.EirVid.conn;
import static eirvid.EirVid.keyboard;
import static eirvid.EirVid.stmt;
import      eirvid.loginHandler;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Erick student number: 2020324
 */
public class ChangePassword {

    
   public void changePassword(User user) throws SQLException{ 
       ResultSet currentPass = stmt.executeQuery("SELECT password FROM users WHERE id ="+user.id+";");
       currentPass.next();
       Statement extraState = conn.createStatement();
  String pass =currentPass.getString(1); //get old password from user
  System.out.println("Enter old password: ");
  String enteredPass = keyboard.next();
 
      if(pass.equals(enteredPass)) {
        int n = 0;
        while (n!= 1) {
     
        System.out.println("Enter new password");
        String newPass = keyboard.nextLine(); // get new password from user
        newPass = newPass.strip();
        
        
          if (newPass.matches("(?=[A-Za-z0-9@#$%^&+!=]+$)^(?=.[a-z])(?=.[A-Z])(?=.[0-9])(?=.[@#$%^&+!=])(?=.{8,}).*$")) {
            extraState.execute("update users set password = "+newPass+" where id = " +user.id + "; ");
            System.out.println("Changed Successfully");
            // password changed
           n = 1;
        } else {
            // wrong confirmation.. password not changed
           System.out.println("It must contain at least 8 characters, 1 uppercase letter, 1 special character and 1 number");
        }
    }}
    else {
        // tell user to enter the correct old password
       System.out.println("Old password is incorrect");
    }
  
  // show error message that user entered the old password 3 times incorrectly
  // and return false
  
}
    
}
