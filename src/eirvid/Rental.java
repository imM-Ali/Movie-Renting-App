/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ohter
 */
public class Rental {
    
    int id; //this will be auto incremented in database
    int movieId;
    String userId;    
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
    Date rentedAt = new Date(); 
    
    public Rental(int _movieId, String _userId){
        
        this.movieId = _movieId;
        this.userId = _userId;
        //we have not added Id as it will be a primary key which is auto incremented by db.
        //we have not added rentedAt as it will automatically be added when an object is created.
        
        
    }
    
     @Override
    public String toString() {
        return this.movieId + " has been rented by " + this.userId + " at: "+this.rentedAt;
    }
    
}
