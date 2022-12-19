package eirvid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class EirVid {

    private static User CURRENTUSER = null;
    static Scanner keyboard = new Scanner(System.in);
    static Statement stmt;
    static Connection conn;
    static MoviesHandler engine = new MoviesHandler();

    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException, ParseException {

        String USER = "root";
        String PASS = "asdf";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/", USER, PASS);
            stmt = conn.createStatement();
            engine.populateMovies();
            System.out.println("Processing successfull");
            System.out.println("----------------------------------\n");
            handleLogin();

        } catch (SQLException e) {
            System.out.println("---FOR DEVELOPMENT PURPOSE ONLY---");
            System.out.println("Incorrect Database Credentials");
            System.out.println("Please make sure your database credentials match the credentials in the main class.");
            System.out.println("----------------------------------");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        }

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
    public static void handleLogin() throws SQLException, IOException, FileNotFoundException, ParseException {

        String input;
        stmt.execute("CREATE TABLE IF NOT EXISTS Users "
                + "(id INT NOT NULL AUTO_INCREMENT UNIQUE,"
                + "userName VARCHAR(512) NOT NULL,"
                + "password VARCHAR(512) NOT NULL UNIQUE,"
                + "history TEXT,"
                + "PRIMARY KEY (id));"
        );
        do {
            System.out.println("""
                               Please select one of the following options
                               1)Login
                               2)Signup
                               3)Exit"""
            );
            loginHandler attempt = new loginHandler();
            while (true) {
                try {
                    input = keyboard.next();
                    switch (Integer.parseInt(input)) {
                        case 1 -> {
                            attempt.login();
                        }
                        case 2 -> {
                            attempt.signUp();
                        }
                        case 3 -> {
                            System.exit(0);
                        }
                        default -> {
                            System.out.println("Invalid selection! Try again");
                            System.out.println("----------------------------------------");
                        }
                    }

                } catch (Exception e) {  
                     System.out.println("Invalid selection! Try again");
                            System.out.println("----------------------------------------");
                    
                }
                break;
            }

        } while (CURRENTUSER == null);

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

        System.out.println("WELCOME TO OUR SHOP - " + _currentUser.userName);
        System.out.println("----------------------------------------");
        System.out.println("PLEASE NOTE MINIMUM RENT DURATION IS 1 DAY");
        System.out.println("----------------------------------------\n");
        int input;
        do {
            System.out.println("""
                               Please select one of the following options
                               1)View Recommended Movies.
                               2)Select a movie to Rent.
                               3)Return a movie.
                               4)Display my current rented movies.
                               5)Logout.
                               6)Exit the shop""");

            //handling inputmismatch exception
            //https://stackoverflow.com/questions/16816250/java-inputmismatchexception
            while (true) {
                try {
                    input = keyboard.nextInt();

                } catch (Exception e) {
                    System.out.println("Please select a valid option");
                    keyboard.nextLine();
                    continue;
                }
                break;
            }

            switch (input) {
                case 1 -> {
                    //top 5 movies which appear most in rentals table in tb will appear here.
                }
                case 2 -> {

                    engine.viewMovies();

                    engine.rentMovie(_currentUser);

                }
                case 3 -> {
                    engine.returnMovie(_currentUser);
                }
                case 4 -> {
                    engine.viewMovies(_currentUser);
                }
                case 5 -> {
                    CURRENTUSER = null;
                    System.out.println("----------------------------------------");
                    System.out.println("Logged out!");
                    System.out.println("----------------------------------------\n");
                    handleLogin();
                }
                case 6 ->
                    System.out.println("See you next time!");
            }

        } while (input != 6);
        System.exit(0);
    }

    public static void waitInput() {
        keyboard.next();
    }

}
