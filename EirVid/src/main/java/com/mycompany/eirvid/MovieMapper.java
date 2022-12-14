/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.eirvid;

import Interfaces.MoviesMapperInterface;

/**
 *
 * @author diese
 */
public class MovieMapper implements MoviesMapperInterface{
    @Override
    public MovieTitle Map(String[] fields) {
        String orgLang = fields[0];
        String orgTitle = fields[1];
        String overview = fields[2];
        double popularity = Double.parseDouble(fields[3]);
        String releaseDate = fields[4];
        int runtime = Integer.parseInt(fields[5]);
        String tagline = fields[6];
        String title = fields[7];
        double voteAvg = Double.parseDouble(fields[8]);
        int voteCount = Integer.parseInt(fields[11]);
        double price = Double.parseDouble(fields[10]);

        return new MovieTitle(orgLang, orgTitle, overview, popularity, releaseDate, runtime, tagline, title, voteAvg, voteCount, price);      
    }
}
