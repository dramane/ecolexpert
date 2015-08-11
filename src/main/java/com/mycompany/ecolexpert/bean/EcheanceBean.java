/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoAcademiqueFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoEcheanceFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoAcademique;
import com.mycompany.ecolexpert.jpa.EcoEcheance;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;

/**
 *
 * @author HP
 */
@Named(value = "echeanceBean")
//@RequestScoped
@ViewScoped
public class EcheanceBean extends BoutonActiveBean implements Serializable{

    private EcoEcheance echeance;
    private List<EcoEcheance> listEcheance;
    private List listCodeEcheance;
    private List listCodeNiveau;
    
    //champ utilisé pour comparer le code du echeance
    private int codeEcheanceCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoNiveauFacadeLocal niveauFacadeL;
    @EJB private EcoEcheanceFacadeLocal  echeanceFacadeL;

    /**
     * Creates a new instance of echeanceBean
     */
    public EcheanceBean() {
        echeance = new EcoEcheance();       
    }

    public EcoEcheance getEcheance() {
        return echeance;
    }

    public void setEcheance(EcoEcheance echeance) {
        this.echeance = echeance;
    }   

    public int getCodeEcheanceCache() {
        return codeEcheanceCache;
    }

    public void setCodeEcheanceCache(int codeEcheanceCache) {
        this.codeEcheanceCache = codeEcheanceCache;
    }    

    public List getListCodeNiveau() {
        listCodeNiveau = new ArrayList();
        if((echeance.getCodeCycle()) != null && !"".equals(echeance.getCodeCycle())){   
            for(EcoNiveau niv : niveauFacadeL.findAll()){   
                if (echeance.getCodeCycle().equals(niv.getCodeCycle())) {
                    listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
                }              
            }
            return listCodeNiveau;
        }else return new ArrayList();

    }        

    public List<EcoEcheance> getListEcheance() {
        try{
            listEcheance = echeanceFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listEcheance;
    }
    
    public List getListCodeEcheance() {                
        try{  
            listCodeEcheance = new ArrayList();
            for(EcoEcheance echeanc : echeanceFacadeL.findAll()){
                listCodeEcheance.add(new SelectItem(echeanc.getIdEcheance(), echeanc.getCodeNiveau()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeEcheance;
    }
    

    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {
            echeance = new EcoEcheance();  
            setCodeEcheanceCache(0);
            setBtnValiderDesactive(false);
            setBtnModifierDesactive(true);  
            setBtnSupprimerDesactive(true);
            setChampLectureSeul(false);   
//            setChampCleNonAuto(false);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //initialisation et verrouillage des champs et initialisation des boutons
    public void annuler(){
        try {
//            cycle.setCodeCycle(null);
            setCodeEcheanceCache(0);
            echeance = new EcoEcheance();      
                                        
            setBtnModifierDesactive(true);
            setBtnSupprimerDesactive(true) ; 
            setBtnValiderDesactive(true);
            setChampLectureSeul(true);  
//            setChampCleNonAuto(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //creation d'un enregistrement
    public void creer(){
        try {
            //Enregistrement d'une écheance
            echeanceFacadeL.create(echeance); 
                                
        } catch (Exception e) {
            System.err.println("Erreur d'enregistrement capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == echeance.getIdEcheance()|| Objects.equals(echeance.getIdEcheance(), "")){            
                FacesMessage message = new FacesMessage( "Veuillez sélectionner l'enregistrement à modifier!" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                    
            }            
            setBtnModifierDesactive(true);  
            setBtnSupprimerDesactive(true);
            setBtnValiderDesactive(false);
            setChampLectureSeul(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //modification d' un utilisateur et de ses profils
    public void modifier(){
        try {
            //modification de l'utilisateur
            echeanceFacadeL.edit(echeance);          
        } catch (Exception e) {
            System.err.println("Erreur de modification capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(echeance.getAnneeaca())){
            FacesMessage message = new FacesMessage("Veuillez selectionner l'année académique!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(echeance.getCodeCycle())){
            FacesMessage message = new FacesMessage("Veuillez selectionner le cycle!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(echeance.getCodeNiveau())){
            FacesMessage message = new FacesMessage("Veuillez selectionner le niveau!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{                     
            
            //Vérifion si la somme des versements est égale au droit de formation saisi
            int som_versement = echeance.getVers1()+echeance.getVers2()+echeance.getVers3()+
                echeance.getVers4()+echeance.getVers5()+echeance.getVers6();
            if(echeance.getDrtformat()!= som_versement){
                FacesMessage message = new FacesMessage("La somme de tous les versements doit être égale au droit de formation! ");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message); 
            }else{
                //Enregistrement ou modification des données apres le controle de saisie
                if((0 != codeEcheanceCache) && (echeance.getIdEcheance()== codeEcheanceCache)){
                    modifier();
                    annuler();
                    System.out.println("Données modifiées");
                }else{ 

                    //Verification de l'existance de l'enregistrement a effectuer dans la BD
                    try {      
                        creer();  
                        annuler();  
                        System.out.println("Données Enregistrées");                                                 

                    }catch (Exception e) {
                        FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre! "+e );
                        message.setSeverity(FacesMessage.SEVERITY_ERROR);
                        FacesContext.getCurrentInstance().addMessage(null, message); 
                        echeance.setIdEcheance(null);  
                    }                 
                }
                
            }                        
        }   
        setBtnModifierDesactive(true);  
        setBtnSupprimerDesactive(true);           
    }
    
    //suppression d'un enregistrement
    public void supprimer(){
        try {                
            if((0 != codeEcheanceCache) && (echeance.getIdEcheance()== codeEcheanceCache)){
                echeanceFacadeL.remove(echeance);  
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
        
    //Action ajax lors de la selection du cycle
    public void onCycleChange() {
        listCodeNiveau = new ArrayList();
        if(echeance.getCodeCycle() != null && !"".equals(echeance.getCodeCycle())){            
            List<EcoNiveau> data ;
//            data = niveauFacadeL.findByCodeCycle("BTS");
            data = niveauFacadeL.findByCodeCycle(echeance.getCodeCycle());
            for(EcoNiveau niv : data){          
              listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
            }  
        }
        else listCodeNiveau = new ArrayList();
          
        echeance.setCodeNiveau("");           
    }
    
    //methode de selection dans la datatable
    public void selectionner(){ 
        codeEcheanceCache = echeance.getIdEcheance();
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
    }    
}
