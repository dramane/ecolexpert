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
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TOSHIBA
 */
//@ManagedBean
@Named(value = "connexionBean")
@RequestScoped
public class ConnexionBean implements Serializable{

    //private static final String CHAMP_VAL_REQUETE = "connexion:nomUtilisateur";
    private static final String CHAMP_ID_UT   = "idUtilisateur";
    private static final String CHAMP_LOGIN  = "loginUtilisateur";
    private static final String CHAMP_PASS   = "motDePasseUtilisateur";  
    private static final String CHAMP_NOM  = "nomUtilisateur";
    private static final String CHAMP_PRENOM  = "prenomUtilisateur";
    private static final String CHAMP_EMAIL   = "emailUtilisateur";  
    private static final String CHAMP_PROFIL = "profilUtilisateur";
    
    EcoUtilisateur utilisateur = null;
    EcoProfilUtilisateur profilUtilisateur = null;
    List<EcoProfilUtilisateur> listProfilUtilisateur = null; 

    @EJB EcoUtilisateurFacadeLocal UtilisateurFacadeL;    
    
    @EJB EcoProfilUtilisateurFacadeLocal profilUtilisateurFacadeL;

    @EJB EcoProfilFacadeLocal profilFacadeL;
        
    public ConnexionBean() {
        utilisateur = new EcoUtilisateur();
        profilUtilisateur = new EcoProfilUtilisateur();
    } 

    //getter et setter de l'entity bean utilisateur
    public EcoUtilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(EcoUtilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    //getter et setter de la classe ProfilUtilisateur
    public EcoProfilUtilisateur getProfilUtilisateur() {
        return profilUtilisateur;
    }

    public void setProfilUtilisateur(EcoProfilUtilisateur profilUtilisateur) {
        this.profilUtilisateur = profilUtilisateur;
    }
    
    //connexion a l'application
    public String connexionUtilisateur(){
        
        //Récupération du context
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        
        //Annuler la session si elle exite
        HttpSession session = (HttpSession) context.getSession(false);
        session.invalidate();
        
        //creation d'une nouvelle session
        session = (HttpSession) context.getSession(true);
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        
        String connexion = null;
 
        //Ajout d'une session d'utilisateur si les données sont bonne, sinon la session est null.       
        try {                           
            //recherche de l'utilisateur dans la BD                
            if (UtilisateurFacadeL.findByLoginAndPassword(utilisateur.getLogin(), utilisateur.getMotDePasse()).toString() == null){  

                connexion = "Echec";
            } else {
                //ajout des données de l'utilisateur dans sa nouvelle session
                utilisateur = UtilisateurFacadeL.findByLoginAndPassword(utilisateur.getLogin(), utilisateur.getMotDePasse()); 

                session.setAttribute(CHAMP_ID_UT, utilisateur.getIdUtilisateur());
                session.setAttribute(CHAMP_LOGIN, utilisateur.getLogin() );
                session.setAttribute(CHAMP_PASS, utilisateur.getMotDePasse());
                session.setAttribute(CHAMP_NOM, utilisateur.getNom());
                session.setAttribute(CHAMP_PRENOM, utilisateur.getPrenom());
                session.setAttribute(CHAMP_EMAIL, utilisateur.getEmail());

                System.out.println("L'Email UtilisateurFacadeL est : "+utilisateur.getEmail());

                //Ajout des profil de l'utilisateur dans sa session.  
                int active = 1;
                Object login;
                
                login = utilisateur.getLogin();
                listProfilUtilisateur = profilUtilisateurFacadeL.findByLoginAndActive(login, active);

                //Ajout des profils de l'utilisateur dans la session
                if(!listProfilUtilisateur.isEmpty()){
                   for(EcoProfilUtilisateur profilUtil : listProfilUtilisateur){

                       //Si l'utilisateur a le profil administrateur alors on lui donne tous les autres profils
                       //Sinon on lui que ses profils dont il a accès
                       if ("administrateur".equals(profilUtil.getCodeProfil())) {                           
                           for(EcoProfil prof : profilFacadeL.findAll()){
                               session.setAttribute(prof.getCodeProfil(), prof.getCodeProfil());
                               System.out.println("Le profil est : "+ prof.getCodeProfil());   
                           }
//                           break;
                       } else {
                           session.setAttribute(profilUtil.getCodeProfil(), profilUtil.getCodeProfil());
                           System.out.println("Le profil est : "+ profilUtil.getCodeProfil());     
                       }                                                              
                   }

                   connexion = "Succes";
                   System.out.println("L'utilisateur est : "+utilisateur.getNom()+" "+utilisateur.getPrenom());
                   System.out.println("Succes de connexion");                    
                }else{
                    connexion = "Echec";

                    FacesMessage message = new FacesMessage("Vous n'avez accès à aucun profil !");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }

//                        if(!listProfilUtilisateur.isEmpty()){
////                           for(EcoProfilUtilisateur ProfilUtilisateur : listProfilUtilisateur){                      
////                               session.setAttribute(ProfilUtilisateur.getIdProfil(), ProfilUtilisateur.getIdProfil());
////                               System.out.println("Le profil est : "+ ProfilUtilisateur.getIdProfil());
////                           }
//                                                        
//                           System.out.println("L'ID de l'utilisateur est : "+idUt);
//                           
//                           indice = "Succes";
//                        }else{
//                            indice = "Echec";
//                            
//                            FacesMessage message = new FacesMessage( "Vous n'avez accès à aucun profil !" );
//                            FacesContext.getCurrentInstance().addMessage(null, message);
//                        }
            }                                                        
           
        } catch (Exception e ) { 
            FacesMessage message = new FacesMessage( "Le nom d'utilisateur ou le mot de passe saisi est incorrect !" );
            FacesContext.getCurrentInstance().addMessage(null, message);                     
        }
        return connexion;  
    }
 
  
    //fermeture d'une session
    public String deconnexionUtilisateur(){
        //invalidate user session
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(false);
        session.invalidate();
        
        return "Deconnexion";
    }
    
}
