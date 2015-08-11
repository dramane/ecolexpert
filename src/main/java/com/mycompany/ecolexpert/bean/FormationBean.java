/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;


import com.mycompany.ecolexpert.ejb.EcoFormationFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoFormation;
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
 * @author toshiba
 */
@Named(value = "formationBean")
@RequestScoped

//Cette classe concerne la table formation (cours du jour ou soir)
public class FormationBean extends BoutonActiveBean{
    private EcoFormation formation;
    private List<EcoFormation> listFormation;
    private List listCodeFormation; 
    
    //champ utilisé pour comparer le code formation
    private String codeFormationCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoFormationFacadeLocal formationFacadeL;
    
    /**
     * Creates a new instance of FormationBean
     */
    public FormationBean() {
        formation = new EcoFormation();
    }
    
    //getter et setter Formation
    public EcoFormation getFormation() {
        return formation;
    }

    public void setFormation(EcoFormation formation) {
        this.formation = formation;
    }
    
    //getter et setter codeFormationCache
    public String getCodeFormationCache() {
        return codeFormationCache;
    }

    public void setCodeFormationCache(String codeFormationCache) {
        this.codeFormationCache = codeFormationCache;
    }    
    
    //Liste des formations a afficher dans le combobox
    public List getListCodeFormation(){   
        try{  
            listCodeFormation = new ArrayList();
            for(EcoFormation forma : formationFacadeL.findAll()){          
              listCodeFormation.add(new SelectItem(forma.getCodeFormation(), forma.getFormation()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeFormation;
    }
    
    //liste de toutes les formation
    public List<EcoFormation> getListFormation(){  
        try{
            listFormation = formationFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listFormation;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            formation.setFormation(null);
            formation.setDescription(null);
            formation.setCodeFormation(null); 
            setCodeFormationCache(null);
            
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
            formation.setFormation(null);
            formation.setDescription(null);
            formation.setCodeFormation(null); 
            setCodeFormationCache(null);
                                        
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
            formationFacadeL.create(formation);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == formation.getCodeFormation() || Objects.equals(formation.getCodeFormation(), "")){            
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
            formationFacadeL.edit(formation);                                            
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(formation.getFormation())){
            FacesMessage message = new FacesMessage("Veuillez saisir la formation!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if(formation.getCodeFormation() == null){
            FacesMessage message = new FacesMessage("Veuillez saisir le code de la formation!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(formation.getCodeFormation().matches(getCodeFormationCache())){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (formationFacadeL.find(formation.getCodeFormation())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    formation.setCodeFormation(null);                    
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
            if(null != formation.getCodeFormation()&& !Objects.equals(formation.getCodeFormation(), "")){
                formationFacadeL.remove(formation);  
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
        setCodeFormationCache(formation.getCodeFormation());
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
