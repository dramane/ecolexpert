/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoAcademiqueFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoClasseFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoFraisScolariteFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoMyinfoFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoAcademique;
import com.mycompany.ecolexpert.jpa.EcoClasse;
import com.mycompany.ecolexpert.jpa.EcoFraisScolarite;
import com.mycompany.ecolexpert.jpa.EcoMyinfo;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author HP
 */
@Named(value = "gestionScolariteBean")
//@RequestScoped
@ViewScoped
public class gestionScolariteBean extends BoutonActiveBean implements Serializable{
    private EcoFraisScolarite fraisScolarite;
    private List<EcoFraisScolarite> listFraisScolarite;
    private List listCodeFraisScolarite;  
    private EcoMyinfo myInfo;
    private EcoAcademique anneeAcademiq;
    
    private EcoNiveau niveau;
    private List listCodeNiveau; 
    
    private EcoClasse classe;
    private List listCodeClasse; 
    
    //champ utilisé pour comparer le code de la classe
    private String codeFraisScolariteCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB    
    private EcoFraisScolariteFacadeLocal fraisScolariteFacadeL;   
    @EJB 
    private EcoMyinfoFacadeLocal myInfoFacadeL;
    @EJB 
    private EcoAcademiqueFacadeLocal anneeAcademiqFacadeL;
    @EJB
    private EcoNiveauFacadeLocal  niveauFacadeL;
    @EJB
    private EcoClasseFacadeLocal  classeFacadeL;
    
    /**
     * Creates a new instance of gestionScolariteBean
     */
    public gestionScolariteBean() {
        fraisScolarite = new EcoFraisScolarite(); 
        myInfo = new EcoMyinfo();    
        anneeAcademiq = new EcoAcademique();       
        niveau = new EcoNiveau();
        classe = new EcoClasse();
    }   
    
    //getter et setter fraisScolarite
    public EcoFraisScolarite getFraisScolarite() {
        return fraisScolarite;
    }

    public void setFraisScolarite(EcoFraisScolarite fraisScolarite) {
        this.fraisScolarite = fraisScolarite;
    }
    
    //getter et setter de myinfo
    public EcoMyinfo getMyInfo() {
        return myInfoFacadeL.findMyInfo_etablissementEnCours();
    }

    public void setMyInfo(EcoMyinfo myInfo) {        
        this.myInfo = myInfo;
    }

    //getter et setter de l'année academique
    public EcoAcademique getAnneeAcademiq() {
        return anneeAcademiqFacadeL.findAnneeEnCours();
    }

    public void setAnneeAcademiq(EcoAcademique anneeAcademiq) {
        this.anneeAcademiq = anneeAcademiq;
    } 
    
    //getter et setter de niveau
    public EcoNiveau getNiveau() {
        return niveau;
    }

    public void setNiveau(EcoNiveau niveau) {
        this.niveau = niveau;
    }
    
    //getter et setter de classe

    public EcoClasse getClasse() {
        return classe;
    }

    public void setClasse(EcoClasse classe) {
        this.classe = classe;
    }    
    
    //getter et setter de code scolarité caché
    public String getCodeFraisScolariteCache() {
        return codeFraisScolariteCache;
    }

    public void setCodeFraisScolariteCache(String codeFraisScolariteCache) {
        this.codeFraisScolariteCache = codeFraisScolariteCache;
    }
       
