/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;
import java.sql.Statement;
import static eirvid.EirVid.conn;
import static eirvid.EirVid.stmt;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

  



/**
 *
 * @author Erick
 */
class userHistory {

    public void view(User currentUser) {
        try {
            ResultSet _currentHistory = stmt.executeQuery("SELECT history FROM users WHERE id =" + currentUser.id + ";");
            Statement extraState = conn.createStatement();
            if (_currentHistory.next()) {
                String historyString = _currentHistory.getString("history");
                String[] movieIds = historyString.split(",");

                for (String movieId : movieIds) {
                   ResultSet movieDetails = extraState.executeQuery("SELECT title, price, popularity FROM movies WHERE id = "+movieId+";");
                   while (movieDetails.next()) {

                System.out.println("Title: " + movieDetails.getString("title") + "\n");
                System.out.println("Rating: " + movieDetails.getString("popularity") + "\n");
                System.out.println("Price (per day): " + movieDetails.getString("price") + "\n");
                System.out.println("----------------------------------------");

            }

                }

            } else {
                System.out.println("You havent rented anything");
            }

        } catch (SQLException ex) {
            Logger.getLogger(userHistory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
    

