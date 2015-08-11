/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoSpecialiteFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoSpecialite;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author HP
 */
@Named(value = "specialiteBean")
@RequestScoped
public class SpecialiteBean extends BoutonActiveBean{
    private EcoSpecialite specialite;
    private List<EcoSpecialite> listSpecialite;
    private List listCodeSpecialite; 
    
    //champ utilisé pour comparer le code du specialité
    private String codeSpecialiteCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoSpecialiteFacadeLocal  specialiteFacadeL;

    /**
     * Creates a new instance of SpecialiteBean
     */
    public SpecialiteBean() {
        specialite = new EcoSpecialite();
    }
    
    //Getter et setter de la specialite
    public EcoSpecialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(EcoSpecialite specialite) {
        this.specialite = specialite;
    }   
    
    //Getter et setter de la codeSpecialiteCache
    public String getCodeSpecialiteCache() {
        return codeSpecialiteCache;
    }

    public void setCodeSpecialiteCache(String codeSpecialiteCache) {
        this.codeSpecialiteCache = codeSpecialiteCache;
    }
    
    //Getter de Liste de toutes les specialités
    public List<EcoSpecialite> getListSpecialite() {
        try{
            listSpecialite = specialiteFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listSpecialite;
    }

    //Getter de codeListSpecialite a aficher dans le combobox
    public List getListCodeSpecialite() {
        try{  
            listCodeSpecialite = new ArrayList();
            for(EcoSpecialite special : specialiteFacadeL.findAll()){          
              listCodeSpecialite.add(new SelectItem(special.getCodeSpecialite(), special.getLibelleSpecialite()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeSpecialite;
    }
    
    
    
    //preparation d'un nouvel enregistrement
    public void nouveau() {
        try {            
            specialite.setCodeSpecialite(null);
            specialite.setLibelleSpecialite(null);
            specialite.setDescriptionSpecialite(null);
            specialite.setCodeFiliere(null);
            setCodeSpecialiteCache(null);
                        
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
            specialite.setCodeSpecialite(null);
            specialite.setLibelleSpecialite(null);
            specialite.setDescriptionSpecialite(null);
            specialite.setCodeFiliere(null);
            setCodeSpecialiteCache(null);
            
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
            //Enregistrement d'une specialité
            specialiteFacadeL.create(specialite);    
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == specialite.getCodeSpecialite() || Objects.equals(specialite.getCodeSpecialite(), "")){            
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
            //modification 'une specialité
            specialiteFacadeL.edit(specialite);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
   
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(specialite.getCodeSpecialite())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code de la spécialité!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(specialite.getLibelleSpecialite())){
            FacesMessage message = new FacesMessage("Veuillez saisir la libellé de la spécialité!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(specialite.getCodeFiliere())){
            FacesMessage message = new FacesMessage("Veuillez selectionner la filière!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(specialite.getCodeSpecialite().matches(getCodeSpecialiteCache())){                
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
            if(null != specialite.getCodeSpecialite() && !Objects.equals(specialite.getCodeSpecialite(), "") && specialite.getCodeSpecialite().equals(getCodeSpecialiteCache())){
                specialiteFacadeL.remove(specialite);                                
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
        setCodeSpecialiteCache(specialite.getCodeSpecialite());
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
