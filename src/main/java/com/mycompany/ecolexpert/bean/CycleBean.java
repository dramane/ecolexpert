/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;


import com.mycompany.ecolexpert.ejb.EcoCycleFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoCycle;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
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
@Named(value = "cycleBean")
@RequestScoped
public class CycleBean extends BoutonActiveBean{
    private EcoCycle cycle;
    private List<EcoCycle> listCycle;   
    private List listCodeCycle;   
    
    private EcoNiveau niveau;  
    List<EcoNiveau> listNiveau;
    
    //champ utilisé pour comparer le code du cycle
    private String codeCycleCache;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoCycleFacadeLocal cycleFacadeL;
    @EJB 
    private EcoNiveauFacadeLocal niveauFacadeLocal;

    /**
     * Creates a new instance of CycleBean
     */
    public CycleBean() {
        cycle = new EcoCycle();
        niveau = new EcoNiveau();
    }
    
    //getter et setter cycle
    public EcoCycle getCycle() {
        return cycle;
    }

    public void setCycle(EcoCycle cycle) {
        this.cycle = cycle;
    }
    
    //getter et setter de la classe Niveau

    public EcoNiveau getNiveau() {
        return niveau;
    }

    public void setNiveau(EcoNiveau niveau) {
        this.niveau = niveau;
    }    
    
    //getter et setter codeCycleCache
    public String getCodeCycleCache() {
        return codeCycleCache;
    }

    public void setCodeCycleCache(String codeCycleCache) {
        this.codeCycleCache = codeCycleCache;
    }    
    
    //Liste des codes cycle a afficher dans le combobox
    public List getListCodeCycle(){   
        try{  
            listCodeCycle = new ArrayList();
            
            for (EcoCycle cycl : cycleFacadeL.findAll()){
                listCodeCycle.add(new SelectItem(cycl.getCodeCycle(), cycl.getDesignation()));
            }
           
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeCycle;
    }
    
    //liste de tous les cycle
    public List<EcoCycle> getListCycle(){  
        try{
            listCycle = cycleFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCycle;
    }
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            cycle.setCodeCycle(null);
            setCodeCycleCache(null);
            cycle.setDesignation(null);
            cycle.setDescription(null);
            cycle.setNombreNiveau(null);                                               
            
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
            cycle.setCodeCycle(null);
            setCodeCycleCache(null);
            cycle.setDesignation(null);
            cycle.setDescription(null);
            cycle.setNombreNiveau(null);
                                        
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
            //Enregistrement d'un cycle
            cycleFacadeL.create(cycle); 
            
            //Enregistrement des niveaux du cycle dans la table Niveau
            for (int i = 0; i < cycle.getNombreNiveau(); i++) {
                niveau.setCodeNiveau(cycle.getCodeCycle()+(i+1));
                niveau.setNiveauCycle(i+1);
                niveau.setCodeCycle(cycle.getCodeCycle());
                niveauFacadeLocal.create(niveau);
            }            
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == cycle.getCodeCycle() || Objects.equals(cycle.getCodeCycle(), "")){            
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
    
    //modification d' un cycle
    public void modifier(){
        try { 
            listNiveau = niveauFacadeLocal.findByCodeCycle(cycle.getCodeCycle());
            int nbNiveau = cycle.getNombreNiveau();
            String vCodeCycle = cycle.getCodeCycle();
            //modification des niveaux du cycle dans la table Niveau  
            //if (!Objects.equals(cycle.getNombreNiveau(), cycleFacadeL.find(cycle.getCodeCycle()).getNombreNiveau())) {
                
                if (listNiveau != null) {

                    //suppression de tous les niveaux des l'encien cycle
                    for (EcoNiveau niv : listNiveau) {
                        System.out.println("SUPPRESSION DE : "+niv.getCodeNiveau());
                        niveauFacadeLocal.remove(niv);                         
                    }
                    
                    //modification du cycle
                    cycleFacadeL.edit(cycle);  
                    
                    //creation des niveaux du nouveau cycle
                    for (int i = 0; i < nbNiveau; i++) {
                        niveau.setCodeNiveau(vCodeCycle+(i+1));                        
                        niveau.setNiveauCycle(i+1);
                        niveau.setCodeCycle(vCodeCycle);
                        if (niveauFacadeLocal.find(niveau.getCodeNiveau()) == null) {
                            niveauFacadeLocal.create(niveau);
                        }                        
                    }   
                }else {
                    //modification du cycle
                    cycleFacadeL.edit(cycle);  
            
                    //creation des niveaux du nouveau cycle
                    for (int i = 0; i < nbNiveau; i++) {
                        niveau.setCodeNiveau(vCodeCycle+(i+1));
                        niveau.setNiveauCycle(i+1);
                        niveau.setCodeCycle(vCodeCycle);
                        niveauFacadeLocal.create(niveau);
                    }  
                }                          
           // }     
            
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(cycle.getCodeCycle())){
            FacesMessage message = new FacesMessage("Veuillez saisir le code du cycle!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if("".equals(cycle.getDesignation())){
            FacesMessage message = new FacesMessage("Veuillez saisir la designation du cycle!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else if(cycle.getNombreNiveau() == null){
            FacesMessage message = new FacesMessage("Veuillez saisir le nombre de Niveau du cycle!" );
            FacesContext.getCurrentInstance().addMessage(null, message);        
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(cycle.getCodeCycle().matches(codeCycleCache)){
                modifier();
                annuler();
                System.out.println("Données modifier");
            }else{ 
                if (cycleFacadeL.find(cycle.getCodeCycle())== null) {
                  creer();  
                  annuler();
                  System.out.println("Données enregistrée");
                }else{
                    FacesMessage message = new FacesMessage("Cet enregistrement existe déjà; veuillez saisir un autre!" );
                    FacesContext.getCurrentInstance().addMessage(null, message); 
                    cycle.setCodeCycle(null);                    
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
            if(null != cycle.getCodeCycle()&& !Objects.equals(cycle.getCodeCycle(), "")){
                cycleFacadeL.remove(cycle);  
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
        codeCycleCache = cycle.getCodeCycle();
        setBtnModifierDesactive(false);  
        setBtnSupprimerDesactive(false);  
        setBtnValiderDesactive(true);  
        setChampLectureSeul(true);  
        setChampCleNonAuto(true);
    }
}
