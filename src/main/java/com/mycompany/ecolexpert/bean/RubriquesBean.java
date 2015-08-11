/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoRubriquesFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoRubriques;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author TOSHIBA
 */
@Named(value = "ecoRubriquesBean")
@RequestScoped
public class RubriquesBean {
    private EcoRubriques rubrique;   
    private List<EcoRubriques> listRubrique;  
    private List listCodeRubrique;
    
    // Injection de notre EJB facade local (Session Bean Stateless)
    @EJB
    private EcoRubriquesFacadeLocal rubriqueFacadeL;

    /**
     * Creates a new instance of ecoRubriquesBean
     */
    public RubriquesBean() {
       rubrique = new EcoRubriques();
    }

    public EcoRubriques getRubrique() {
        return rubrique;
    }

    public void setRubrique(EcoRubriques rubrique) {
        this.rubrique = rubrique;
    }
    
    //liste des rubrique
    public List<EcoRubriques> getListRubrique(){
       try{
              listRubrique = rubriqueFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listRubrique;     
    }
    
    public List listCodeRubrique(){
        listCodeRubrique = new ArrayList();
        for(EcoRubriques rubriq : getListRubrique()){          
          listCodeRubrique.add(new SelectItem(rubriq.getCode(), rubriq.getRubrique()));
        }
        return listCodeRubrique;        
    }
}
