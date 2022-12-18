/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

import Interfaces.MoviesMapperInterface;
import Interfaces.MoviesParserInterface;
import Interfaces.MoviesValidatorInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author diese
 */
public class MovieParser implements MoviesParserInterface{
    private MoviesValidatorInterface movieValidator;
    private MoviesMapperInterface movieMapper;

    public MovieParser(MoviesValidatorInterface movieValidator, MoviesMapperInterface movieMapper) {
        this.movieValidator = movieValidator;
        this.movieMapper = movieMapper;
    }
    
    public List<Movie> ParseMovies(List<String[]> lines) {
        List<Movie> movies = new ArrayList<>();        
        lines.forEach(line -> {
            String[] fields = Arrays.toString(line).replace("[", "").replace("]", "").split(","); 
            
            if (movieValidator.Validate(fields)) {
                movies.add(movieMapper.Map(fields));
            }
        
        });        
        return movies;
    }
}
