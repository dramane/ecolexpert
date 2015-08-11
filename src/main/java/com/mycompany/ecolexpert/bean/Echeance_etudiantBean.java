/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoEcheanceEtudiantFacadeLocal;
import com.mycompany.ecolexpert.ejb.ViewEtudiantInscriptionEcheanceFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoEcheanceEtudiant;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
import com.mycompany.ecolexpert.jpa.ViewEtudiantInscriptionEcheance;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author HP
 */
@Named(value = "echeance_etudiantBean")
@ViewScoped
public class Echeance_etudiantBean extends BoutonActiveBean implements Serializable{
    private EcoEcheanceEtudiant echeance_etudiant;
    private List<EcoEcheanceEtudiant> listEcheance_etudiant;
    private List listCodeEcheance_etudiant;
  
    private ViewEtudiantInscriptionEcheance viewEtudiantInscripEcheance;   
    private List<ViewEtudiantInscriptionEcheance> listRechercheEtudiantInscripEcheance;
   
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoEcheanceEtudiantFacadeLocal  echeance_etudiantFacadeL;
    @EJB private ViewEtudiantInscriptionEcheanceFacadeLocal viewEtudiantInscripEcheanceFacadeL;

    /**
     * Creates a new instance of Echeance_etudiantBean
     */
    public Echeance_etudiantBean() {
        echeance_etudiant = new EcoEcheanceEtudiant();
        viewEtudiantInscripEcheance = new ViewEtudiantInscriptionEcheance();        
    }

    public EcoEcheanceEtudiant getEcheance_etudiant() {
        return echeance_etudiant;
    }

    public void setEcheance_etudiant(EcoEcheanceEtudiant echeance_etudiant) {
        this.echeance_etudiant = echeance_etudiant;
    }

    //getter et setter de viewEtudiantInscripEcheance
    public ViewEtudiantInscriptionEcheance getViewEtudiantInscripEcheance() {
        return viewEtudiantInscripEcheance;
    }

    public void setViewEtudiantInscripEcheance(ViewEtudiantInscriptionEcheance viewEtudiantInscripEcheance) {
        this.viewEtudiantInscripEcheance = viewEtudiantInscripEcheance;
    }    
    
    //getter de la listRechercheEtudiantInscripEcheance
    public List<ViewEtudiantInscriptionEcheance> getListRechercheEtudiantInscripEcheance() {
        return listRechercheEtudiantInscripEcheance;
    }  

