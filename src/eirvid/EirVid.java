/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package eirvid;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

/**
 *
 * @author diese
 */
public class EirVid {

    private static User CURRENTUSER = null;
    static Scanner keyboard = new Scanner(System.in);
    static List<Rental> allRentals = new ArrayList<>();
    static Statement stmt;
    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException, ParseException {
        try {

            //Workflow ->Movie processor calls for Movies Data Input -> Movies Data Parsed -> Movies data validated -> Movies data mapped -> mapped data returned to main class
            var allMovies = new MovieProcessor().ProcessMovies();
            //all movies are in here           
            //to display
            // allMovies.forEach(movie->{
            //  System.out.println(movie.title);
            // System.out.println(movie.price);
            // });

            String dbName = "RTPlayer";
            String DB_URL = "jdbc:mysql://localhost/" + dbName;
            String USER = "root";
            String PASS = "asdf";
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/", USER, PASS);
                stmt = conn.createStatement();
                stmt.execute("CREATE SCHEMA IF NOT EXISTS " + dbName + ";");
                stmt.execute("USE " + dbName + ";");
                stmt.execute("CREATE TABLE IF NOT EXISTS Movies "
                        + "(id INT NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "org_language VARCHAR(512) NOT NULL,"
                        + "org_title VARCHAR(512) NOT NULL UNIQUE,"
                        + "overview TEXT NOT NULL,"
                        + "popularity DOUBLE NOT NULL,"
                        + "release_date DATE NOT NULL,"
                        + "runtime INT NOT NULL,"
                        + "tagline varchar(512) NOT NULL,"
                        + "title VARCHAR(512) NOT NULL,"
                        + "vote_avg DOUBLE NOT NULL,"
                        + "vote_count INT NOT NULL,"
                        + "price DOUBLE NOT NULL,"
                        + "isAvailable BOOLEAN NOT NULL,"                        
                        + "PRIMARY KEY (id));"
                );
                for (Movie movie : allMovies) {                    
                    stmt.execute(
                            "INSERT IGNORE INTO movies(org_language,org_title,overview,popularity,release_date,runtime,tagline,title,vote_avg,vote_count,price,isAvailable) VALUES "
                            + "('" + movie.orgLang + "',"
                            + "'"+ movie.title.strip() +"',"
                            + "'" + movie.overview.strip() + "',"
                            + "" + movie.popularity + ","
                            + "STR_TO_DATE(\""+ movie.releaseDate.trim() +"\", \"%d/%m/%Y\"),"
                            + "" + movie.runtime + ","
                            + "'" + movie.tagline + "',"
                            + "'" + movie.title + "',"
                            + "" + movie.voteAvg + ","
                            + "" + movie.voteCount + ","
                            + "" + movie.price + ","
                            + "" + movie.isAvailable + ")"
                            
                              );
                }
            } catch (SQLException e) {
               
                e.printStackTrace();
            }

           
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
            
            handleLogin();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void handleLogin() throws SQLException, IOException, FileNotFoundException, ParseException{
        int input;
        //System.out.println(CURRENTUSER.userName + ", Welcome to EirVid - Movie Rentals");
        stmt.execute("CREATE TABLE IF NOT EXISTS Users "
                        + "(id INT NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "userName VARCHAR(512) NOT NULL,"
                        + "password VARCHAR(512) NOT NULL UNIQUE,"
                        + "history TEXT,"         
                        + "PRIMARY KEY (id));"
                );
        do{
        System.out.println("""
                               Please select one of the following options
                               1)Login
                               2)Signup """
                               );
        input = keyboard.nextInt();
        loginHandler attempt = new loginHandler();
        switch(input){
            case 1->{                
               attempt.login();
            }
            case 2 ->{                
                attempt.signUp();  
            }
        }
        }while(CURRENTUSER==null);
           
    }
    

    public static void openShop(User _currentUser) throws FileNotFoundException, IOException, SQLException, ParseException {
        stmt.execute("CREATE TABLE IF NOT EXISTS Rentals "
                        + "(id INT NOT NULL AUTO_INCREMENT UNIQUE,"
                        + "movieId INT NOT NULL,"
                        + "userId INT NOT NULL,"
                        + "rentedAt DATE," 
                        + "returnAt DATE," 
                        + "PRIMARY KEY (id));"
                );
        
        System.out.println("\nWELCOME TO OUR SHOP - "+_currentUser.userName);
        System.out.println("\nPLEASE NOTE MINIMUM RENT DURATION IS 1 DAY");
        int input;
        MoviesHandler attempt = new MoviesHandler();
        do {
            System.out.println("""
                               Please select one of the following options
                               1)View Recommended Movies
                               2)Rent a movie
                               3)Return a movie
                               4)Display my movies
                               5)Exit the shop""");

            input = keyboard.nextInt();
            switch (input) {
                case 1 -> {
                    //top 5 movies which appear most in rentals table in tb will appear here.
                }
                case 2 -> {
                    
                 attempt.viewMovies();
                    
                 attempt.rentMovie(_currentUser);
                    
                  
                  
                  
                  
                  
                  
                }
                case 3 -> {
                    //will add movie return mechanism here
                }
                case 4 -> {

                    //for the moment, gets all rented movies from a list of allRentals,
                    //when db is up, we will get this from db
                    

                }
                case 5 ->
                    System.out.println("See you next time!");
            }

        } while (input != 5);

    }

    public static void waitInput() {
        keyboard.next();
    }

}
