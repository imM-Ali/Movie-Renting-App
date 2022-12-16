/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

import Interfaces.DBConnectionInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author diese
 */
public class DBConnection implements DBConnectionInterface{
    private String DB_URL = "jdbc:mysql://localhost/RTPlayer";
    private String USER = "Rental";
    private String PASS = "Rental";
        
    public void DBConnection (String DB_URL, String USER, String PASS) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.DB_URL = DB_URL;
        this.USER = USER;
        this.PASS = PASS;
        
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
           Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate("CREATE database if not exists RTPlayer;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
           Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS movies (id INT not null auto_increment, org_language varchar(5), org_title varchar(50), overview blob, popularity float(3,2), release_date varchar(10), runtime integer(3), tagline varchar(50), title varchar(50), vote_avg float(3,2), vote_count integer(5), price float(4,2), isAvailable boolean default false, last_rented datetime, primary key (id));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
           Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INT not null auto_increment, user_name varchar(20), email varchar(100), password varchar(20), primary key (id));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
           Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rental (id int not null auto_increment, id_user int not null, id_movie int not null, date datetime not null default current_timestamp, primary key (id));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public boolean ConnectDatabase(String sql) {
        
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
           Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate(sql);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
