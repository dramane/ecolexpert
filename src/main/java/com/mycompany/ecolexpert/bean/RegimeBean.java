/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoRegimeFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoRegime;
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
 * @author HP
 */
@Named(value = "regimeBean")
@RequestScoped
public class RegimeBean extends BoutonActiveBean{
    private EcoRegime regime;
    private List<EcoRegime> listRegime;
    private List listCodeRegime; 
    
    //champ utilisé pour comparer le code du regime
    private String codeRegimeCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoRegimeFacadeLocal  regimeFacadeL;

    /**
     * Creates a new instance of RegimeBean
     */
    public RegimeBean() {
        regime = new EcoRegime();
    }
    
    //getter et setter du regime
    public EcoRegime getRegime() {
        return regime;
    }

    public void setRegime(EcoRegime regime) {
        this.regime = regime;
    }
    
    //getter et setter du codeRegimeCache
    public String getCodeRegimeCache() {
        return codeRegimeCache;
    }

    public void setCodeRegimeCache(String codeRegimeCache) {
        this.codeRegimeCache = codeRegimeCache;
    }
    
    //Liste des regimes a afficher dans le combobox
    public List getListCodeRegime() {
        try{  
            listCodeRegime = new ArrayList();
            for(EcoRegime regim : regimeFacadeL.findAll()){
                listCodeRegime.add(new SelectItem(regim.getCodeRegime(), regim.getRegime()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeRegime;
    }
    
    //liste de tous les regimes
    public List<EcoRegime> getListRegime(){  
        try{
            listRegime = regimeFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listRegime;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {                        
            regime.setCodeRegime(null);
            setCodeRegimeCache(null);
            regime.setRegime(null);
            regime.setDescription(null);
            
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
            regime.setCodeRegime(null);
            setCodeRegimeCache(null);
            regime.setRegime(null);
            regime.setDescription(null);
            
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
            regimeFacadeL.create(regime);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == getCodeRegimeCache() || Objects.equals(getCodeRegimeCache(), "")){            
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
            regimeFacadeL.edit(regime);  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(regime.getCodeRegime())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code du régime!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(regime.getRegime())){
            FacesMessage message = new FacesMessage("Veuillez saisir le nom du régime!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(regime.getCodeRegime().matches(getCodeRegimeCache())){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (regimeFacadeL.find(regime.getCodeRegime())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    regime.setCodeRegime(null);                    
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
            if(null != getCodeRegimeCache() && !Objects.equals(getCodeRegimeCache(), "")){
                regimeFacadeL.remove(regime);  
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
        setCodeRegimeCache(regime.getCodeRegime());
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