    public void setListRechercheEtudiantInscripEcheance(List<ViewEtudiantInscriptionEcheance> listRechercheEtudiantInscripEcheance) {
        this.listRechercheEtudiantInscripEcheance = listRechercheEtudiantInscripEcheance;
    }
    
    
    public List<EcoEcheanceEtudiant> getListEcheance_etudiant() {
        try{
            listEcheance_etudiant = echeance_etudiantFacadeL.findAll();
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listEcheance_etudiant;
    }

    public List getListCodeEcheance_etudiant() {
        try{  
            listCodeEcheance_etudiant = new ArrayList();
            for(EcoEcheanceEtudiant echeanc : echeance_etudiantFacadeL.findAll()){
                listCodeEcheance_etudiant.add(new SelectItem(echeanc.getIdEcheanceEtu(), echeanc.getMatricule()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeEcheance_etudiant;
    }
    
    //chargement des infos de l'etudiant en foncttion de son matricule saisi
    public void rechercheEtudiantInscripEcheanceParMatricule(){
        try {       
            setViewEtudiantInscripEcheance(viewEtudiantInscripEcheanceFacadeL.findByMatricule(getViewEtudiantInscripEcheance().getMatricule())); 
     
            //initialisation des echeances
//            viewEtudiantInscripEcheance.setVers1(0);  
//            viewEtudiantInscripEcheance.setVers2(0);  
//            viewEtudiantInscripEcheance.setVers3(0);  
//            viewEtudiantInscripEcheance.setVers4(0);  
//            viewEtudiantInscripEcheance.setVers5(0);  
//            viewEtudiantInscripEcheance.setVers6(0);  
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Accun étudiant n'a été trouvé! ");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, message); 
            
            viewEtudiantInscripEcheance = new ViewEtudiantInscriptionEcheance();
        }                
    }
    
    //retourne la liste des etudiant recherche par le nom avec un jocker
    public List<ViewEtudiantInscriptionEcheance> rechercheEtudiantInscripEcheanceParNomEtPrenom(){  
        try{                           
            setListRechercheEtudiantInscripEcheance(viewEtudiantInscripEcheanceFacadeL.findEtudInscripByNonEtPrenomWithJocker(getViewEtudiantInscripEcheance().getNomEtPrenom())); 
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
         return listRechercheEtudiantInscripEcheance;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {
            viewEtudiantInscripEcheance = new ViewEtudiantInscriptionEcheance();
            echeance_etudiant = new EcoEcheanceEtudiant();  
                                    
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //initialisation et verrouillage des champs et initialisation des boutons
    public void annuler(){
        try {
            viewEtudiantInscripEcheance = new ViewEtudiantInscriptionEcheance();
            echeance_etudiant = new EcoEcheanceEtudiant();      
            
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //creation d'un enregistrement
    public void creer(){
        try {
            //affectation des valeur
            echeance_etudiant.setAnneeaca(getViewEtudiantInscripEcheance().getAnneeaca());
            echeance_etudiant.setNumetu(getViewEtudiantInscripEcheance().getNumetu());
            echeance_etudiant.setMatricule(getViewEtudiantInscripEcheance().getMatricule());
            echeance_etudiant.setCodeCycle(getViewEtudiantInscripEcheance().getCodeCycle());
            echeance_etudiant.setCodeNiveau(getViewEtudiantInscripEcheance().getCodeNiveau());
            echeance_etudiant.setCodeClasse(getViewEtudiantInscripEcheance().getCodeClasse());
            echeance_etudiant.setCodeRegime(getViewEtudiantInscripEcheance().getCodeRegime());
            echeance_etudiant.setDrtinscri(getViewEtudiantInscripEcheance().getInscriptionAPaye());
            echeance_etudiant.setDrtforma(getViewEtudiantInscripEcheance().getFormationAPaye());     
            
            echeance_etudiant.setVers1(getViewEtudiantInscripEcheance().getVers1());
            echeance_etudiant.setVers2(getViewEtudiantInscripEcheance().getVers2());
            echeance_etudiant.setVers3(getViewEtudiantInscripEcheance().getVers3());
            echeance_etudiant.setVers4(getViewEtudiantInscripEcheance().getVers4());
            echeance_etudiant.setVers5(getViewEtudiantInscripEcheance().getVers5());
            echeance_etudiant.setVers6(getViewEtudiantInscripEcheance().getVers6());    
            
            //Enregistrement d'une écheance
            echeance_etudiantFacadeL.create(echeance_etudiant); 
                                
        } catch (Exception e) {
            System.err.println("Erreur d'enregistrement capturée : "+e);
        }
    }
    
    //modification d' un utilisateur et de ses profils
    public void modifier(){
        try {
            //affectation des valeur                       
            echeance_etudiant.setIdEcheanceEtu(getViewEtudiantInscripEcheance().getIdEcheanceEtu()); 
                        
            echeance_etudiant.setAnneeaca(getViewEtudiantInscripEcheance().getAnneeaca());
            echeance_etudiant.setNumetu(getViewEtudiantInscripEcheance().getNumetu());
            echeance_etudiant.setMatricule(getViewEtudiantInscripEcheance().getMatricule());
            echeance_etudiant.setCodeCycle(getViewEtudiantInscripEcheance().getCodeCycle());
            echeance_etudiant.setCodeNiveau(getViewEtudiantInscripEcheance().getCodeNiveau());
            echeance_etudiant.setCodeClasse(getViewEtudiantInscripEcheance().getCodeClasse());
            echeance_etudiant.setCodeRegime(getViewEtudiantInscripEcheance().getCodeRegime());
            echeance_etudiant.setDrtinscri(getViewEtudiantInscripEcheance().getInscriptionAPaye());
            echeance_etudiant.setDrtforma(getViewEtudiantInscripEcheance().getFormationAPaye());             
            
            echeance_etudiant.setVers1(getViewEtudiantInscripEcheance().getVers1());
            echeance_etudiant.setVers2(getViewEtudiantInscripEcheance().getVers2());
            echeance_etudiant.setVers3(getViewEtudiantInscripEcheance().getVers3());
            echeance_etudiant.setVers4(getViewEtudiantInscripEcheance().getVers4());
            echeance_etudiant.setVers5(getViewEtudiantInscripEcheance().getVers5());
            echeance_etudiant.setVers6(getViewEtudiantInscripEcheance().getVers6());    
                                   
            //modification de l'utilisateur
            echeance_etudiantFacadeL.edit(echeance_etudiant);          
        } catch (Exception e) {
            System.err.println("Erreur de modification capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        try {
            //controle des données saisies
            if("".equals(viewEtudiantInscripEcheance.getMatricule())){
                FacesMessage message = new FacesMessage("Veuillez saisir le matricule de l'étudiant!" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);

                //verification si le matricule afficher appartient a un etudiant
            }else if("".equals(viewEtudiantInscripEcheance.getNometu())){
                FacesMessage message = new FacesMessage("Veuillez rechercher un étudiant!" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);
            
            }else{ 
                if (viewEtudiantInscripEcheance.getVers1() == null) viewEtudiantInscripEcheance.setVers1(0);             
                if (viewEtudiantInscripEcheance.getVers2() == null) viewEtudiantInscripEcheance.setVers2(0);  
                if (viewEtudiantInscripEcheance.getVers3() == null) viewEtudiantInscripEcheance.setVers3(0);  
                if (viewEtudiantInscripEcheance.getVers4() == null) viewEtudiantInscripEcheance.setVers4(0);  
                if (viewEtudiantInscripEcheance.getVers5() == null) viewEtudiantInscripEcheance.setVers5(0);  
                if (viewEtudiantInscripEcheance.getVers6() == null) viewEtudiantInscripEcheance.setVers6(0);  
             
                //Vérifion si la somme des versements est égale au droit de formation saisi                
                int som_versement = getViewEtudiantInscripEcheance().getVers1()+getViewEtudiantInscripEcheance().getVers2()+getViewEtudiantInscripEcheance().getVers3()+
                    getViewEtudiantInscripEcheance().getVers4()+getViewEtudiantInscripEcheance().getVers5()+getViewEtudiantInscripEcheance().getVers6();
                                
                if(getViewEtudiantInscripEcheance().getFormationAPaye()!= som_versement){
                    FacesMessage message = new FacesMessage("La somme de tous les versements doit être égale au droit de formation! ");
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                }else{
                    //Enregistrement ou modification des données apres le controle de saisie
                    if((null != getViewEtudiantInscripEcheance().getIdEcheanceEtu())){                      
                        modifier();
                        annuler();      
                        
                        FacesMessage message = new FacesMessage("Modification effectuée avec succès ");
                        message.setSeverity(FacesMessage.SEVERITY_ERROR);
                        FacesContext.getCurrentInstance().addMessage(null, message); 
                    }else{ 
                        creer();  
                        annuler();  

                        //notification d'enregistrement
                        FacesMessage message = new FacesMessage("Enregistrement effectué avec succès." );
                        message.setSeverity(FacesMessage.SEVERITY_INFO);
                        FacesContext.getCurrentInstance().addMessage(null, message);             
                    }

                }                        
            }           
        
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre! "+e );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message); 
        }                 
    }
    
//    public void onFocusVersement() {        
//        //initialisation des echeances
//        if (viewEtudiantInscripEcheance.getVers1() == null) {
//            viewEtudiantInscripEcheance.setVers1(0);  
//        }else if (viewEtudiantInscripEcheance.getVers2() == null) {
//            viewEtudiantInscripEcheance.setVers2(0);  
//        }else if (viewEtudiantInscripEcheance.getVers3() == null) {
//            viewEtudiantInscripEcheance.setVers3(0);  
//        }else if (viewEtudiantInscripEcheance.getVers4() == null) {
//            viewEtudiantInscripEcheance.setVers4(0);  
//        }else if (viewEtudiantInscripEcheance.getVers5() == null) {
//            viewEtudiantInscripEcheance.setVers5(0);  
//        }else if (viewEtudiantInscripEcheance.getVers6() == null) {
//            viewEtudiantInscripEcheance.setVers6(0);  
//        }        
//    }
}
