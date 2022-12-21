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

/*
Author : Muhammad Ali Shahzaib 2020463
 */
public class EirVid {
    
    //setting these as static so they can be used across the classes in the package.
    private static User CURRENTUSER = null;
    static Scanner keyboard = new Scanner(System.in);
    static Statement stmt;
    static Connection conn;
    static MoviesHandler engine = new MoviesHandler();

    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException, ParseException {

        //Please set this according to your local DB credentials
        String dbUSER = "root";
        String dbPASS = "asdf";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/", dbUSER, dbPASS);
            stmt = conn.createStatement();
            //reads, validates movies from csv and then pushes to DB
            engine.populateMovies();
            System.out.println("Processing successfull");
            System.out.println("----------------------------------\n");
            //sends to the login/signup methods.
            handleLogin();

        } catch (SQLException e) {
            System.out.println("---FOR DEVELOPMENT PURPOSE ONLY---");
            System.out.println("Incorrect Database Credentials");
            System.out.println("Please make sure your database credentials match the credentials in the main class.");
            System.out.println("----------------------------------");

        } catch (ClassNotFoundException | IllegalAccessException ex) {
            Logger.getLogger(EirVid.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

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
            //creating loginHandler object
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
                            //https://www.scaler.com/topics/java/exit-in-java/
                            System.exit(0);
                        }
                        default -> {
                            System.out.println("Invalid selection! Try again");
                            System.out.println("----------------------------------------");
                        }
                    }

                } catch (IOException | NumberFormatException | SQLException | ParseException e) {
                    System.out.println("Invalid selection! Try again");
                    System.out.println("----------------------------------------");

                }
                break;
            }

        } while (CURRENTUSER == null);

    }
    //takes the currentuser object so that it can be referenced all across the program wherever needed such as user id and name
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
                               5)Display history
                               6)Change password
                               7)Logout.
                               8)Exit the shop""");

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

                    engine.viewTopMovies();

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
                    // view history by passing user to the method.
                    userHistory history = new userHistory();
                    history.view(_currentUser);
                }
                 case 6 -> {
                    ChangePassword changer = new ChangePassword();
                    changer.changePassword(_currentUser);
                }
                case 7 -> {
                    CURRENTUSER = null;
                    System.out.println("----------------------------------------");
                    System.out.println("Logged out!");
                    System.out.println("----------------------------------------\n");
                    handleLogin();
                }
                case 8 ->
                    System.out.println("See you next time!");
            }

        } while (input != 6);
        System.exit(0);
    }

}
