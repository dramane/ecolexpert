/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoCiviliteFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoCivilite;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author toshiba
 */
@Named(value = "civiliteBean")
@RequestScoped
public class CiviliteBean {
    private EcoCivilite civilite;
    private List listCivilite;   
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoCiviliteFacadeLocal civiliteFacadeL;

    /**
     * Creates a new instance of CiviliteBean
     */
    public CiviliteBean() {
        civilite = new EcoCivilite();
    }
    
    //getter et setter civilité
    public EcoCivilite getCivilite() {
        return civilite;
    }

    public void setCivilite(EcoCivilite civilite) {
        this.civilite = civilite;
    }
    
    //Liste des civilite a afficher dans le champ select
    public List getListCivilite(){   
        try{  
            listCivilite = new ArrayList();
            for(EcoCivilite civ : civiliteFacadeL.findAll()){          
              listCivilite.add(new SelectItem(civ.getCivilite(), civ.getCivilite()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCivilite;
    }
}
