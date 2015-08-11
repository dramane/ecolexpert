/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
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
@Named(value = "niveauBean")
@RequestScoped
public class NiveauBean {
    private EcoNiveau niveau;
    private List<EcoNiveau> listNiveau;
    private List listCodeNiveau; 
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoNiveauFacadeLocal  niveauFacadeL;

    /**
     * Creates a new instance of NiveauBean
     */
    public NiveauBean() {
        niveau = new EcoNiveau();
    }
    
    //getter et setter de niveau
    public EcoNiveau getNiveau() {
        return niveau;
    }

    public void setNiveau(EcoNiveau niveau) {
        this.niveau = niveau;
    }
    
    //Liste des niveau a afficher dans le combobox
    public List getListCodeNiveau(){   
        try{  
            listCodeNiveau = new ArrayList();
            for(EcoNiveau niv : niveauFacadeL.findAll()){          
              listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeNiveau;
    }
    
    //liste de tous les niveau a afficher dans un datatable
    public List<EcoNiveau> getListNiveau(){  
        try{
            listNiveau = niveauFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listNiveau;
    }
    
}
