/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoTypePaiementFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoTypePaiement;
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
@Named(value = "typePaiementBean")
@RequestScoped
public class TypePaiementBean extends BoutonActiveBean{
    private EcoTypePaiement typePaiement;   
    private List<EcoTypePaiement> listTypePaiement;  
    private List listCodeTypePaiement;    
    
    // Injection de notre EJB facade local (Session Bean Stateless)
    @EJB
    private EcoTypePaiementFacadeLocal typePaiementFacadeL;

    /**
     * Creates a new instance of TypePaiementBean
     */
    public TypePaiementBean() {
        typePaiement = new EcoTypePaiement();
    }

    public EcoTypePaiement getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(EcoTypePaiement typePaiement) {
        this.typePaiement = typePaiement;
    }
    
    //Liste des types de paiement
    public List<EcoTypePaiement> getListeTypePaiement(){   
        try{
              listTypePaiement = typePaiementFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listTypePaiement;
    }
    
    //Liste des types de paiement a afficher dans le combobox
    public List listCodeTypePaiement(){
        listCodeTypePaiement = new ArrayList();
        for(EcoTypePaiement typePaie : getListeTypePaiement()){          
          listCodeTypePaiement.add(new SelectItem(typePaie.getTypePaiement(), typePaie.getTypePaiement()));
        }
        return listCodeTypePaiement;        
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            typePaiement.setIdTypePaiement(null);
            typePaiement.setCodeTypePaiement(null);
            typePaiement.setTypePaiement(null);
            typePaiement.setActif(null);
            typePaiement.setDescription(null);
            
            setBtnModifierDesactive(true);
            setBtnSupprimerDesactive(true);
            setChampLectureSeul(false);   
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //initialisation et verrouillage des champs et initialisation des boutons
    public void annuler(){
        try {
            typePaiement.setIdTypePaiement(null);
            typePaiement.setCodeTypePaiement(null);
            typePaiement.setTypePaiement(null);
            typePaiement.setActif(null);
            typePaiement.setDescription(null);
                        
            setBtnModifierDesactive(true);
            setBtnSupprimerDesactive(true);
            setBtnValiderDesactive(true);
            setChampLectureSeul(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //creation d'un enregistrement
    public void creer(){
        try {
            typePaiementFacadeL.create(typePaiement);  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == typePaiement.getIdTypePaiement() || Objects.equals(typePaiement.getIdTypePaiement(), "")){            
                FacesMessage message = new FacesMessage( "Merci de sélectionner l'enregistrement à modifier!" );
                FacesContext.getCurrentInstance().addMessage(null, message);            
            }
            
            setBtnModifierDesactive(true);
            setBtnSupprimerDesactive(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //modification d'un enregistrement
    public void modifier(){
        try {
            if(null != typePaiement.getIdTypePaiement() && !Objects.equals(typePaiement.getIdTypePaiement(), "")){
                typePaiementFacadeL.edit(typePaiement);
            }
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(typePaiement.getTypePaiement())){
            FacesMessage message = new FacesMessage("Merci de saisir le type de paiement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(typePaiement.getCodeTypePaiement())){
            FacesMessage message = new FacesMessage("Merci de saisir le code du type de paiement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(typePaiement.getDescription())){
            FacesMessage message = new FacesMessage("Merci de saisir la description du type de paiement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(null != typePaiement.getIdTypePaiement() && !Objects.equals(typePaiement.getIdTypePaiement(), "")){
                modifier();
                System.out.println("Données modifier");
            }else {
                creer();
                System.out.println("Données enregistrée");
            }  
            annuler();
        }   
        setBtnModifierDesactive(true);  
        setBtnSupprimerDesactive(true);
    }
    
    //suppression d'un enregistrement
    public void supprimer(){
        try {                
            if(null != typePaiement.getIdTypePaiement() && !Objects.equals(typePaiement.getIdTypePaiement(), "")){
                typePaiementFacadeL.remove(typePaiement);
                annuler();
            }else{
                FacesMessage message = new FacesMessage( "Merci de sélectionner l'enregistrement à supprimer!" );
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
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
    }
}
