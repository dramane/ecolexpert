/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoEquipeFormationFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoFiliereFacadeLocal;
import com.mycompany.ecolexpert.ejb.ViewFiliereEquipeFormationFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoEquipeFormation;
import com.mycompany.ecolexpert.jpa.EcoFiliere;
import com.mycompany.ecolexpert.jpa.ViewFiliereEquipeFormation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/**
 *
 * @author TOSHIBA
 */
@Named(value = "filiereBean")
@RequestScoped
public class FiliereBean extends BoutonActiveBean{
    private EcoFiliere filiere;
    private List<EcoFiliere> listFiliere;
    private List listCodeFiliere; 
    
    private EcoEquipeFormation equipeFormation;
    
    private ViewFiliereEquipeFormation viewFiliereEquipeFormation;
    private List<ViewFiliereEquipeFormation> listViewFiliereEquipeFor;
    
    //champ utilisé pour comparer le code du filière
    private String codeFiliereCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoFiliereFacadeLocal  filiereFacadeL;
    @EJB private EcoEquipeFormationFacadeLocal equipeFormationFacadeL;
    @EJB private ViewFiliereEquipeFormationFacadeLocal viewFiliereEquipeForFacadeL;

    /**
     * Creates a new instance of FiliereBean
     */
    public FiliereBean() {
        filiere = new EcoFiliere();
        equipeFormation = new EcoEquipeFormation();
        viewFiliereEquipeFormation = new ViewFiliereEquipeFormation();
    }
    
    //getter et setter filiere
    public EcoFiliere getFiliere() {
        return filiere;
    }

    public void setFiliere(EcoFiliere filiere) {
        this.filiere = filiere;
    }    

    //getter et setter equipeFormation
    public EcoEquipeFormation getEquipeFormation() {    
        return equipeFormation;
    } 
    
    public void setEquipeFormation(EcoEquipeFormation equipeFormation) {        
        this.equipeFormation = equipeFormation;
    }       

    //Getter et setter viewFiliereEquipeFormation
    public ViewFiliereEquipeFormation getViewFiliereEquipeFormation() {
        return viewFiliereEquipeFormation;
    }

    public void setViewFiliereEquipeFormation(ViewFiliereEquipeFormation viewFiliereEquipeFormation) {
        this.viewFiliereEquipeFormation = viewFiliereEquipeFormation;
    }    

    //getter et setter codeFiliereCache
    public String getCodeFiliereCache() {
        return codeFiliereCache;
    }

    public void setCodeFiliereCache(String codeFiliereCache) {
        this.codeFiliereCache = codeFiliereCache;
    }    
    
