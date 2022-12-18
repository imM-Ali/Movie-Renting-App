/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

import Interfaces.MoviesMapperInterface;
import Interfaces.MoviesParserInterface;
import Interfaces.MoviesValidatorInterface;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diese
 */
public class MovieProcessor {
    
    
    private MoviesParserInterface movieParser;
    private MoviesDataInput movieDataInput;
    private MoviesValidatorInterface movieValidator = new MovieValidator();
    private MoviesMapperInterface movieMapper = new MovieMapper();
    
    
    public MovieProcessor() throws IOException {
        this.movieDataInput = new MoviesDataInput();
        this.movieParser = new MovieParser(movieValidator, movieMapper); 
        this.movieDataInput = movieDataInput;
        this.movieParser = movieParser;        
        
    }
    
 
    public List<Movie> ProcessMovies() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<String[]> lines = movieDataInput.ReadMoviesData(); 
        //removing the first row (because it only gives the field labels)
        lines.remove(0); 
        
        //turns our raw csv data that was read in moviesdatainput class into a map with key value pairs, like title price popularity etc. 
        List<Movie> movies = movieParser.ParseMovies(lines);
        
        //returns meaningful data with key value pairs
        return movies;
       // String sql="";
       
        //for (MovieTitle movie : movies) {
           //         sql = String.format("INSERT INTO Movies (org_language, org_title, overview, popularity, release_date, runtime, tagline, title, vote_avg, vote_count, price, isAvailable) VALUES ('%s', '%s', '%s', %f, '%s', %d, '%s', %f, %d, %f, %b);",
            //        movie.orgLang, movie.orgTitle, movie.overview, movie.popularity, movie.releaseDate, movie.runtime, movie.tagline, movie.voteAvg, movie.voteCount, movie.price, true);
           //         dbConnection.ConnectDatabase(sql);
           // }
    }
}
