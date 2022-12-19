package eirvid;

import static eirvid.EirVid.conn;
import static eirvid.EirVid.keyboard;
import static eirvid.EirVid.stmt;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


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
                        //Using ignore keyword so that if SQL returns with a duplicate error it will ignore that record and go to the next one
                        //https://www.tutorialspoint.com/mysql/mysql-handling-duplicates.htm#:~:text=Use%20the%20INSERT%20IGNORE%20command,silently%20without%20generating%20an%20error.
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
            System.out.println("Cannot process movies, please make sure CSV is in src folder and named Movie_Metadata_Edited_2");
        } catch (ClassNotFoundException ex) {
            System.out.println("Fatal Error, Class files not found, please reclone the repository");
        } catch (InstantiationException ex) {
            System.out.println("Cannot process movies, please make sure CSV is in CCT Format");
        } catch (SQLException ex) {
            System.out.println("CSV Data incorrect, please use a CSV with movies in CCT Format");
        }
    }

    public void viewMovies() throws SQLException {

        ResultSet dbMovies = stmt.executeQuery("SELECT * FROM movies Where isAvailable = true");
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

    public void rentMovie(User currentUser) {
        boolean appCompleted = false;
        int numOfDays;

        do {
            try {
                Boolean isAvailable = false;
                String movieId;

                //asking user for the Id of the movie he wants to rent, as soon as he selects
                //a rental object is created with his Id against the movie he selected. This object will be stored in the rentals table in database.
                do {
                    System.out.println("Please enter the Id of the movie you want to rent\nPress B to go to main menu");
                    //handling inputmismatch exception
                    while (true) {
                        try {
                            movieId = keyboard.next();

                        } catch (Exception e) {
                            System.out.println("Please select a valid option");
                            keyboard.nextLine();
                            continue;
                        }
                        break;
                    }
                    if (movieId.equalsIgnoreCase("b") && movieId.matches("[a-zA-Z]")) {
                        break;
                    } else {

                        ResultSet rs = stmt.executeQuery("SELECT * FROM movies WHERE id=" + Integer.valueOf(movieId) + "");
                        if (!rs.next()) {
                            System.out.println("Movie not in Record");

                        } else {
                            //if movie is visible in our shop, we check if it is already rented out.
                            //this makes sure that a user cannot select a movie Id which is not visible
                            isAvailable = rs.getBoolean(13);
                            if (!isAvailable) {
                                System.out.println("Movie not available");
                            }
                        }
                    }

                } while (!isAvailable);
                if (isAvailable == false) {
                    break;
                }
                System.out.println("How long do you want to rent for? Enter number of days");

                //handling inputmismatch exception
                while (true) {
                    try {
                        numOfDays = keyboard.nextInt();
                        if(numOfDays<1){
                            throw new Exception();
                        }

                    } catch (Exception e) {
                        System.out.println("Please Enter a Valid Number of Days");
                        keyboard.nextLine();
                        continue;
                    }
                    break;
                }

                Rental newRent = new Rental(Integer.parseInt(movieId), currentUser.id, numOfDays);
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
                System.out.println("\n----------------------------------------");

                appCompleted = true;
            } catch (Exception e) {
                System.out.println("Enter a valid response");
                continue;
            }

        } while (!appCompleted);

    }

    public void returnMovie(User _user) {
        String movieId;

        try {
            Statement tempState = conn.createStatement();
            System.out.println("Please enter a valid ID of the Movie you want to return");
            while (true) {
                try {
                    movieId = keyboard.next();
                    if (movieId.equalsIgnoreCase("b") && movieId.matches("[a-zA-Z]")) {
                        System.out.println("----------------------------------------\n");
                    } else {
                        ResultSet RentalForUser = stmt.executeQuery("SELECT * FROM rentals WHERE userId = " + _user.id + " AND movieId = " + Integer.valueOf(movieId) + "");
                        if (!RentalForUser.next()) {
                            System.out.println("Please enter a valid movie ID which was rented to you\nPress B to go back to main menu");
                            continue;

                        } else {
                            ResultSet correspondingMovie = tempState.executeQuery("SELECT * FROM movies WHERE id = " + Integer.valueOf(movieId) + "");
                            correspondingMovie.next();
                            System.out.println("----------------------------------------");
                            System.out.println("Now returning:\n" + correspondingMovie.getString("title") + ", which was rented out at " + RentalForUser.getDate("rentedAt") + "\n");
                            System.out.println("----------------------------------------");
                            tempState.execute("update movies set isAvailable = true where id = " + RentalForUser.getString("movieId") + "; ");
                            if (new java.sql.Date(new Date().getTime()).compareTo(RentalForUser.getDate("returnAt")) > 0) {
                                System.out.println("WARNING! MOVIE WAS HELD FOR MORE THAN REQUESTED TIME\n");
                                System.out.println("You have been fined an additional");
                                System.out.println("----------------------------------------\n");

                            }

                        }
                    }

                } catch (Exception e) {
                    System.out.println("Please enter a numeric Id");
                    keyboard.nextLine();
                    continue;
                }
                break;
            }

        } catch (SQLException ex) {
            System.out.println("This rental was not found in DB");
        }

    }

}
