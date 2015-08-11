/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoSystemeAcademiqueFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoSystemeAcademique;
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
@Named(value = "systemeAcademiqueBean")
@RequestScoped
public class SystemeAcademiqueBean extends BoutonActiveBean{
    private EcoSystemeAcademique systemeAcademiq;
    private List<EcoSystemeAcademique> listSystemeAcademiq; 
    private List listCodeSystemeAcademiq;    
    
    //champ utilisé pour comparer le code formation
    private String codeAcademiqueCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoSystemeAcademiqueFacadeLocal systemeAcademiqL;

    /**
     * Creates a new instance of SystemeAcademiqueBean
     */
    public SystemeAcademiqueBean() {
        systemeAcademiq = new  EcoSystemeAcademique();
    }
    
    // gettter et setter du systeme academique
    public EcoSystemeAcademique getSystemeAcademiq() {
        return systemeAcademiq;
    }

    public void setSystemeAcademiq(EcoSystemeAcademique systemeAcademiq) {
        this.systemeAcademiq = systemeAcademiq;
    }  
    
    // gettter et setter codeAcademiqueCache
    public String getCodeAcademiqueCache() {
        return codeAcademiqueCache;
    }

    public void setCodeAcademiqueCache(String codeAcademiqueCache) {
        this.codeAcademiqueCache = codeAcademiqueCache;
    }    
    
    //Liste des codes systeme academique a afficher dans le combobox
    public List getListCodeSystemeAcademiq(){   
        try{  
            listCodeSystemeAcademiq = new ArrayList();
            
            for (EcoSystemeAcademique syst_acad : systemeAcademiqL.findAll()){
                listCodeSystemeAcademiq.add(new SelectItem(syst_acad.getCodeAcademique(), syst_acad.getDesignation()));
            }
           
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeSystemeAcademiq;
    }
    
    //liste de tout les systemes academiques
    public List<EcoSystemeAcademique> getListSystemeAcademiq(){  
        try{
            listSystemeAcademiq = systemeAcademiqL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listSystemeAcademiq;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            systemeAcademiq.setCodeAcademique(null);
            setCodeAcademiqueCache(null);
            systemeAcademiq.setDesignation(null);
            systemeAcademiq.setDescription(null);              
            
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
            systemeAcademiq.setCodeAcademique(null);
            setCodeAcademiqueCache(null);
            systemeAcademiq.setDesignation(null);
            systemeAcademiq.setDescription(null);           
                                        
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
            //Enregistrement d'un systemeAcademiq           
            systemeAcademiqL.create(systemeAcademiq);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == systemeAcademiq.getCodeAcademique() || Objects.equals(systemeAcademiq.getCodeAcademique(), "")){            
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
            systemeAcademiqL.edit(systemeAcademiq);                                        
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(systemeAcademiq.getCodeAcademique())){
            FacesMessage message = new FacesMessage("Veuillez saisir le le code du système académique!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(systemeAcademiq.getDesignation())){
            FacesMessage message = new FacesMessage("Veuillez saisir la designation du système académique!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(systemeAcademiq.getCodeAcademique().matches(getCodeAcademiqueCache())){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (systemeAcademiqL.find(systemeAcademiq.getCodeAcademique())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    systemeAcademiq.setCodeAcademique(null);                    
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
            if(null != systemeAcademiq.getCodeAcademique()&& !Objects.equals(systemeAcademiq.getCodeAcademique(), "")){
                systemeAcademiqL.remove(systemeAcademiq);  
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
        setCodeAcademiqueCache(systemeAcademiq.getCodeAcademique());
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
