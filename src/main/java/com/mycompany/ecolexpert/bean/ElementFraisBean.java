/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoElementFraisFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoElementFrais;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author HP
 */
@Named(value = "elementFraisBean")
@RequestScoped
public class ElementFraisBean {
    private EcoElementFrais elementFrais;
    private List<EcoElementFrais> listElementFrais;
    private List listCodeElementFrais;         
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoElementFraisFacadeLocal  elementFraisFacadeL;

    /**
     * Creates a new instance of ElementFraisBean
     */
    public ElementFraisBean() {
        elementFrais = new EcoElementFrais();
    }
    
    //getter et setter elementFrais
    public EcoElementFrais getElementFrais() {
        return elementFrais;
    }

    public void setElementFrais(EcoElementFrais elementFrais) {
        this.elementFrais = elementFrais;
    }
    
    //Liste des regimes a afficher dans le combobox
    public List getListCodeElementFrais() {
        try{  
            listCodeElementFrais = new ArrayList();
            for(EcoElementFrais elementFrai : elementFraisFacadeL.findAll()){
                listCodeElementFrais.add(new SelectItem(elementFrai.getCodeElementFrais(), elementFrai.getCodeElementFrais()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeElementFrais;
    }
    
    //liste de tous les elementfrais
    public List<EcoElementFrais> getListElementFrais(){  
        try{
            listElementFrais = elementFraisFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listElementFrais;
    }
}
