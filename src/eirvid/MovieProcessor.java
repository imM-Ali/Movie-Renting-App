/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

import Interfaces.MoviesParserInterface;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author diese
 */
public class MovieProcessor {
    
    private MoviesDataInput movieDataInput;
    private MoviesParserInterface movieParser;
    private DBConnection dbConnection;
    
    public MovieProcessor(MoviesDataInput movieDataInput, MoviesParserInterface movieParser, DBConnection dbConnection) {
        this.movieDataInput = movieDataInput;
        this.movieParser = movieParser;
        this.dbConnection = dbConnection;
    }
    
 
    public void ProcessMovies() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<String> lines = movieDataInput.ReadMoviesData();
        List<MovieTitle> movies = movieParser.ParseMovies(lines);
        String sql="";
        for (MovieTitle movie : movies) {
                    sql = String.format("INSERT INTO Movies (org_language, org_title, overview, popularity, release_date, runtime, tagline, title, vote_avg, vote_count, price, isAvailable) VALUES ('%s', '%s', '%s', %f, '%s', %d, '%s', %f, %d, %f, %b);",
                    movie.orgLang, movie.orgTitle, movie.overview, movie.popularity, movie.releaseDate, movie.runtime, movie.tagline, movie.voteAvg, movie.voteCount, movie.price, true);
                    dbConnection.ConnectDatabase(sql);
            }
    }
}
