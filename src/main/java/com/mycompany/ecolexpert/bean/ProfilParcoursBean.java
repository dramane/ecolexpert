/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoProfilParcoursFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoProfilParcours;
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
@Named(value = "profilParcoursBean")
@RequestScoped
public class ProfilParcoursBean {
    private EcoProfilParcours profilParcours;
    private List<EcoProfilParcours> listProfilParcours;
    private List listCodeProfilParcours; 
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoProfilParcoursFacadeLocal  profilParcoursFacadeL;

    /**
     * Creates a new instance of ProfilParcoursBean
     */
    public ProfilParcoursBean() {
        profilParcours = new EcoProfilParcours();
    }
    
    //Getter et setter profilParcours
    public EcoProfilParcours getProfilParcours() {
        return profilParcours;
    }

    public void setProfilParcours(EcoProfilParcours profilParcours) {
        this.profilParcours = profilParcours;
    }
    
    //Liste de tous les profilParcours
    public List<EcoProfilParcours> getListProfilParcours() {
        try{
            listProfilParcours = profilParcoursFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listProfilParcours;
    }

    //Getter de codeProfilParcours a aficher dans le combobox
    public List getListCodeProfilParcours() {
        try{  
            listCodeProfilParcours = new ArrayList();
            for(EcoProfilParcours profilF : profilParcoursFacadeL.findAll()){          
              listCodeProfilParcours.add(new SelectItem(profilF.getCodeProfilParcours(), profilF.getLibelleProfilParcours()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeProfilParcours;
    }
    
}
