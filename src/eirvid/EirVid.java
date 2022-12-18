/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package eirvid;

import Interfaces.MoviesMapperInterface;
import Interfaces.MoviesParserInterface;
import Interfaces.MoviesValidatorInterface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diese
 */
public class EirVid {
    
    
    private static User CURRENTUSER = new User("Ali","pass");
    static Scanner keyboard = new Scanner(System.in);
    static List<Rental> allRentals = new ArrayList<>();    
       
    
    public static void main(String[] args) throws FileNotFoundException, IOException {        
       try {
           
           //Workflow ->Movie processor calls for Movies Data Input -> Movies Data Parsed -> Movies data validated -> Movies data mapped -> mapped data returned to main class
            var allMovies = new MovieProcessor().ProcessMovies();            
            //all movies are in here           
            //to display
            allMovies.forEach(movie->{
                System.out.println(movie.title);
                System.out.println(movie.price);
            });
            
            
            //database.ConnectDatabase(CURRENTUSER);
            
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
            
            
            openShop(CURRENTUSER.userName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //for the moment currentuser is a string, this will be a user object later on after login is implemented by group mate
    public static void openShop(String CURRENTUSER) throws FileNotFoundException, IOException{
        int input;
        System.out.println(CURRENTUSER+", Welcome to EirVid - Movie Rentals");
        
        do{
            System.out.println("""
                               Please select one of the following options
                               1)View Recommended Movies
                               2)Rent a movie
                               3)Return a movie
                               4)Display my movies
                               5)Exit the shop""");
          
            input = keyboard.nextInt();
            switch(input){
                case 1 -> {
                    //top 5 movies which appear most in rentals table in tb will appear here.
                }
                case 2 -> {
                    //reading all movies line by line and storing in an array list named movies
                    BufferedReader csv = new BufferedReader(new FileReader("src/Movie_Metadata_Edited_2.csv"));
                    ArrayList<String[]> movies = new ArrayList<>();
                    String line ="";
                    while((line = csv.readLine())!=null){                        
                    movies.add(line.split(",")); 
                    }
                    
                    //displaying all movies with index positions (which will be our movie ids)
                    movies.forEach(movie->{
                        
                        //to split by each movie
                        System.out.println(movies.indexOf(movie)+") "+ Arrays.toString(movie));
                        
                        
                    });
                    
                    //asking user for the Id of the movie he wants to rent, as soon as he selects
                    //a rental object is created with his Id (for the moment I only have name) against the 
                    //movie he selected. This object will be stored in the rentals table in database later on.
                    boolean appCompleted;
                    do{
                        System.out.println("Please enter the Id of the movie you want to rent");
                        int movieId = keyboard.nextInt();
                        
                        //only saving in arraylist at the moment, will be stored and retrieved from db later on
                       allRentals.add(new Rental(movieId, CURRENTUSER));
                       
                       //displays a success message with the title of the movie
                        System.out.println(Arrays.toString(movies.get(movieId)).split(",")[1]+" was rented out!");
                        //after this when we have a db, we will set the isAvailable field of this movie 
                        //in movies table to false. for example -> movies.at(movieId).isAvailable.set(false);
                        //we need to write a db table first
                        System.out.println("Press any key to go back to main menu");
                        waitInput();
                        appCompleted = true;
                    }while(!appCompleted);
                    
	
                    
                }
                case 3 -> {
                    //will add movie return mechanism here
                }
                case 4 -> {
                    
                    //for the moment, gets all rented movies from a list of allRentals,
                    //when db is up, we will get this from db
                    allRentals.forEach(rental->{
                        if(rental.userId.equalsIgnoreCase(CURRENTUSER)){
                            System.out.println(rental);
                            waitInput();
                        }else{
                            System.out.println("You are not renting any movies!");
                        }
                    });
                    
                }
                case 5 -> System.out.println("See you next time!");
            }
            
            
            
        }while(input!=5);
        
        
        
        
        
        
    }
    
    public static void waitInput(){
        keyboard.next();
    }
    
}
