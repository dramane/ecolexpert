/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoProfilFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoProfilUtilisateurFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoUtilisateurFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoProfil;
import com.mycompany.ecolexpert.jpa.EcoProfilUtilisateur;
import com.mycompany.ecolexpert.jpa.EcoUtilisateur;
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
@Named(value = "utilisateurBean")
@RequestScoped
public class UtilisateurBean extends BoutonActiveBean{
    private EcoUtilisateur user;
    private List<EcoUtilisateur> listUser;      
    private List listUtil; 
    private EcoProfil profil;
    private List listProfil;   
    private String[] selectedProfils;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    private EcoProfilUtilisateur profilUser;
    private List<EcoProfilUtilisateur> listProfilUser;      
      
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoUtilisateurFacadeLocal userFacadeL;
    @EJB
    private EcoProfilFacadeLocal profilFacadeL;
    @EJB
    private EcoProfilUtilisateurFacadeLocal profilUserFacadeL;


    /**
     * Creates a new instance of UtilisateurBean
     */
    public UtilisateurBean() {
        user = new EcoUtilisateur();
        profil = new EcoProfil();
        profilUser = new EcoProfilUtilisateur();
    }
    
    // gettter et setter du user
    public EcoUtilisateur getUser() {
        return user;
    }

    public void setUser(EcoUtilisateur user) {
        this.user = user;
    }
        
    //getter et setter du profil
    public EcoProfil getProfil() {
        return profil;
    }

    public void setProfil(EcoProfil profil) {
        this.profil = profil;
    }
    
    //getter et setter de la liste des profil profils selectionnés
    public String[] getSelectedProfils() {
        return selectedProfils;
    }

    public void setSelectedProfils(String[] selectedProfils) {
        this.selectedProfils = selectedProfils;
    }    
   
    //getter et setter du profilUtilisateur
    public EcoProfilUtilisateur getProfilUser() {
        return profilUser;
    }

    public void setProfilUser(EcoProfilUtilisateur profilUser) {
        this.profilUser = profilUser;
    }            
    
    //liste de tout les utilisateur
    public List<EcoUtilisateur> getListUser(){  
        try{
                  listUser = userFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listUser;
    }
    
//    //Liste des utilisateur a afficher dans le combobox
//    public List getListUtil(){   
//        try{  
//            listUtil = new ArrayList();
//            for(EcoUtilisateur util : getListUser()){          
//              listUtil.add(new SelectItem(util.getIdUtilisateur(), util.getLogin()));
//            }
//        }catch (Exception ex) {
//              System.err.println("Erreur capturée : "+ex);
//        }
//        return listUtil;
//    }   
   
    
    //Liste des profils a afficher dans la liste de choix
    public List getListProfil(){   
        try{  
            listProfil = new ArrayList();
            
            for (EcoProfil prof : profilFacadeL.findAll()){
                listProfil.add(new SelectItem(prof.getCodeProfil(), prof.getLibelleProfil()));
            }
                        
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listProfil;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
//            user.setIdUtilisateur(null);
            user.setLogin(null);
            user.setMotDePasse(null);
            user.setNom(null);
            user.setPrenom(null);
            user.setEmail(null);
            setSelectedProfils(null);
            
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
//            user.setIdUtilisateur(null);
            user.setLogin(null);
            user.setMotDePasse(null);
            user.setNom(null);
            user.setPrenom(null);
            user.setEmail(null);
            setSelectedProfils(null);           
                        
            setBtnModifierDesactive(true);
            setBtnSupprimerDesactive(true) ; 
            setBtnValiderDesactive(true);
            setChampLectureSeul(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //creation d'un enregistrement
    public void creer(){
        try {
//            academique.setNannee(academique.getAnneeaca().substring(2, 4));
            //Enregistrement de l'utilisateur
            userFacadeL.create(user);  
            
            //Enregistrement des profils
            profilUser.setLogin(user.getLogin());
            profilUser.setActive(1);
            for (String selectedProfil : selectedProfils) {               
                profilUser.setCodeProfil(selectedProfil);                
                profilUserFacadeL.create(profilUser);
            }
                        
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == user.getLogin() || Objects.equals(user.getLogin(), "")){            
                FacesMessage message = new FacesMessage( "Merci de sélectionner l'enregistrement à modifier!" );
                FacesContext.getCurrentInstance().addMessage(null, message);            
            }
            setBtnModifierDesactive(true);  
            setBtnSupprimerDesactive(true);               
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //modification d' un utilisateur et de ses profils
    public void modifier(){
        try {
            if(null != user.getLogin() && !Objects.equals(user.getLogin(), "")){
                //modification de l'utilisateur
                userFacadeL.edit(user);
                
                //suppression des profils de l'utilisateur
                listProfilUser = profilUserFacadeL.findByLogin(user.getLogin());
                for (EcoProfilUtilisateur ProfilUtilisateur : listProfilUser) {
                    profilUserFacadeL.remove(ProfilUtilisateur);
                }
                             
                profilUser.setLogin(user.getLogin());
                profilUser.setActive(1);
                //enregistrement des profils selectionnés
                for (String selectedProfil : selectedProfils) {
                   profilUser.setCodeProfil(selectedProfil);                                      
                   profilUserFacadeL.create(profilUser);
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(user.getNom())){
            FacesMessage message = new FacesMessage("Merci de saisir le nom de l'utilisateur!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(user.getPrenom())){
            FacesMessage message = new FacesMessage("Merci de saisir le prénom de l'utilisateur!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if(!user.getEmail().matches(EMAIL_PATTERN)){
            FacesMessage message = new FacesMessage("Veuillez saisir un e-mail correct!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(user.getLogin())){
            FacesMessage message = new FacesMessage("Merci de saisir le login de l'utilisateur!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(user.getMotDePasse())){
            FacesMessage message = new FacesMessage("Merci de saisir le mot de passe de l'utilisateur!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(null != user.getLogin()&& !Objects.equals(user.getLogin(), "")){
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
            if(null != user.getLogin()&& !Objects.equals(user.getLogin(), "")){
                userFacadeL.remove(user);  
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
        listProfilUser = profilUserFacadeL.findByLogin(user.getIdUtilisateur());
        String [] var = new String[15];
        int i = 0;
        
        for (EcoProfilUtilisateur ecoProfilUser  : listProfilUser) { 
            
            var[i] = ecoProfilUser.getCodeProfil();
            System.out.println("variable : "+ecoProfilUser.getCodeProfil());            
            i++;
        } 
        setSelectedProfils(var);
                
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
    }
}
