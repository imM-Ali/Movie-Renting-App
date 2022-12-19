package eirvid;

import Interfaces.MoviesMapperInterface;

public class MovieMapper implements MoviesMapperInterface{
    @Override
    public Movie Map(String[] fields) {  
        
        String orgLang = fields[0];
        String orgTitle = fields[1];
        String overview = fields[2];
        double popularity = Double.parseDouble(fields[3]);
        String releaseDate = fields[4];
        int runtime = Integer.parseInt(fields[5].trim());
        String tagline = fields[6];
        String title = fields[7];
        double voteAvg = Double.parseDouble(fields[8]);
        int voteCount = Integer.parseInt(fields[9].trim());
        double price = Double.parseDouble(fields[10]);
       
        return new Movie(orgLang, orgTitle, overview, popularity, releaseDate, runtime, tagline, title, voteAvg, voteCount, price);      
    }
}
