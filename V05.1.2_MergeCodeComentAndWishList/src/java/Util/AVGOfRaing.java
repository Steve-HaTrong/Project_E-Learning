/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import Model.StarRatingDTO;
import java.util.ArrayList;

/**
 *
 * @author Tuan Anh(Gia Truong)
 */
public class AVGOfRaing {
    public static ArrayList<Double> AvgRatingCourse(ArrayList<StarRatingDTO> listRatingCourse ) {
        ArrayList<Double> list = new ArrayList<>();
        double avg = 0;
        double count = 0;
        for (StarRatingDTO starRatingDTO : listRatingCourse) {
            avg += starRatingDTO.getStar();
            count++;
        }
        list.add(Math.floor(avg / count));
        list.add(count);
        return list;
        
    }
}
