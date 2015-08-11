/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoCycleFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoDiplomeFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoCycle;
import com.mycompany.ecolexpert.jpa.EcoDiplome;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author HARRISON
 */
@Named(value = "diplomeBean")
@RequestScoped
public class DiplomeBean implements Serializable{

    private EcoDiplome diplome;
    private List<EcoDiplome> listDiplome;

// Pour récupérer le cycle    
    private EcoCycle cycle;
    private List listCodeCycle;

    @EJB
    private EcoDiplomeFacadeLocal ecoDiplomeFacadeL;
    @EJB
    private EcoCycleFacadeLocal cycleFacadeL;

    /**
     * Creates a new instance of DiplomeBean
     */
    public DiplomeBean() {
        diplome = new EcoDiplome();
        cycle = new EcoCycle();
    }

    /**
     * @return the diplome
     */
    public EcoDiplome getDiplome() {
        return diplome;
    }

    /**
     * @param diplome the diplome to set
     */
    public void setDiplome(EcoDiplome diplome) {
        this.diplome = diplome;
    }

    /**
     * @return the listDiplome
     */
    public List<EcoDiplome> getListDiplome() {
        try {
            listDiplome = ecoDiplomeFacadeL.findAll();
        } catch (Exception ex) {
            System.err.println("Erreur capturée : " + ex);
        }
        return listDiplome;
    }

    /**
     * @return the cycle
     */
    public EcoCycle getCycle() {
        return cycle;
    }

    /**
     * @param cycle the cycle to set
     */
    public void setCycle(EcoCycle cycle) {
        this.cycle = cycle;
    }

    /**
     * @return the listCodeCycle
     */
    public List getListCodeCycle() {
        listCodeCycle = new ArrayList();            
        for (EcoCycle cycl : cycleFacadeL.findAll()){
            listCodeCycle.add(new SelectItem(cycl.getCodeCycle(), cycl.getDesignation()));
        }
        return listCodeCycle;
    }

    /**
     * @param listCodeCycle the listCodeCycle to set
     */
    public void setListCodeCycle(List listCodeCycle) {
        this.listCodeCycle = listCodeCycle;
    }

}
