package eirvid;

import static eirvid.EirVid.conn;
import static eirvid.EirVid.dbName;
import static eirvid.EirVid.keyboard;
import static eirvid.EirVid.stmt;
import static eirvid.EirVid.waitInput;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MoviesHandler {
    
    public void populateMovies() throws IllegalAccessException{
        try {
            //Workflow ->Movie processor calls for Movies Data Input -> Movies Data Parsed -> Movies data validated -> Movies data mapped -> mapped data returned to main class
            //This brings all the movies from CSV to DB
            
            var allMovies = new MovieProcessor().ProcessMovies();
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
                System.out.println("Price: " + dbMovie.getString(12) + "\n");
                System.out.println("To be returned by: " + myRents.getString("returnAt") + "\n");
                System.out.println("----------------------------------------");

            }
        }

        //map - indexing starts at 1
        //1-ID
        //2-Language
        //3-Title
        //4-Overview etc.
        //asking user for the Id of the movie he wants to rent, as soon as he selects
        //a rental object is created with his Id (for the moment I only have name) against the 
        //movie he selected. This object will be stored in the rentals table in database later on.
    }

    public void rentMovie(User currentUser) throws SQLException, ParseException {
        boolean appCompleted;
        int rentalDuration;
        do {
            Boolean isAvailable = false;
            int movieId;
            do {
                System.out.println("Please enter the Id of the movie you want to rent");
                movieId = keyboard.nextInt();
                ResultSet rs = stmt.executeQuery("SELECT * FROM movies WHERE id=" + movieId + "");
                if (rs.next()) {
                    isAvailable = rs.getBoolean(13);
                    if (!isAvailable) {
                        System.out.println("Movie not available");
                    }
                } else {
                    System.out.println("Movie not in Record");
                }
            } while (!isAvailable);

            System.out.println("How long do you want to rent for? Enter number of days");
            boolean correctInput = false;
            do {
                rentalDuration = keyboard.nextInt();
                correctInput = true;
            } while (!correctInput);

            Rental newRent = new Rental(movieId, currentUser.id, rentalDuration);
            stmt.execute("INSERT INTO Rentals(movieId, userId, rentedAt, returnAt) VALUES ('" + newRent.movieId + "','" + newRent.userId + "', STR_TO_DATE(\"" + newRent.rentedAt + "\", \"%Y-%m-%d\"),'" + newRent.willReturnAt + "')");
            stmt.execute("update movies set isAvailable = false where id = " + newRent.movieId + "; ");
            String userHistory = currentUser.rentalHistory;
            if (userHistory == "") {
                userHistory = userHistory.concat("" + newRent.movieId + "");
            } else {
                userHistory = userHistory.concat("," + newRent.movieId + "");
            }

            stmt.execute("update users set history = '" + userHistory + "' Where id = (" + newRent.userId + ")");

            //only saving in arraylist at the moment, will be stored and retrieved from db later on
            // allRentals.add(new Rental(movieId, CURRENTUSER.userName));
            //displays a success message with the title of the movie
            //System.out.println(Arrays.toString(movies.get(movieId)).split(",")[1] + " was rented out!");
            //after this when we have a db, we will set the isAvailable field of this movie 
            //in movies table to false. for example -> movies.at(movieId).isAvailable.set(false);
            //we need to write a db table first
            System.out.println("Successfully Rented!\nPress any key to go back to main menu");
            waitInput();
            appCompleted = true;

        } while (!appCompleted);
    }

}
