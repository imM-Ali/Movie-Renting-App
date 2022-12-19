package eirvid;

import Interfaces.MoviesMapperInterface;
import Interfaces.MoviesParserInterface;
import Interfaces.MoviesValidatorInterface;
import java.io.IOException;
import java.util.List;


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
       
    }
}