    //Liste des frais scolarié a afficher dans le combobox
    public List getListCodeFraisScolarite() {
        try{  
            listCodeFraisScolarite = new ArrayList();
            for(EcoFraisScolarite frais : fraisScolariteFacadeL.findAll()){
                listCodeFraisScolarite.add(new SelectItem(frais.getCodeFraisScolarite(), frais.getCodeFraisScolarite()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeFraisScolarite;
    }
    
    //liste de toutes les frais de scolarité
    public List<EcoFraisScolarite> getListFraisScolarite() {
        try{
            listFraisScolarite = fraisScolariteFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée zz: "+ex);
        }
        return listFraisScolarite;
    }      
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try { 
            fraisScolarite.setCodeElementFrais(null);
            setCodeFraisScolariteCache(null);            
            fraisScolarite.setCodeCycle(null);
            fraisScolarite.setCodeNiveau(null);
            fraisScolarite.setCodeClasse(null);            
            fraisScolarite.setCodeRegime(null);       
            fraisScolarite.setCodeFraisScolarite(null);
            fraisScolarite.setMontantFrais(null);            
                   
            setBtnSupprimerDesactive(true);
            setBtnValiderDesactive(false);
            setChampLectureSeul(false);   
            setChampCleNonAuto(false);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //initialisation et verrouillage des champs et initialisation des boutons
    public void annuler(){
        try {
            fraisScolarite.setCodeElementFrais(null);
            setCodeFraisScolariteCache(null);            
            fraisScolarite.setCodeCycle(null);
            fraisScolarite.setCodeNiveau(null);
            fraisScolarite.setCodeClasse(null);            
            fraisScolarite.setCodeRegime(null);       
            fraisScolarite.setCodeFraisScolarite(null);
            fraisScolarite.setMontantFrais(null);
            
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
            //Enregistrement d'un fraisScolarite
            fraisScolariteFacadeL.create(fraisScolarite);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
        
    //code du bouton valider qui consiste à créer un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(fraisScolarite.getCodeElementFrais())){
            FacesMessage message = new FacesMessage("Veuillez selectionner le frais concerné!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(fraisScolarite.getCodeCycle())){
            FacesMessage message = new FacesMessage("Veuillez selectionner le cycle!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(fraisScolarite.getCodeNiveau())){
            FacesMessage message = new FacesMessage("Veuillez selectionner le niveau!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(fraisScolarite.getCodeClasse())){
            FacesMessage message = new FacesMessage("Veuillez selectionner la classe!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(fraisScolarite.getCodeRegime())){
            FacesMessage message = new FacesMessage("Veuillez selectionner le regime!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if(fraisScolarite.getMontantFrais() == null){
            FacesMessage message = new FacesMessage("Veuillez saisir le montant du frais!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{                                                     
              
            //Génération du code frais scolarié
            fraisScolarite.setCodeFraisScolarite(getMyInfo().getCodeMyinfo().toUpperCase()+getAnneeAcademiq().getAnneeaca()+
            fraisScolarite.getCodeElementFrais().toUpperCase().substring(0, 4)+fraisScolarite.getCodeCycle().toUpperCase()+
            fraisScolarite.getCodeNiveau().toUpperCase()+fraisScolarite.getCodeClasse().toUpperCase()+
            fraisScolarite.getCodeRegime().toUpperCase().replaceAll(" ", "").substring(0, 8));             

            //Enregistrement des données apres le controle de saisie       
            if (fraisScolariteFacadeL.find(fraisScolarite.getCodeFraisScolarite())== null) {
                //Assignation des valeur saisies dans les champs                           
                fraisScolarite.setIdacademique(getAnneeAcademiq().getIdacademique());
                fraisScolarite.setCodeMyinfo(getMyInfo().getCodeMyinfo());                     
                creer();  
                annuler();
                System.out.println("Données enregistrée");
            }else{
                FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                FacesContext.getCurrentInstance().addMessage(null, message); 
                fraisScolarite.setCodeFraisScolarite(null);                    
                System.out.println("Cet enregistrement existe déjà");
            }           
        }    
        setBtnSupprimerDesactive(true);           
    }
    
    //suppression d'un enregistrement
    public void supprimer(){
        try {             
            // suppression de l'enregistrement
            if(null != fraisScolarite.getCodeFraisScolarite() && !Objects.equals(fraisScolarite.getCodeFraisScolarite(), "")){
                fraisScolariteFacadeL.remove(fraisScolarite);  
                annuler();
            }else{
                FacesMessage message = new FacesMessage( "Veuillez sélectionner l'enregistrement à supprimer!" );
                FacesContext.getCurrentInstance().addMessage(null, message);        
                setBtnSupprimerDesactive(true);                
            }
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //methode de selection dans la datatable
    public void selectionner(){ 
        setCodeFraisScolariteCache(fraisScolarite.getCodeFraisScolarite());
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
    
    //getter de la liste a aficher dans le combobox niveau
    public List getListCodeNiveau(){           
        listCodeNiveau = new ArrayList();
        if((fraisScolarite.getCodeCycle()) != null && !"".equals(fraisScolarite.getCodeCycle())){   
            for(EcoNiveau niv : niveauFacadeL.findAll()){   
                if (fraisScolarite.getCodeCycle().equals(niv.getCodeCycle())) {
                    listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
                }              
            }
            return listCodeNiveau;
        }else return new ArrayList();
    }
    
    //getter de la liste a aficher dans le combobox classe
    public List getListCodeClasse() {
        listCodeClasse = new ArrayList();
        if((fraisScolarite.getCodeNiveau()) != null && !"".equals(fraisScolarite.getCodeNiveau())){   
            for(EcoClasse clas : classeFacadeL.findAll()){   
                if (fraisScolarite.getCodeNiveau().equals(clas.getCodeNiveau())) {
                    listCodeClasse.add(new SelectItem(clas.getCodeClasse(), clas.getNomclasse()));
                }              
            }
            return listCodeClasse;
        }else return new ArrayList();
    }
    
    //Action ajax lors de la selection du cycle
    public void onCycleChange() {
        listCodeNiveau = new ArrayList();
        if(fraisScolarite.getCodeCycle() != null && !"".equals(fraisScolarite.getCodeCycle())){            
            List<EcoNiveau> data ;
            data = niveauFacadeL.findByCodeCycle(fraisScolarite.getCodeCycle());
            for(EcoNiveau niv : data){          
              listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
            }  
        }else listCodeNiveau = new ArrayList();
    }
    
    //événement ajax l'ors du clic sur le combobox classe
    public void onNiveauChange() {
        listCodeClasse = new ArrayList();
        if(fraisScolarite.getCodeNiveau() != null && !"".equals(fraisScolarite.getCodeNiveau())){            
            List<EcoClasse> data ;
            data = classeFacadeL.findByCodeNiveau(fraisScolarite.getCodeNiveau());
            for(EcoClasse clas : data){          
              listCodeClasse.add(new SelectItem(clas.getCodeClasse(), clas.getNomclasse()));
            }  
        }else listCodeClasse = new ArrayList();                         
                
    }          
}
