/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author toshiba
 */
@Named(value = "systemeAcademiqBean")
@RequestScoped
public class SystemeAcademiqBean {

    private String systeme;
    private boolean activer;
    private boolean desactiver;

    /**
     * Creates a new instance of systemeAcademiqBean
     */
    public SystemeAcademiqBean() {
        systeme = new String();
    }

    public String getSysteme() {
        return systeme;
    }

    public void setSysteme(String systeme) {
        this.systeme = systeme;
    }

    public boolean getActiver() {
        return activer;
    }

    public void setActiver(boolean activer) {
        this.activer = activer;
    }

    public boolean getDesactiver() {
        return desactiver;
    }

    public void setDesactiver(boolean desactiver) {
        this.desactiver = desactiver;
    }       

    public void valueChanged() {
        if(systeme !=null && !systeme.equals("")) {
            if ("Classique".equals(systeme)) {
                activer = false;
                desactiver = true;
                System.out.println("le systeme est : " + systeme);
            } else {
                activer = true;
                desactiver = false;
                System.out.println("le systeme est : " + systeme);
            }
        }
        
    }
    
    
//    public void valueChanged(ValueChangeEvent event) {
//        if (null != event.getNewValue()) {
//            String currentItem;
//            System.out.println("IL y a un evenement " + event.getNewValue().toString());
//            String valeurSelect = event.getNewValue().toString();
//
//            if (valeurSelect.equals("Classique")) {
//                activer = false;
//                desactiver = true;
//                System.out.println("le systeme est : " + valeurSelect);
//            } else {
//                activer = true;
//                desactiver = false;
//                System.out.println("le systeme est : " + valeurSelect);
//            }
//        }
//    }
}
