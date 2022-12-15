/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package eirvid;

import Interfaces.MoviesMapperInterface;
import Interfaces.MoviesParserInterface;
import Interfaces.MoviesValidatorInterface;
import java.io.IOException;

/**
 *
 * @author diese
 */
public class EirVid {
    
    private static String CURRENTUSER = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {        
        
        DBConnection database = new DBConnection();
        MoviesDataInput movieDataInput = new MoviesDataInput();
        MoviesValidatorInterface movieValidator = new MovieValidator();
        MoviesMapperInterface movieMapper = new MovieMapper();
        MoviesParserInterface movieParser = new MovieParser(movieValidator, movieMapper);
        
        MovieProcessor mp = new MovieProcessor(movieDataInput, movieParser, database);
        mp.ProcessMovies();
        
        database.ConnectDatabase(CURRENTUSER);
        
        //1)moviesDataInput
        //2)moviesParse
        
        //USE SQLSERVER WITH THESE CREDS - THEN CREATE TABLES AS NEEDED -- DONT REMOVE PLEASE//
        //String connectionUrl = "jdbc:mysql://localhost/RTPlayer";
        //String connectionUser = "Rental";
        //String connectionPassword = "Rental";
        
        //---------------NEED SEPERATE CLASSES FOR THESE----------------------//
        
        
        
        //3) move all the movie projects to database table named 'movies' with appropriate properties/labels,
        // this table will have all properties from csv plus additional property named 'isAvailable'
        //isAvailable will be a boolean and stay true unless the movie is rented out.
        
        
        
        
        //4) login/signup =For signup, If it doesnt exist, Create a table in database named 'users' and add users there.
        //for login, when user has entered credentials, check if it exists in database and set CURRENT_USER to this user.
        //The users table will have the following attributes:
        //1) Id
        //2) userName
        //3) password
        //4) rentalHistory - this will be an array with all the Ids of movies this user has rented. 
        
        
        
        
       // MUHAMMAD ALI SHAHZAIB - 2020463         
        //5) renting process - When successfully logged in, will display menu:
        
        // - View recommended movies (will display most rented movies, we will get this from rentals table)
        // - Rent a movie (will display all movies so user can select - rented movie will save in rentals table)
        // - Return a movie (user can return a movie if he wants to - will ask user for rental ID and his details)
        // - Exit
        
        //Requires a table in database named 'rentals'. All rented movies will be stored in that with unique rental ID.
        //rentals rable will have the following attributes:
        //1) Id - this will be rental ID - dont confuse this with movie id
        //2) movieID - the id of movie that has been rented
        //3) userId - the Id of user who has rented this movie
        //4) rentedAt - the time this movie was rented at
    
        
        
        
        
        
    }
}
