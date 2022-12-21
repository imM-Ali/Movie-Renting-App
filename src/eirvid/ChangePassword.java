/*

 */
package eirvid;

import static eirvid.EirVid.conn;
import static eirvid.EirVid.keyboard;
import static eirvid.EirVid.stmt;
import static eirvid.loginHandler.validatePass;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author Erick student number: 2020324
 */
public class ChangePassword {

    public void changePassword(User user) throws SQLException {
        ResultSet currentPass = stmt.executeQuery("SELECT password FROM users WHERE id =" + user.id + ";");
        currentPass.next();
        Statement extraState = conn.createStatement();
        String pass = currentPass.getString(1); //get old password from user  
        System.out.println("Enter old password: ");
        String enteredPass = keyboard.next();
        String newPass;
        if (pass.equals(enteredPass)) {
            try {
                do {
                    System.out.print("Please Enter new Password: ");
                    newPass = keyboard.next();
                } while (validatePass(newPass) == false);
                extraState.execute("update users set password = '" + newPass + "' where id = " + user.id + "; ");
                System.out.println("Changed Successfully");
            } catch (SQLException ex) {
                System.out.println("Database information mismatch");
            }
        }else {
                    // wrong confirmation.. password not changed
                    System.out.println("Old password is incorrect");
               }

       

        // show error message that user entered the old password 3 times incorrectly
        // and return false
    
    }
}

