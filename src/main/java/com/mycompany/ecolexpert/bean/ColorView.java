/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author TOSHIBA
 */
@Named(value = "colorView")
@RequestScoped
public class ColorView {

    /**
     * Creates a new instance of ColorView
     */
    public ColorView() {
    }
    
    private String colorPopup;
 
    public String getColorPopup() {
        return colorPopup;
    }
 
    public void setColorPopup(String colorPopup) {
        this.colorPopup = colorPopup;
    } 
}
