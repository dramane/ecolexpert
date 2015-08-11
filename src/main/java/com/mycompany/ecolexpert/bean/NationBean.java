/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoNationFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoNation;
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
 * @author TOSHIBA
 */
@Named(value = "nationBean")
@RequestScoped
public class NationBean extends BoutonActiveBean{

    private EcoNation nation;   
    private List<EcoNation> listNation;  
     
    private String pays;
    private List listPays = null; 
    private String nationalite;
    private boolean btnModifierDesactive;
    private boolean btnSupprimerDesactive;
    private boolean btnValiderDesactive;
    private boolean champLectureSeul;
 
    // Injection de notre EJB facade local (Session Bean Stateless)
    @EJB
    private EcoNationFacadeLocal nationFacadeL;

    /**
     * Creates a new instance of NationBean
     * @return 
     */
        
    /**
     * Creates a new instance of NationBean
     */
    public NationBean() {
        nation = new EcoNation();
    }
    
    //getter et setter EcoNation
    public EcoNation getNation() {
        return nation;
    }

    public void setNation(EcoNation nation) {
        this.nation = nation;
    }                 
    
    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }  
    
    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
    
    //Liste des nation
    public List<EcoNation> getListeNation(){   
        try{
              listNation = nationFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listNation;
    }
      
    public List getListPays(){
        listPays = new ArrayList();
        for(EcoNation nat : getListeNation()){          
          listPays.add(new SelectItem(nat.getIdnation(), nat.getNomFr()));
        }
        return listPays;        
    } 
    
    public List getListPays2(){
        listPays = new ArrayList();
        for(EcoNation nat : getListeNation()){          
          listPays.add(new SelectItem(nat.getNomFr(), nat.getNomFr()));
        }
        return listPays;        
    } 
    
    public void valueChanged() {
       if(pays !=null && !pays.equals(""))
         nation = nationFacadeL.find(Integer.parseInt(pays));
         nationalite = nation.getNationalite();                                       
    }   
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            nation.setIdnation(null);            
            nation.setCapitale(null);
            nation.setCode(null);
            nation.setCode2(null);            
            nation.setIndicatif(null);
            nation.setNationalite(null);
            nation.setNomFr(null);
            nation.setNomUs(null);
            nation.setActif(null);
            nation.setContinent(null);
            nation.setZones(null);
            
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
            nation.setIdnation(null);            
            nation.setCapitale(null);
            nation.setCode(null);
            nation.setCode2(null);           
            nation.setIndicatif(null);
            nation.setNationalite(null);
            nation.setNomFr(null);
            nation.setNomUs(null);
            nation.setActif(null);
            nation.setContinent(null);
            nation.setZones(null);
                        
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
            nationFacadeL.create(nation);  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == nation.getIdnation() || Objects.equals(nation.getIdnation(), "")){            
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
            if(null != nation.getIdnation() && !Objects.equals(nation.getIdnation(), "")){
                nationFacadeL.edit(nation);
            }
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(nation.getNomFr())){
            FacesMessage message = new FacesMessage("Merci de saisir le nom du pays!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(nation.getCapitale())){
            FacesMessage message = new FacesMessage("Merci de saisir la capitale du pays!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(nation.getNationalite())){
            FacesMessage message = new FacesMessage("Merci de saisir la nationalité!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(nation.getContinent())){
            FacesMessage message = new FacesMessage("Merci de saisir le continent du pays!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(nation.getIndicatif())){
            FacesMessage message = new FacesMessage("Merci de saisir l'indicatif du pays!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(nation.getCode())){
            FacesMessage message = new FacesMessage("Merci de saisir le code à 3 lettre!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(nation.getCode2())){
            FacesMessage message = new FacesMessage("Merci de saisir le code à 2 lettre!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(null != nation.getIdnation() && !Objects.equals(nation.getIdnation(), "")){
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
            if(null != nation.getIdnation()&& !Objects.equals(nation.getIdnation(), "")){
                nationFacadeL.remove(nation);
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
