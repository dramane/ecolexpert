/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoSalleFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoSalle;
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
@Named(value = "salleBean")
@RequestScoped
public class SalleBean extends BoutonActiveBean{
    private EcoSalle salle;
    private List<EcoSalle> listSalle;
    private List listCodeSalle; 
    
    //champ utilisé pour comparer le code de la classe
    private String codeSalleCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoSalleFacadeLocal  salleFacadeL;

    /**
     * Creates a new instance of SalleBean
     */
    public SalleBean() {
        salle = new EcoSalle();        
    }
 
    //getter et setter salle
    public EcoSalle getSalle() {
        return salle;
    }

    public void setSalle(EcoSalle salle) {
        this.salle = salle;
    }    

    //getter et setter codeSalleCache
    public String getCodeSalleCache(){    
        return codeSalleCache;
    }

    public void setCodeSalleCache(String codeSalleCache) {
        this.codeSalleCache = codeSalleCache;
    }

    //Liste des salle a afficher dans le combobox
    public List getListCodeSalle() {
        try{  
            listCodeSalle = new ArrayList();
            for(EcoSalle sall : salleFacadeL.findAll()){
                listCodeSalle.add(new SelectItem(sall.getNomSalle(), sall.getNomSalle()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeSalle;
    }
    
    //liste de toutes les salle
    public List<EcoSalle> getListSalle(){  
        try{
            listSalle = salleFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listSalle;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {                        
            salle.setNomSalle(null);
            setCodeSalleCache(null);
            salle.setCapacite(null);
//            salle.setDateEnregistrement(null);
            salle.setCodeBatiment(null);
            
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
            salle.setNomSalle(null);
            setCodeSalleCache(null);
            salle.setCapacite(null);
//            salle.setDateEnregistrement(null);
            salle.setCodeBatiment(null);
            
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
            //Enregistrement d'une salle
            salleFacadeL.create(salle);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == salle.getNomSalle() || Objects.equals(salle.getNomSalle(), "")){            
                FacesMessage message = new FacesMessage( "Veuillez sélectionner l'enregistrement à modifier!" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);            
            }            
            setBtnModifierDesactive(true);  
            setBtnSupprimerDesactive(true);
            setChampCleNonAuto(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //modification d'une salle
    public void modifier(){
        try {
            //modification de la salle
            salleFacadeL.edit(salle);  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(salle.getNomSalle())){
            FacesMessage message = new FacesMessage("Veuillez saisir le nom de la salle!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if(salle.getCapacite() == null){
            FacesMessage message = new FacesMessage("Veuillez saisir la capacité d'accueil de la salle!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(salle.getCodeBatiment())){
            FacesMessage message = new FacesMessage("Veuillez selectionner le bâtiment abritant la salle!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(salle.getNomSalle().matches(codeSalleCache)){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (salleFacadeL.find(salle.getNomSalle())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    salle.setNomSalle(null);                    
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
            if(null != salle.getNomSalle() && !Objects.equals(salle.getNomSalle(), "")){
                salleFacadeL.remove(salle);  
                annuler();
            }else{
                FacesMessage message = new FacesMessage( "Veuillez sélectionner l'enregistrement à supprimer!" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
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
        codeSalleCache = salle.getNomSalle();
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
    
}
