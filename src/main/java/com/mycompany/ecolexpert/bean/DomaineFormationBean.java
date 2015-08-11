/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;


import com.mycompany.ecolexpert.ejb.EcoDomaineFormationFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoDomaineFormation;
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
 * @author toshiba
 */
@Named(value = "domaineFormationBean")
@RequestScoped
public class DomaineFormationBean extends BoutonActiveBean{
    private EcoDomaineFormation domaine; 
    private List<EcoDomaineFormation> listDomaineFormation;
    private List listCodeDomaine; 
    
    //champ utilisé pour comparer le code du DomaineFormation
    private String codeDomaineFormationCache;
    
    @EJB EcoDomaineFormationFacadeLocal domaineFacadeL;

    /**
     * Creates a new instance of DomaineFormationBean
     */
    public DomaineFormationBean() {
        domaine = new EcoDomaineFormation();
    }        
        
    public EcoDomaineFormation getDomaine() {
        return domaine;
    }

    public void setDomaine(EcoDomaineFormation domaine) {
        this.domaine = domaine;
    }

        //getter et setter codeDomaineFormationCache;
    public String getCodeDomaineFormationCache() {
        return codeDomaineFormationCache;
    }

    public void setCodeDomaineFormationCache(String codeDomaineFormationCache) {
        this.codeDomaineFormationCache = codeDomaineFormationCache;
    }    
    
    //Liste des domaine formation a afficher dans le champ select
    public List getListCodeDomaine(){   
        try{  
            listCodeDomaine = new ArrayList();
            for(EcoDomaineFormation dom : domaineFacadeL.findAll()){          
              listCodeDomaine.add(new SelectItem(dom.getCodeDomaine(), dom.getLibelleDomaine()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeDomaine;
    }
    
    //liste de tous les domaines de formation
    public List<EcoDomaineFormation> getListDomaineFormation() {
         try{
            listDomaineFormation = domaineFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listDomaineFormation;
    }
    
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            domaine.setCodeDomaine(null);
            domaine.setLibelleDomaine(null);
            setCodeDomaineFormationCache(null);
            
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
            domaine.setCodeDomaine(null);
            domaine.setLibelleDomaine(null);
            setCodeDomaineFormationCache(null);
            
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
            domaineFacadeL.create(domaine);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == domaine.getCodeDomaine()|| Objects.equals(domaine.getCodeDomaine(), "")){            
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
            domaineFacadeL.edit(domaine);          
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(domaine.getCodeDomaine())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code du domaine de formation!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(domaine.getLibelleDomaine())){
            FacesMessage message = new FacesMessage("Veuillez saisir le libellé du domaine de formation!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);  
            setChampCleNonAuto(true);
        }else{           
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(domaine.getCodeDomaine().matches(codeDomaineFormationCache)){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (domaineFacadeL.find(domaine.getCodeDomaine())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    domaine.setCodeDomaine(null);                    
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
            if(null != domaine.getCodeDomaine()&& !Objects.equals(domaine.getCodeDomaine(), "")){
                domaineFacadeL.remove(domaine);  
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
        codeDomaineFormationCache = domaine.getCodeDomaine();
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
