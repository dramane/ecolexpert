/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoClasseFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoCycleFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoClasse;
import com.mycompany.ecolexpert.jpa.EcoCycle;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
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
@Named(value = "classeBean")
@ViewScoped
public class ClasseBean extends BoutonActiveBean implements Serializable{
    private EcoClasse classe;
    private List<EcoClasse> listClasse;
    private List listCodeClasse; 

    private EcoNiveau niveau;
    private List listCodeNiveau; 
    
    private EcoCycle cycle;
    private List listCodeCycle;
    
    //champ utilisé pour comparer le code de la classe
    private String codeClasseCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoClasseFacadeLocal  classeFacadeL;
    @EJB
    private EcoCycleFacadeLocal cycleFacadeL;
    @EJB
    private EcoNiveauFacadeLocal  niveauFacadeL;

    /**
     * Creates a new instance of ClasseBean
     */
    public ClasseBean() {
        classe = new EcoClasse();
        niveau = new EcoNiveau();
        cycle = new EcoCycle();
    }       
    
//    @PostConstruct
//    public void init() {
//         listCodeCycle = new ArrayList();            
//        for (EcoCycle cycl : cycleFacadeL.findAll()){
//            listCodeCycle.add(new SelectItem(cycl.getCodeCycle(), cycl.getDesignation()));
//        }
//    }

    //getter et setter des class jpa
    public EcoClasse getClasse() {
        return classe;
    }

    public void setClasse(EcoClasse classe) {
        this.classe = classe;
    }

    public EcoNiveau getNiveau() {
        return niveau;
    }

    public void setNiveau(EcoNiveau niveau) {
        this.niveau = niveau;
    }

    public EcoCycle getCycle() {
        return cycle;
    }

    public void setCycle(EcoCycle cycle) {
        this.cycle = cycle;
    }

    public String getCodeClasseCache() {
        return codeClasseCache;
    }

    public void setCodeClasseCache(String codeClasseCache) {
        this.codeClasseCache = codeClasseCache;
    }
    
    //getter des lists
    public List<EcoClasse> getListClasse() {
        try{
            listClasse = classeFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listClasse;
    }

    public List getListCodeClasse() {
        try{  
            listCodeClasse = new ArrayList();
            for(EcoClasse clas : classeFacadeL.findAll()){          
              listCodeClasse.add(new SelectItem(clas.getCodeClasse(), clas.getNomclasse()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeClasse;
    }

    public List getListCodeNiveau() {
        listCodeNiveau = new ArrayList();
        if((classe.getCodeCycle()) != null && !"".equals(classe.getCodeCycle())){   
            for(EcoNiveau niv : niveauFacadeL.findAll()){   
                if (classe.getCodeCycle().equals(niv.getCodeCycle())) {
                    listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
                }              
            }
            return listCodeNiveau;
        }else return new ArrayList();
    }

    public List getListCodeCycle() {
        listCodeCycle = new ArrayList();            
        for (EcoCycle cycl : cycleFacadeL.findAll()){
            listCodeCycle.add(new SelectItem(cycl.getCodeCycle(), cycl.getDesignation()));
        }
        return listCodeCycle;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {                
            classe.setCodeClasse(null);
            setCodeClasseCache(null);
            classe.setNomclasse(null);
            classe.setDescription(null);
            classe.setCodeFiliere(null);
            classe.setCodeCycle(null);
            classe.setCodeNiveau(null);
            classe.setFormation(null);
            classe.setNomSalle(null);
            classe.setEffectifMax(null);
            
            setBtnModifierDesactive(true);  
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
            classe.setCodeClasse(null);
            setCodeClasseCache(null);
            classe.setNomclasse(null);
            classe.setDescription(null);
            classe.setCodeFiliere(null);
            classe.setCodeCycle(null);
            classe.setCodeNiveau(null);
            classe.setFormation(null);
            classe.setNomSalle(null);
            classe.setEffectifMax(null);
            
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
        System.out.println("valeur niveau : "+classe.getCodeNiveau());
        try {
            //Enregistrement d'une salle
            classeFacadeL.create(classe);                                                  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == getCodeClasseCache() || Objects.equals(getCodeClasseCache(), "")){            
                FacesMessage message = new FacesMessage( "Veuillez sélectionner l'enregistrement à modifier!" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);            
            }            
            setBtnModifierDesactive(true);  
            setBtnSupprimerDesactive(true);
            setBtnValiderDesactive(false);
            setChampLectureSeul(false);
            setChampCleNonAuto(true);
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //modification d' une classe
    public void modifier(){
        try {          
            //modification de l'utilisateur
            classeFacadeL.edit(classe);                                            
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(classe.getCodeClasse())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code de la classe!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(classe.getNomclasse())){
            FacesMessage message = new FacesMessage("Veuillez saisir le nom de la classe!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if(classe.getEffectifMax() == null){
            FacesMessage message = new FacesMessage("Veuillez saisir la capacité d'accueil de la classe!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(classe.getCodeFiliere())){
            FacesMessage message = new FacesMessage("Veuillez selectionner la filière!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(classe.getCodeCycle())){
            FacesMessage message = new FacesMessage("Veuillez saisir le cycle!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(classe.getCodeNiveau())){
            FacesMessage message = new FacesMessage("Veuillez saisir le niveau!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if(classe.getFormation() == null){
            FacesMessage message = new FacesMessage("Veuillez selectionner la formation!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if("".equals(classe.getNomSalle())){
            FacesMessage message = new FacesMessage("Veuillez selectionner la salle!" );
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(classe.getCodeClasse().matches(codeClasseCache)){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (classeFacadeL.find(classe.getCodeClasse())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    classe.setCodeClasse(null);                    
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
            if(null != classe.getCodeClasse() && !Objects.equals(classe.getCodeClasse(), "")){
                classeFacadeL.remove(classe);  
                annuler();
            }else{
                FacesMessage message = new FacesMessage( "Veuillez sélectionner l'enregistrement à supprimer!" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
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
        setCodeClasseCache(classe.getCodeClasse());
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
    
    //Action ajax lors de la selection du cycle
    public void onCycleChange() {
        listCodeNiveau = new ArrayList();
        if(classe.getCodeCycle() != null && !"".equals(classe.getCodeCycle())){            
            List<EcoNiveau> data ;
            data = niveauFacadeL.findByCodeCycle(classe.getCodeCycle());
            for(EcoNiveau niv : data){          
              listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
            }  
        }else listCodeNiveau = new ArrayList();
    }
}
