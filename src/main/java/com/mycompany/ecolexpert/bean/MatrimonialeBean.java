/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoMatrimonialeFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoMatrimoniale;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author HP
 */
@Named(value = "matrimonialeBean")
@RequestScoped
public class MatrimonialeBean {
    private EcoMatrimoniale matrimoniale;   
    private List listCodeMatrimoniale; 
    
    @EJB
    EcoMatrimonialeFacadeLocal matrimonialeFacadeL;
    
    /**
     * Creates a new instance of MatrimonialeBean
     */
    public MatrimonialeBean() {
        matrimoniale = new EcoMatrimoniale();
    }
    
    //getter et setter de matrimoniale

    public EcoMatrimoniale getMatrimoniale() {
        return matrimoniale;
    }

    public void setMatrimoniale(EcoMatrimoniale matrimoniale) {
        this.matrimoniale = matrimoniale;
    }

    public List getListCodeMatrimoniale() {
        try{  
            listCodeMatrimoniale = new ArrayList();
            for(EcoMatrimoniale mat : matrimonialeFacadeL.findAll()){          
              listCodeMatrimoniale.add(new SelectItem(mat.getCodeMatrimoniale(), mat.getLibelleMatrimoniale()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeMatrimoniale;
    }
    
}
