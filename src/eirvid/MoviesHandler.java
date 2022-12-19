package eirvid;

import static eirvid.EirVid.conn;
import static eirvid.EirVid.keyboard;
import static eirvid.EirVid.stmt;
import static eirvid.EirVid.waitInput;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MoviesHandler {

    public void populateMovies() throws IllegalAccessException {
        try {
            //Workflow ->Movie processor calls for Movies Data Input -> Movies Data Parsed -> Movies data validated -> Movies data mapped -> mapped data returned to main class
            //This brings all the movies from CSV to DB
            
            String dbName = "RTPlayer";
            var allMovies = new MovieProcessor().ProcessMovies();
            System.out.println("Creating DB with Movies Table....");
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
            System.out.println("Pushing Movies To DB....");
            for (Movie movie : allMovies) {
                stmt.execute(
                        "INSERT IGNORE INTO movies(org_language,org_title,overview,popularity,release_date,runtime,tagline,title,vote_avg,vote_count,price,isAvailable) VALUES "
                        + "('" + movie.orgLang + "',"
                        + "'" + movie.title.strip() + "',"
                        + "'" + movie.overview.strip() + "',"
                        + "" + movie.popularity + ","
                        + "STR_TO_DATE(\"" + movie.releaseDate.trim() + "\", \"%d/%m/%Y\"),"
                        + "" + movie.runtime + ","
                        + "'" + movie.tagline + "',"
                        + "'" + movie.title + "',"
                        + "" + movie.voteAvg + ","
                        + "" + movie.voteCount + ","
                        + "" + movie.price + ","
                        + "" + movie.isAvailable + ")"
                );
            }

        } catch (IOException ex) {
            Logger.getLogger(MoviesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MoviesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MoviesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MoviesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void viewMovies() throws SQLException {

        ResultSet dbMovies = stmt.executeQuery("SELECT * FROM movies");
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

    public void viewMovies(User user) throws SQLException {

        //Using extra statement because only 1 resultset can be oopen from a single statement at one time
        //https://stackoverflow.com/questions/16511863/how-can-i-keep-the-resultset-open-or-make-calls-to-different-resultsets
        ResultSet myRents = stmt.executeQuery("SELECT * FROM rentals Where userId = " + user.id + "");
        Statement tempState = conn.createStatement();

        System.out.println("----------------------------------------");
        System.out.println("Your Current Rentals\n");

        while (myRents.next()) {
            ResultSet dbMovie = tempState.executeQuery("SELECT * FROM movies WHERE id=" + myRents.getString("movieId") + "");
            while (dbMovie.next()) {

                System.out.println("Movie Id: " + dbMovie.getString(1) + "\n");
                System.out.println("Title: " + dbMovie.getString(3) + "\n");
                System.out.println("Rating: " + dbMovie.getString(10) + "\n");
                System.out.println("Price (per day): " + dbMovie.getString(12) + "\n");
                System.out.println("To be returned by: " + myRents.getString("returnAt") + "\n");
                System.out.println("----------------------------------------");

            }
        }

    }

    public void rentMovie(User currentUser) throws SQLException, ParseException {
        boolean appCompleted;
        int numOfDays;
        do {
            Boolean isAvailable = false;
            int movieId;

            //asking user for the Id of the movie he wants to rent, as soon as he selects
            //a rental object is created with his Id against the movie he selected. This object will be stored in the rentals table in database.
            do {
                System.out.println("Please enter the Id of the movie you want to rent");
                //handling inputmismatch exception
                while (true) {
                    try {
                        movieId = keyboard.nextInt();

                    } catch (Exception e) {
                        System.out.println("Please select a valid option");
                        keyboard.nextLine();
                        continue;
                    }
                    break;
                }
                ResultSet rs = stmt.executeQuery("SELECT * FROM movies WHERE id=" + movieId + "");
                if (rs.next()) {
                    //if movie is visible in our shop, we check if it is already rented out.
                    //this makes sure that a user cannot select a movie Id which is not visible
                    isAvailable = rs.getBoolean(13);
                    if (!isAvailable) {
                        System.out.println("Movie not available");
                    }
                } else {
                    System.out.println("Movie not in Record");
                }
            } while (!isAvailable);

            System.out.println("How long do you want to rent for? Enter number of days");

            //handling inputmismatch exception
            while (true) {
                try {
                    numOfDays = keyboard.nextInt();

                } catch (Exception e) {
                    System.out.println("Please Enter a Valid Number of Days");
                    keyboard.nextLine();
                    continue;
                }
                break;
            }

            Rental newRent = new Rental(movieId, currentUser.id, numOfDays);
            //update rentals table
            stmt.execute("INSERT INTO Rentals(movieId, userId, rentedAt, returnAt) VALUES ('" + newRent.movieId + "','" + newRent.userId + "', STR_TO_DATE(\"" + newRent.rentedAt + "\", \"%Y-%m-%d\"),'" + newRent.willReturnAt + "')");
            //set the isAvailable field of this movie to not available
            stmt.execute("update movies set isAvailable = false where id = " + newRent.movieId + "; ");
            String userHistory = currentUser.rentalHistory;
            if (userHistory == "") {
                userHistory = userHistory.concat("" + newRent.movieId + "");
            } else {
                userHistory = userHistory.concat("," + newRent.movieId + "");
            }
            //update users rental history
            stmt.execute("update users set history = '" + userHistory + "' Where id = (" + newRent.userId + ")");

            ResultSet price = stmt.executeQuery("SELECT price FROM rtplayer.movies where id = " + newRent.movieId + "");
            price.next();
            //displays a success message with the ID and Price of the movie  
            System.out.println("\n----------------------------------------");
            System.out.println("Successfully Rented movie ID: " + newRent.movieId + "!");
            System.out.println("Please pay " + price.getDouble("price") * numOfDays + " at the counter, thank you!\n");
            System.out.println("Press any key to go back to main menu");

            appCompleted = true;

        } while (!appCompleted);
    }

}
