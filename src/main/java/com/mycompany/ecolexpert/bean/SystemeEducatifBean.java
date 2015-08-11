/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoSystemeEducatifFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoSystemeEducatif;
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
@Named(value = "systemeEducatifBean")
@RequestScoped
public class SystemeEducatifBean extends BoutonActiveBean{
    private EcoSystemeEducatif systemeEducatif;
    private List<EcoSystemeEducatif> listSystemeEducatif;  
    private List listCodeSystEduc;
    
    //champ utilisé pour comparer le code formation
    private String codeEducatifCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoSystemeEducatifFacadeLocal systemeEducatifL;

    /**
     * Creates a new instance of SystemeEducatifBean
     */
    public SystemeEducatifBean() {
        systemeEducatif = new EcoSystemeEducatif();
    }
    
    // gettter et setter du systeme Educatif
    public EcoSystemeEducatif getSystemeEducatif() {
        return systemeEducatif;
    }

    public void setSystemeEducatif(EcoSystemeEducatif systemeEducatif) {
        this.systemeEducatif = systemeEducatif;
    }

    // gettter et setter codeEducatifCache
    public String getCodeEducatifCache() {
        return codeEducatifCache;
    }

    public void setCodeEducatifCache(String codeEducatifCache) {
        this.codeEducatifCache = codeEducatifCache;
    }
    
    //liste de tout les systemes educatifs
    public List<EcoSystemeEducatif> getListSystEducatif(){  
        try{
                  listSystemeEducatif = systemeEducatifL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listSystemeEducatif;
    }
    
    //Liste des systemes educatifs a afficher dans le combobox
    public List getListCodeSystEduc() {
        listCodeSystEduc = new ArrayList();
        for(EcoSystemeEducatif systEduc : getListSystEducatif()){          
          listCodeSystEduc.add(new SelectItem(systEduc.getCodeEducatif(), systEduc.getLibelle()));
        }
        return listCodeSystEduc;
    }    
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            systemeEducatif.setCodeEducatif(null);
            setCodeEducatifCache(null);
            systemeEducatif.setLibelle(null);
            systemeEducatif.setDescription(null);
            systemeEducatif.setActif(null);           
            
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
            systemeEducatif.setCodeEducatif(null);
            setCodeEducatifCache(null);
            systemeEducatif.setLibelle(null);
            systemeEducatif.setDescription(null);
            systemeEducatif.setActif(null);           
                                        
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
            systemeEducatifL.create(systemeEducatif);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == systemeEducatif.getCodeEducatif() || Objects.equals(systemeEducatif.getCodeEducatif(), "")){            
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
    
    //modification d' un utilisateur et de ses profils
    public void modifier(){
        try {
            //modification de l'utilisateur
            systemeEducatifL.edit(systemeEducatif);                                   
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(systemeEducatif.getCodeEducatif())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code du système éducatif!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(systemeEducatif.getLibelle())){
            FacesMessage message = new FacesMessage("Veuillez saisir le libellé du système éducatif!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(systemeEducatif.getDescription())){
            FacesMessage message = new FacesMessage("Veuillez saisir la description du système éducatif!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(systemeEducatif.getCodeEducatif().matches(getCodeEducatifCache())){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (systemeEducatifL.find(systemeEducatif.getCodeEducatif())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    systemeEducatif.setCodeEducatif(null);                    
                    System.out.println("Cet enregistrement existe déjà");
                }
                                
            }    
        }   
        setBtnModifierDesactive(true);  
        setBtnSupprimerDesactive(true);           
    }
    
    //suppression d'un enregistrement
    public void supprimer(){
        try {                
            if(null != systemeEducatif.getCodeEducatif()&& !Objects.equals(systemeEducatif.getCodeEducatif(), "")){
                systemeEducatifL.remove(systemeEducatif);  
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
        setCodeEducatifCache(systemeEducatif.getCodeEducatif());
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
