/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import com.mycompany.eirvid.MovieTitle;
import java.util.List;

/**
 *
 * @author diese
 */
public interface MoviesParserInterface {
    public List<MovieTitle> ParseMovies(List<String> lines);
}
