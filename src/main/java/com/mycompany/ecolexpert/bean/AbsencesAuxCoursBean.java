/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoAcademiqueFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoClasseFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoCycleFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoEtudiantFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.ejb.ViewListeEtudiantInscriptionFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoClasse;
import com.mycompany.ecolexpert.jpa.EcoCycle;
import com.mycompany.ecolexpert.jpa.EcoEtudiant;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
import com.mycompany.ecolexpert.jpa.ViewListeEtudiantInscription;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author HP
 */
@Named(value = "absencesAuxCoursBean")
@ViewScoped
public class AbsencesAuxCoursBean implements Serializable{ 
    private ViewListeEtudiantInscription viewListEtudiantInsc;
    private List<ViewListeEtudiantInscription> listViewListEtudiantInsc;
    private List listCodeCycle;
    private List listCodeNiveau;
    private List listCodeClasse;
    
    //les parametres de ma recherche d'etudiants   
    String cycle = "";
    String niveau = "";
    String classe = "";
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private ViewListeEtudiantInscriptionFacadeLocal  viewListEtudiantInscFacadeL;    
    @EJB private EcoCycleFacadeLocal cycleFacadeL;
    @EJB private EcoNiveauFacadeLocal niveauFacadeL;
    @EJB private EcoClasseFacadeLocal  classeFacadeL;

    /**
     * Creates a new instance of AbsencesAuxCoursBean
     */
    public AbsencesAuxCoursBean() {
        viewListEtudiantInsc = new ViewListeEtudiantInscription();
    }         
    
    //getter et setter de viewListEtudiantInsc
    public ViewListeEtudiantInscription getViewListEtudiantInsc() {
        return viewListEtudiantInsc;
    }

    public void setViewListEtudiantInsc(ViewListeEtudiantInscription viewListEtudiantInsc) {
        this.viewListEtudiantInsc = viewListEtudiantInsc;
    } 

    //getter et setter de mes parametres de recherche   
    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
    
    
    //Liste des codes cycle a afficher dans le combobox 
    public List getListCodeCycle(){
        listCodeCycle = new ArrayList();                      
            for (EcoCycle cycl : cycleFacadeL.findAll()){
                listCodeCycle.add(new SelectItem(cycl.getCodeCycle(), cycl.getDesignation()));
            }        
        return listCodeCycle;        
    }
    
    //Liste des codes niveau a afficher dans le combobox
    public List getListCodeNiveau(){
        return listCodeNiveau;
    }
    
    //Liste des codes classe a afficher dans le combobox
    public List getListCodeClasse(){
        return listCodeClasse;
    }
    
    //Action ajax lors de la selection d'un cycle
    public void onCycleChange(){
       listCodeNiveau = new ArrayList();
        if(cycle != null && !"".equals(cycle)){            
            List<EcoNiveau> data ;
            data = niveauFacadeL.findByCodeCycle(cycle);
            for(EcoNiveau niv : data){          
              listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
            }   
        }else { 
            listCodeNiveau = new ArrayList();
            listCodeClasse = new ArrayList();
            niveau = "";
            classe = "";
        }   
    } 
    
    //Action ajax lors de la selection d'un niveau
    public void onNiveauChange(){
       listCodeClasse = new ArrayList();
        if(niveau != null && !"".equals(niveau)){            
            List<EcoClasse> data ;
            data = classeFacadeL.findByCodeNiveau(niveau);
            for(EcoClasse clas : data){          
              listCodeClasse.add(new SelectItem(clas.getCodeClasse(), clas.getNomclasse()));
            }  
        }else {
            listCodeClasse = new ArrayList();
            classe = "";
        }    
    } 
    
    //Action ajax lors de la selection d'une classe
    public void onClasseChange(){
       
    } 
}
