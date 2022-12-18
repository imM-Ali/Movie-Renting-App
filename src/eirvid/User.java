/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

import java.util.List;



/**
 *
 * @author 35389
 */
public class User {
     int id; //this will be auto incremented in database
     String userName;
     String password; 
     List<Movie> rentalHistory;
     
     public User(String _userName, String _password){
        
        
        this.password = _password;
        this.userName = _userName;
        
        
    }
    
}
