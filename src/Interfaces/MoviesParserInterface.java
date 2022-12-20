/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import eirvid.Movie;
import java.util.List;

/**
 *
 * @author diesel
 * Student number: 2020302
 */
public interface MoviesParserInterface {
    public List<Movie> ParseMovies(List<String[]> lines);
}
