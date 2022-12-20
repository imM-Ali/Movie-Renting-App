/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author diesel
 * Student number: 2020302
 */
public interface MoviesDataInputInterface {
    public List<String[]> ReadMoviesData() throws FileNotFoundException, IOException;
}
