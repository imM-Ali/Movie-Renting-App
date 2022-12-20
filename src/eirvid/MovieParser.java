package eirvid;

/**
 *
 * @author diesel Student number: 2020302
 */
import Interfaces.MoviesMapperInterface;
import Interfaces.MoviesParserInterface;
import Interfaces.MoviesValidatorInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieParser implements MoviesParserInterface {

    private MoviesValidatorInterface movieValidator;
    private MoviesMapperInterface movieMapper;

    public MovieParser(MoviesValidatorInterface movieValidator, MoviesMapperInterface movieMapper) {
        this.movieValidator = movieValidator;
        this.movieMapper = movieMapper;
    }

    public List<Movie> ParseMovies(List<String[]> lines) {
        List<Movie> movies = new ArrayList<>();
        System.out.println("Validating data....");
        lines.forEach(line -> {
            String[] fields = Arrays.toString(line).replace("[", "").replace("]", "").split(",");

            if (movieValidator.Validate(fields)) {
                movies.add(movieMapper.Map(fields));
            }

        });
        return movies;
    }
}
