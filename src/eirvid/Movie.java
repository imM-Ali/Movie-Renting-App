package eirvid;

/**
 *
 * @author diesel Student number: 2020302
 */
public class Movie {

    String orgLang;
    String orgTitle;
    String overview;
    double popularity;
    String releaseDate;
    int runtime;
    String tagline;
    String title;
    double voteAvg;
    int voteCount;
    double price;
    Boolean isAvailable;

    public Movie(String orgLang, String orgTitle, String overview, double popularity, String releaseDate, int runtime, String tagline, String title, double voteAvg, int voteCount, double price) {
        this.orgLang = orgLang;
        this.orgTitle = orgTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.tagline = tagline;
        this.title = title;
        this.voteAvg = voteAvg;
        this.voteCount = voteCount;
        this.price = price;
        this.isAvailable = true;
    }
}
