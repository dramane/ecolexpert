/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;


import com.mycompany.ecolexpert.ejb.EcoAcademiqueFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoAcademique;
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
@Named(value = "academiqueBean")
@RequestScoped
public class AcademiqueBean extends BoutonActiveBean{
    private EcoAcademique academique;
    private List<EcoAcademique> listAcademique;  
    private List listAnneeAcademiq; 
    //liste des année academique sans le code
    private List listAnneeAcademiq2; 
    
      
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoAcademiqueFacadeLocal academiqFacadeL;
    
    /*public boolean isDroitsave() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.setTime(date); 
        return (calendar.get(Calendar.HOUR)==0);
    }
    
    public boolean isDroitupdate() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.setTime(date); 
        return (calendar.get(Calendar.HOUR)==0 && calendar.get(Calendar.MINUTE)>=5);
    }*/
    
    /**
     * Creates a new instance of AcademiqueBean
     */        
    
    public AcademiqueBean() {
        academique = new EcoAcademique(); 
    }

    //Academique getter and setter
    public EcoAcademique getAcademique() {
        return academique;
    }

    public void setAcademique(EcoAcademique academique) {
        this.academique = academique;
    }      
    
    //liste de toutes les années academiques
    public List<EcoAcademique> getListAcademiques(){  
        try{
                  listAcademique = academiqFacadeL.findAll();              
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listAcademique;
    }
    
    //Liste des années academique a afficher dans le combobox
    public List getListAnneeAcademiq(){   
        try{  
            listAnneeAcademiq = new ArrayList();
            for(EcoAcademique acad : getListAcademiques()){          
              listAnneeAcademiq.add(new SelectItem(acad.getIdacademique(), acad.getAnneeaca()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listAnneeAcademiq;
    }   
    
    //Liste des années academique a afficher dans le combobox
    public List getListAnneeAcademiq2(){   
        try{  
            listAnneeAcademiq = new ArrayList();
            for(EcoAcademique acad : getListAcademiques()){          
              listAnneeAcademiq.add(new SelectItem(acad.getAnneeaca(), acad.getAnneeaca()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listAnneeAcademiq;
    }   
    
    //preparation d'un nouvel enregistrement
    public void nouveau(){
        try {            
            academique.setIdacademique(null);
            academique.setAnneeaca(null);
            academique.setAnneePre(null);
            academique.setDateDebut(null);
            academique.setDateFin(null);
            academique.setObjectif(null);
            academique.setNannee(null);
            
            setBtnModifierDesactive(true);  
            setBtnSupprimerDesactive(true);
            setChampLectureSeul(true);   
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //initialisation et verrouillage des champs et initialisation des boutons
    public void annuler(){
        try {
            academique.setIdacademique(null);
            academique.setAnneeaca(null);
            academique.setAnneePre(null);
            academique.setDateDebut(null);
            academique.setDateFin(null);
            academique.setObjectif(null);
            academique.setNannee(null);
                        
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
            academique.setNannee(academique.getAnneeaca().substring(2, 4));
            academiqFacadeL.create(academique);  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
    
    //preparation de la modification
    public void preparation_modifier(){
        try {
            if(null == academique.getIdacademique() || Objects.equals(academique.getIdacademique(), "")){            
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
            if(null != academique.getIdacademique() && !Objects.equals(academique.getIdacademique(), "")){
                academiqFacadeL.edit(academique);
            }
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }        
    }
    
    //code du bouton valider qui consiste à créer ou à modifier un enregistrement
    public void valider(){
        //controle des données saisies
        if("".equals(academique.getAnneeaca())){
            FacesMessage message = new FacesMessage("Merci de saisir l'année academique!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if(academique.getDateDebut() == null){
            FacesMessage message = new FacesMessage("Merci de saisir la date du debut de l'année academique!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else if(academique.getDateFin()== null){
            FacesMessage message = new FacesMessage("Merci de saisir la date de la fin de l'année academique!" );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            
            //Enregistrement ou modification des données apres le controle de saisie
            if(null != academique.getIdacademique() && !Objects.equals(academique.getIdacademique(), "")){
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
            if(null != academique.getIdacademique() && !Objects.equals(academique.getIdacademique(), "")){
                academiqFacadeL.remove(academique);  
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