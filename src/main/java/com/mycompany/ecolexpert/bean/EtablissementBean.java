/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoMyinfoFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoMyinfo;
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
@Named(value = "etablissementBean")
@RequestScoped
public class EtablissementBean extends BoutonActiveBean{
    private EcoMyinfo myInfo;
    private List<EcoMyinfo> listMyInfo;
    private List listCodeMyInfo;  
    
//    private String etablissementEnCours;
    
    //champ utilisé pour comparer le code de l'établissement
    private String codeMyInfoCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoMyinfoFacadeLocal myInfoFacadeL;

    /**
     * Creates a new instance of EtablissementBean
     */
    public EtablissementBean() {
        myInfo = new EcoMyinfo();    
//        super.setBtnValiderDesactive(true);
//        super.setBtnSupprimerDesactive(true);
//        super.setChampCleNonAuto(true);
//        super.setChampLectureSeul(true);
    }
    
    //getter et setter myInfo
    public EcoMyinfo getMyInfo() {       
//        return myInfoFacadeL.findMyInfo_etablissementEnCours();  
        return myInfo;
    }

    public void setMyInfo(EcoMyinfo myInfo) {
        this.myInfo = myInfo;
    }  
    
    //getter et setter codeMyInfoCaché
    public String getCodeMyInfoCache() {
        return codeMyInfoCache;
    }

    public void setCodeMyInfoCache(String codeMyInfoCache) {
        this.codeMyInfoCache = codeMyInfoCache;
    }    
          
    //liste de tout les etablissements (myfinfo)
    public List<EcoMyinfo> getListMyInfo() {
        try{
            listMyInfo = myInfoFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listMyInfo;
    }

    //Liste des etablissements (myfinfo) a afficher dans le combobox 
    public List getListCodeMyInfo() {
        try{  
            listCodeMyInfo = new ArrayList();
            for(EcoMyinfo myInfo1 : myInfoFacadeL.findAll()){
                listCodeMyInfo.add(new SelectItem(myInfo1.getIdmyinfo(), myInfo1.getMyname()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeMyInfo;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {                        
            myInfo = null;
            setCodeMyInfoCache(null);  
            
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
            myInfo = null;
            setCodeMyInfoCache(null);                        
            
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
            myInfoFacadeL.create(myInfo);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == myInfo.getCodeMyinfo() || Objects.equals(myInfo.getCodeMyinfo(), "")){            
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
    
    //modification d'un établissement
    public void modifier(){
        try {
            //modification de l'établissement
            myInfoFacadeL.edit(myInfo);  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(myInfo.getCodeMyinfo())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code de l'établissement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(myInfo.getMysigle())){
            FacesMessage message = new FacesMessage("Veuillez saisir le sigle de l'établissement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(myInfo.getMyname())){
            FacesMessage message = new FacesMessage("Veuillez saisir le nom de l'établissement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(myInfo.getMycountry())){
            FacesMessage message = new FacesMessage("Veuillez saisir le pays!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(myInfo.getMyville())){
            FacesMessage message = new FacesMessage("Veuillez saisir la ville!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(myInfo.getMypostal())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code postal!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(myInfo.getMyphone())){
            FacesMessage message = new FacesMessage("Veuillez saisir le numéro de téléphone de l'établissement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(myInfo.getMyadresse())){
            FacesMessage message = new FacesMessage("Veuillez saisir l'adresse de létablissement!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(myInfo.getMymanagerTitle())){
            FacesMessage message = new FacesMessage("Veuillez saisir le titre du responsable!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(myInfo.getMymanager())){
            FacesMessage message = new FacesMessage("Veuillez saisir le nom du responsable!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(myInfo.getCodeMyinfo().matches(getCodeMyInfoCache())){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (myInfoFacadeL.find(myInfo.getCodeMyinfo())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    myInfo.setCodeMyinfo(null);                    
                    System.out.println("Cet enregistrement existe déjà");
                }
                                
            }      
        }             
    }
    
    //suppression d'un enregistrement
    public void supprimer(){
        try {                
            if(null != myInfo.getCodeMyinfo() && !Objects.equals(myInfo.getCodeMyinfo(), "")){
                myInfoFacadeL.remove(myInfo);  
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
        codeMyInfoCache = myInfo.getCodeMyinfo();
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
