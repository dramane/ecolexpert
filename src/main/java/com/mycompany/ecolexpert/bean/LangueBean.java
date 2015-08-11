/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoLanguesFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoLangues;
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
@Named(value = "langueBean")
@RequestScoped
public class LangueBean {
    private EcoLangues langues;
    private List<EcoLangues> listLangues;
    private List listCodeLangues;        
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoLanguesFacadeLocal languesFacadeL;
    

    /**
     * Creates a new instance of LangueBean
     */
    public LangueBean() {
        langues = new EcoLangues();
    }
    
    //getter et setter de langue
    public EcoLangues getLangues() {
        return langues;
    }

    public void setLangues(EcoLangues langues) {
        this.langues = langues;
    }
    
    //liste des listCodeLangues a afficher dans un combobox
    public List getListCodeLangues() {
        try{  
            listCodeLangues = new ArrayList();
            for(EcoLangues lang : languesFacadeL.findAll()){
                listCodeLangues.add(new SelectItem(lang.getLibelleLangue(), lang.getLibelleLangue()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeLangues;
    }
    
    //liste de toutes les langues
    public List<EcoLangues> getListLangues() {
        try{
            listLangues = languesFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listLangues;
    }
    
}
