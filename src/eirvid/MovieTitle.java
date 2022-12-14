/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eirvid;

/**
 *
 * @author diese
 */
public class MovieTitle {
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

    public MovieTitle(String orgLang, String orgTitle, String overview, double popularity, String releaseDate, int runtime, String tagline, String title, double voteAvg, int voteCount, double price) {
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
    }
}
