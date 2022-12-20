package eirvid;

/**
 *
 * @author diesel
 * Student number: 2020302
 */

import Interfaces.MoviesDataInputInterface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MoviesDataInput implements MoviesDataInputInterface {
    BufferedReader myReader;
    String fileName = "src/Movie_Metadata_Edited_2.csv";
    public MoviesDataInput() throws FileNotFoundException, IOException {
        this.myReader = new BufferedReader(new FileReader(fileName));
    }
    
    @Override
    public List<String[]> ReadMoviesData() throws FileNotFoundException, IOException {
        //creates a list of String arrays where each list index is a row in CSV and each array index is a column in that row
        
        List<String[]> movieData = new ArrayList<>();
        String inputLine = "";
       
        while ((inputLine = myReader.readLine())!=null) {    
            inputLine = inputLine.replace("\'", "");
            movieData.add(inputLine.split(","));
            
        }
        
        return movieData;
    }
}
