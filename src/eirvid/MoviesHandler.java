/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

import static eirvid.EirVid.keyboard;
import static eirvid.EirVid.stmt;
import static eirvid.EirVid.waitInput;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.sql.Date;


/**
 *
 * @author ohter
 */
public class MoviesHandler {

    ResultSet dbMovies;

    public void viewMovies() throws SQLException {

        dbMovies = stmt.executeQuery("SELECT * FROM movies");
        //map - indexing starts at 1
        //1-ID
        //2-Language
        //3-Title
        //4-Overview etc.

        System.out.println("Available Movies");
        while (dbMovies.next()) {
            System.out.println("Movie Id: " + dbMovies.getString(1) + "\n");
            System.out.println("Title: " + dbMovies.getString(3) + "\n");
            System.out.println("Language: " + dbMovies.getString(2) + "\n");
            System.out.println("Rating: " + dbMovies.getString(10) + "\n");
            System.out.println("Price: " + dbMovies.getString(12) + "\n");
            System.out.println("Overview: " + dbMovies.getString(4) + "\n");
            System.out.println("----------------------------------------");
        }
        
        //asking user for the Id of the movie he wants to rent, as soon as he selects
        //a rental object is created with his Id (for the moment I only have name) against the 
        //movie he selected. This object will be stored in the rentals table in database later on.
    }

    public void rentMovie(User currentUser) throws SQLException, ParseException {
        boolean appCompleted;        
        int rentalDuration;
        do {
            Boolean isAvailable=false;
            int movieId;
            do{
            System.out.println("Please enter the Id of the movie you want to rent");            
            movieId = keyboard.nextInt();
            ResultSet rs = stmt.executeQuery("SELECT * FROM movies WHERE id="+movieId+"");            
            if(rs.next()){                
               isAvailable = rs.getBoolean(13);
               if(!isAvailable){
                   System.out.println("Movie not available");
               }
            }else{
                System.out.println("Movie not in Record");
            }            
            }while(!isAvailable);
           
            
            System.out.println("How long do you want to rent for? Enter number of days");
            boolean correctInput = false;
            do {
                rentalDuration = keyboard.nextInt();
                correctInput = true;
            } while (!correctInput);
            
            Rental newRent = new Rental(movieId, currentUser.id, rentalDuration);
            stmt.execute("INSERT INTO Rentals(movieId, userId, rentedAt, returnAt) VALUES ('"+newRent.movieId+"','"+newRent.userId+"', STR_TO_DATE(\""+ newRent.rentedAt +"\", \"%Y-%m-%d\"),'"+newRent.willReturnAt+"')");
            stmt.execute("update movies set isAvailable = false where id = "+newRent.movieId+"; ");
            String userHistory = currentUser.rentalHistory;
            if(userHistory==""){
                userHistory = userHistory.concat(""+newRent.movieId+"");
            }else{
                userHistory = userHistory.concat(","+newRent.movieId+"");
            }
                       
            stmt.execute("update users set history = '"+userHistory+"' Where id = ("+newRent.userId+")");
            
            //only saving in arraylist at the moment, will be stored and retrieved from db later on
            // allRentals.add(new Rental(movieId, CURRENTUSER.userName));
            //displays a success message with the title of the movie
            //System.out.println(Arrays.toString(movies.get(movieId)).split(",")[1] + " was rented out!");
            //after this when we have a db, we will set the isAvailable field of this movie 
            //in movies table to false. for example -> movies.at(movieId).isAvailable.set(false);
            //we need to write a db table first
            System.out.println("Press any key to go back to main menu");
            waitInput();
            appCompleted = true;
            
        } while (!appCompleted);
    }

}
