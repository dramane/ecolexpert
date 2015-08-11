/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoBanqueFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoBanque;
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
@Named(value = "banqueBean")
@RequestScoped
public class BanqueBean extends BoutonActiveBean{
    private EcoBanque banque;   
    private List<EcoBanque> listBanque;  
    private List listSigleBanq;
    
    // Injection de notre EJB facade local (Session Bean Stateless)
    @EJB
    private EcoBanqueFacadeLocal banqueFacadeL;
    
    /**
     * Creates a new instance of BanqueBean
     */
    public BanqueBean() {
        banque = new EcoBanque();
    }

    //getter et setter de banque
    public EcoBanque getBanque() {
        return banque;
    }

    public void setBanque(EcoBanque banque) {
        this.banque = banque;
    }    
    
    //Liste des banques
    public List<EcoBanque> getListeBanque(){   
        try{
              listBanque = banqueFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listBanque;
    }
    
    //Liste des banques a afficher dans le combobox
    public List getlistSigleBanq(){
        listSigleBanq = new ArrayList();
        for(EcoBanque banq : getListeBanque()){          
          listSigleBanq.add(new SelectItem(banq.getSigleBq(), banq.getSigleBq()));
        }
        return listSigleBanq;        
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            banque.setIdbanque(null);
            banque.setCdBq(null);
            banque.setSigleBq(null);
            banque.setBanque(null);
            banque.setAdresseBq(null);
            banque.setEmailBq(null);
            banque.setFaxBq(null);
            banque.setPhoneBq(null);
            banque.setPostalBq(null);            
            banque.setTelex(null);
            banque.setWebSite(null);
                        
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
            banque.setIdbanque(null);
            banque.setCdBq(null);
            banque.setSigleBq(null);
            banque.setBanque(null);
            banque.setAdresseBq(null);
            banque.setEmailBq(null);
            banque.setFaxBq(null);
            banque.setPhoneBq(null);
            banque.setPostalBq(null);            
            banque.setTelex(null);
            banque.setWebSite(null);
                        
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
            banqueFacadeL.create(banque);  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == banque.getIdbanque()|| Objects.equals(banque.getIdbanque(), "")){            
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
            banqueFacadeL.edit(banque);            
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(banque.getCdBq())){
            FacesMessage message = new FacesMessage("Merci de saisir le code de la banque!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(banque.getBanque())){
            FacesMessage message = new FacesMessage("Merci de saisir le libellé de la banque!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(banque.getSigleBq())){
            FacesMessage message = new FacesMessage("Merci de saisir le sigle de la banque!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(banque.getPostalBq())){
            FacesMessage message = new FacesMessage("Merci de saisir l'adresse postale de la banque!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(banque.getPhoneBq())){
            FacesMessage message = new FacesMessage("Merci de saisir le numéro de téléphone de la banque!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(null != banque.getIdbanque()&& !Objects.equals(banque.getIdbanque(), "")){
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
            if(null != banque.getIdbanque() && !Objects.equals(banque.getIdbanque(), "")){
                banqueFacadeL.remove(banque);  
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
