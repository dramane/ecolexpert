/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoTypeParcoursFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoTypeParcours;
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
@Named(value = "typeParcoursBean")
@RequestScoped
public class TypeParcoursBean {
    private EcoTypeParcours typeParcours;
    private List<EcoTypeParcours> listTypeParcours;
    private List listCodeTypeParcours; 
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoTypeParcoursFacadeLocal  typeParcoursFacadeL;

    /**
     * Creates a new instance of TypeParcoursBean
     */
    public TypeParcoursBean() {
        typeParcours = new EcoTypeParcours();
    }
    
    //Getter et setter typeParcours
    public EcoTypeParcours getTypeParcours() {
        return typeParcours;
    }

    public void setTypeParcours(EcoTypeParcours typeParcours) {
        this.typeParcours = typeParcours;
    }

    //Liste de tous les typeParcours
    public List<EcoTypeParcours> getListTypeParcours() {
        try{
            listTypeParcours = typeParcoursFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listTypeParcours;
    }

    //Getter de CodeTypeParcours a aficher dans le combobox
    public List getListCodeTypeParcours() {
        try{  
            listCodeTypeParcours = new ArrayList();
            for(EcoTypeParcours typeP: typeParcoursFacadeL.findAll()){          
              listCodeTypeParcours.add(new SelectItem(typeP.getCodeTypeParcours(), typeP.getLibelleTypeParcours()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeTypeParcours;
    }    
}
