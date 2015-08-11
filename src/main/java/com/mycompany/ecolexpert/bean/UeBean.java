/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoUeFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoUe;
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
@Named(value = "ueBean")
@RequestScoped
public class UeBean extends BoutonActiveBean{
    private EcoUe ue;
    private List<EcoUe> listUe;
    private List listCodeUe; 
    
    //champ utilisé pour comparer le code du filière
    private String codeUeCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoUeFacadeLocal ueFacadeL;

    /**
     * Creates a new instance of UeBean
     */
    public UeBean() {
        ue = new EcoUe();
    }
    
    //getter et setter UE
    public EcoUe getUe() {
        return ue;
    }

    public void setUe(EcoUe ue) {
        this.ue = ue;
    }
   
    //getter et setter codeUeCache
    public String getCodeUeCache() {
        return codeUeCache;
    }

    public void setCodeUeCache(String codeUeCache) {
        this.codeUeCache = codeUeCache;
    }    
            
    //liste de tous les UE
    public List<EcoUe> getListUe() {
        try{
            listUe = ueFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listUe;
    }

    //liste des CodeUe a afficher dans le combobox
    public List getListCodeUe() {
         try{  
            listCodeUe = new ArrayList();
            for(EcoUe uniteE : ueFacadeL.findAll()){          
              listCodeUe.add(new SelectItem(uniteE.getLibelleUe(), uniteE.getLibelleUe()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeUe;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau() {
        try {            
            ue.setCodeUe(null);
            ue.setLibelleUe(null);
            ue.setDescriptionUe(null);
                                    
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
            ue.setCodeUe(null);
            ue.setLibelleUe(null);
            ue.setDescriptionUe(null);
            setCodeUeCache(null);
            
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
            ueFacadeL.create(ue);    
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == ue.getCodeUe() || Objects.equals(ue.getCodeUe(), "")){            
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
            ueFacadeL.edit(ue);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(ue.getCodeUe())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code de l'unité d'enseignement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(ue.getLibelleUe())){
            FacesMessage message = new FacesMessage("Veuillez saisir la libellé de l'unité d'enseignement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(ue.getCodeUe().matches(getCodeUeCache())){                
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
            if(null != ue.getCodeUe() && !Objects.equals(ue.getCodeUe(), "") && ue.getCodeUe().equals(getCodeUeCache())){
                ueFacadeL.remove(ue);                                
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
        setCodeUeCache(ue.getCodeUe());
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
