/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoBatimentFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoBatiment;
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
@Named(value = "batimentBean")
@RequestScoped
public class BatimentBean extends BoutonActiveBean{
    private EcoBatiment batiment;
    private List<EcoBatiment> listBatiment;
    private List listCodeBatiment;
    
    //champ utilisé pour comparer le code du batiment
    private String codeBatimentCache;
    
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoBatimentFacadeLocal batimentFacadeL;

    /**
     * Creates a new instance of BatimentBean
     */
    public BatimentBean() {
        batiment = new EcoBatiment();
    }

    //getter et setter filiere
    public EcoBatiment getBatiment() {
        return batiment;
    }

    public void setBatiment(EcoBatiment batiment) {
        this.batiment = batiment;
    }
    
    //getter et setter codeBatimentCache
    public String getCodeBatimentCache(){    
        return codeBatimentCache;
    }

    public void setCodeBatimentCache(String codeBatimentCache) {
        this.codeBatimentCache = codeBatimentCache;
    }

    //Liste des batiment a afficher dans le combobox
    public List getListCodeBatiment() {
        try{  
            listCodeBatiment = new ArrayList();
            for(EcoBatiment bat : batimentFacadeL.findAll()){
                listCodeBatiment.add(new SelectItem(bat.getCodeBatiment(), bat.getNomBatiment()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeBatiment;
    }
    
    //liste de tous les batiment
    public List<EcoBatiment> getListBatiment(){  
        try{
            listBatiment = batimentFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listBatiment;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            batiment.setCodeBatiment(null);
            setCodeBatimentCache(null);
            batiment.setNomBatiment(null);
            batiment.setDescription(null);
            
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
            batiment.setCodeBatiment(null);
            setCodeBatimentCache(null);
            batiment.setNomBatiment(null);
            batiment.setDescription(null);
            
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
            batimentFacadeL.create(batiment);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == batiment.getCodeBatiment()|| Objects.equals(batiment.getCodeBatiment(), "")){            
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
            batimentFacadeL.edit(batiment);          
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(batiment.getCodeBatiment())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code du batiment!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(batiment.getNomBatiment())){
            FacesMessage message = new FacesMessage("Veuillez saisir le nom du batiment!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{           
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(batiment.getCodeBatiment().matches(codeBatimentCache)){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (batimentFacadeL.find(batiment.getCodeBatiment())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    batiment.setCodeBatiment(null);                    
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
            if(null != batiment.getCodeBatiment()&& !Objects.equals(batiment.getCodeBatiment(), "")){
                batimentFacadeL.remove(batiment);  
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
        codeBatimentCache = batiment.getCodeBatiment();
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