    //Liste des filiere a afficher dans le combobox
    public List getListCodeFiliere(){   
        try{  
            listCodeFiliere = new ArrayList();
            for(EcoFiliere fil : filiereFacadeL.findAll()){          
              listCodeFiliere.add(new SelectItem(fil.getCodeFiliere(), fil.getDesignation()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeFiliere;
    }
    
    //liste de toutes les Filiere
    public List<EcoFiliere> getListFiliere(){  
        try{
            listFiliere = filiereFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listFiliere;
    }
    
    //Liste de tous les FiliereEquipeFormation 
    public List<ViewFiliereEquipeFormation> getListViewFiliereEquipeFor(){    
        try{
            listViewFiliereEquipeFor = viewFiliereEquipeForFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listViewFiliereEquipeFor;
    }

    //preparation d'un nouvel enregistrement
    public void nouveau() {
        try {            
            viewFiliereEquipeFormation.setCodeFiliere(null);
            setCodeFiliereCache(null);
            viewFiliereEquipeFormation.setDesignation(null);
            viewFiliereEquipeFormation.setDescription(null);
            viewFiliereEquipeFormation.setCodeDomaine(null); 
            
            viewFiliereEquipeFormation.setCodeEquipeForm(0);
            viewFiliereEquipeFormation.setNomFormateur1(null);
            viewFiliereEquipeFormation.setPrenomFormateur1(null);
            viewFiliereEquipeFormation.setTitreFormateur1(null);
            viewFiliereEquipeFormation.setNomFormateur2(null);
            viewFiliereEquipeFormation.setPrenomFormateur2(null);
            viewFiliereEquipeFormation.setTitreFormateur2(null);
            viewFiliereEquipeFormation.setNomFormateur3(null);
            viewFiliereEquipeFormation.setPrenomFormateur3(null);
            viewFiliereEquipeFormation.setTitreFormateur3(null);
            
            setBtnModifierDesactive(true);  
            setBtnSupprimerDesactive(true);
            setChampLectureSeul(false);   
            setChampCleNonAuto(false);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //initialisation et verrouillage des champs et initialisation des boutons
    public void annuler(){
        try {
            viewFiliereEquipeFormation.setCodeFiliere(null);
            setCodeFiliereCache(null);
            viewFiliereEquipeFormation.setDesignation(null);
            viewFiliereEquipeFormation.setDescription(null);
            viewFiliereEquipeFormation.setCodeDomaine(null);
            
            viewFiliereEquipeFormation.setCodeEquipeForm(0);
            viewFiliereEquipeFormation.setNomFormateur1(null);
            viewFiliereEquipeFormation.setPrenomFormateur1(null);
            viewFiliereEquipeFormation.setTitreFormateur1(null);
            viewFiliereEquipeFormation.setNomFormateur2(null);
            viewFiliereEquipeFormation.setPrenomFormateur2(null);
            viewFiliereEquipeFormation.setTitreFormateur2(null);
            viewFiliereEquipeFormation.setNomFormateur3(null);
            viewFiliereEquipeFormation.setPrenomFormateur3(null);
            viewFiliereEquipeFormation.setTitreFormateur3(null);
            
            setBtnModifierDesactive(true);
            setBtnSupprimerDesactive(true) ; 
            setBtnValiderDesactive(true);
            setChampLectureSeul(true);  
            setChampCleNonAuto(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //creation d'un enregistrement
    public void creer(){
        try {
            //Enregistrement d'un systemeEducatif
            filiereFacadeL.create(filiere);    
            equipeFormationFacadeL.create(equipeFormation);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            filiere.setCodeFiliere(viewFiliereEquipeFormation.getCodeFiliere());
            
            if(null == filiere.getCodeFiliere() || Objects.equals(filiere.getCodeFiliere(), "")){            
                FacesMessage message = new FacesMessage( "Veuillez sélectionner l'enregistrement à modifier!" );
                FacesContext.getCurrentInstance().addMessage(null, message);            
            }            
            setBtnModifierDesactive(true);  
            setBtnSupprimerDesactive(true);
            setChampCleNonAuto(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //modification de l'enregistrement
    public void modifier(){
        try {
            //modification de la filière et de son equipe de formation
            filiereFacadeL.edit(filiere);
            equipeFormationFacadeL.edit(equipeFormation);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
   
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        // Affectation des champs de ViewFiliereEquipeFormation dans filiere et dans equipeFormation
        filiere.setCodeFiliere(viewFiliereEquipeFormation.getCodeFiliere());
        filiere.setDesignation(viewFiliereEquipeFormation.getDesignation());
        filiere.setDescription(viewFiliereEquipeFormation.getDescription());
        filiere.setCodeDomaine(viewFiliereEquipeFormation.getCodeDomaine());
        filiere.setActif(viewFiliereEquipeFormation.getActif());
        
        equipeFormation.setNomFormateur1(viewFiliereEquipeFormation.getNomFormateur1());
        equipeFormation.setPrenomFormateur1(viewFiliereEquipeFormation.getPrenomFormateur1());
        equipeFormation.setTitreFormateur1(viewFiliereEquipeFormation.getTitreFormateur1());
        equipeFormation.setNomFormateur2(viewFiliereEquipeFormation.getNomFormateur2());
        equipeFormation.setPrenomFormateur2(viewFiliereEquipeFormation.getPrenomFormateur2());
        equipeFormation.setTitreFormateur2(viewFiliereEquipeFormation.getTitreFormateur2());
        equipeFormation.setNomFormateur3(viewFiliereEquipeFormation.getNomFormateur3());
        equipeFormation.setPrenomFormateur3(viewFiliereEquipeFormation.getPrenomFormateur3());
        equipeFormation.setTitreFormateur3(viewFiliereEquipeFormation.getTitreFormateur3());
        equipeFormation.setCodeFiliere(viewFiliereEquipeFormation.getCodeFiliere());
        
        //controle des données saisies
        if("".equals(filiere.getCodeFiliere())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code de la filière!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(filiere.getDesignation())){
            FacesMessage message = new FacesMessage("Veuillez saisir la designation de la filière!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(filiere.getCodeDomaine())){
            FacesMessage message = new FacesMessage("Veuillez selectionner le domaine de formation!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(equipeFormation.getNomFormateur1())){
            FacesMessage message = new FacesMessage("Veuillez saisir le nom du formateur 1!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(equipeFormation.getPrenomFormateur1())){
            FacesMessage message = new FacesMessage("Veuillez saisir le prénom du formateur 1!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(equipeFormation.getTitreFormateur1())){
            FacesMessage message = new FacesMessage("Veuillez saisir le titre du formateur 1!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(filiere.getCodeFiliere().matches(getCodeFiliereCache())){
                equipeFormation.setCodeEquipeForm(viewFiliereEquipeFormation.getCodeEquipeForm());
                
                modifier();               
                annuler();
                System.out.println("Données modifier");
            }else{                
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");                                                
            }    
        }   
        setBtnModifierDesactive(true);  
        setBtnSupprimerDesactive(true);           
    }
    
    //suppression d'un enregistrement
    public void supprimer(){
        try {  
            filiere.setCodeFiliere(viewFiliereEquipeFormation.getCodeFiliere());           
            
            if(null != filiere.getCodeFiliere() && !Objects.equals(filiere.getCodeFiliere(), "") && filiere.getCodeFiliere().equals(getCodeFiliereCache())){
                equipeFormation.setCodeEquipeForm(viewFiliereEquipeFormation.getCodeEquipeForm());
                
                equipeFormationFacadeL.remove(equipeFormation);
                filiereFacadeL.remove(filiere);                                
                annuler();
            }else{
                FacesMessage message = new FacesMessage( "Veuillez sélectionner l'enregistrement à supprimer!" );
                FacesContext.getCurrentInstance().addMessage(null, message);        
                setBtnModifierDesactive(true);  
                setBtnSupprimerDesactive(true);           
            }
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //methode de selection dans la datatable
    public void selectionner(){        
        setCodeFiliereCache(viewFiliereEquipeFormation.getCodeFiliere());
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
