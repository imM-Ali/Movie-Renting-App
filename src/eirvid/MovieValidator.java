package eirvid;

import Interfaces.MoviesValidatorInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MovieValidator implements MoviesValidatorInterface{
    @Override
    public boolean Validate(String[] fields) {      
        
        if (fields.length != 11) {
            System.out.println("Warning: Incorrect number of fields");
            return false;
        }

        if (fields[0].length() < 1) {
            System.out.println("Warning: Movie language malformed");
        }
        
        if (fields[1].length() < 1) {
            System.out.println("Warning: Movie original title malformed");
        }
        
        if (fields[2].length() < 1) {
            System.out.println("Warning: Movie overview malformed");
        }
        
        double moviePop = -1;
        try {
            moviePop = Double.parseDouble(fields[3]);
        } 
        catch (Exception e) {
             System.out.println("Warning: Movie popularity is not a valid decimal");
             return false;
        }
        
        // from stack overflow https://stackoverflow.com/questions/27007995/how-to-compare-date-format
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);
            date = dateFormat.parse(fields[4]);
        } catch (ParseException ex) {
            return false;
        }

        int runtime = -1;
        try {
            runtime = Integer.parseInt(fields[5].trim());
        } 
        catch (Exception e) {
             System.out.println("Warning: Movie runtime is not a valid integer");
             return false;
        }
        
        if (fields[6].length() < 1) {
            System.out.println("Warning: Movie tagline malformed");
        }
        
        if (fields[7].length() < 1) {
            System.out.println("Warning: Movie title malformed");
        }
        
        double movieVoteAvg = -1;
        try {
            movieVoteAvg = Double.parseDouble(fields[8]);
        } 
        catch (Exception e) {
             System.out.println("Warning: Movie vote average is not a valid decimal");
             return false;
        }
        
        int voteCount = -1;
        try {
            voteCount = Integer.parseInt(fields[9].trim());
        } 
        catch (Exception e) {
             System.out.println("Warning: Movie vote count is not a valid integer");
             return false;
        }

        double moviePrice = -1;
        try {
            moviePrice = Double.parseDouble(fields[10]);
        } 
        catch (Exception e) {
            System.out.println(e);
             System.out.println("Warning: Movie price not a valid decimal");
             return false;
        }
        
        return true;
    }
}
