/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoEcueFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoEquipePedagogiqueFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoParcoursFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoSemetreFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoTypeUeFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoUeParcoursFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoEcue;
import com.mycompany.ecolexpert.jpa.EcoEquipePedagogique;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
import com.mycompany.ecolexpert.jpa.EcoParcours;
import com.mycompany.ecolexpert.jpa.EcoSemetre;
import com.mycompany.ecolexpert.jpa.EcoTypeUe;
import com.mycompany.ecolexpert.jpa.EcoUeParcours;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;

/**
 *
 * @author HP
 */
@Named(value = "parcoursBean")
@ViewScoped
public class ParcoursBean implements Serializable{
    private EcoParcours parcours;
    private List<EcoParcours> listParcours;
    private List listCodeParcours;         
    
    private EcoUeParcours parcours_Ue;
    private List<EcoUeParcours> listParcours_Ue = new ArrayList<>(); //liste a ajouter dans la dataTable
    
    private EcoSemetre semetre;
    private List listCodeSemetre;   
    
    private EcoTypeUe typeUE;
    private List listCodeTypeUe;
    
    private EcoEcue ecue;
    private List<EcoEcue> listEcue = new ArrayList<>(); 
    private List<EcoEcue> listEcueTemporaire = new ArrayList<>();
            
    private EcoEquipePedagogique equipePedagogiq;
    private List<EcoEquipePedagogique> listEquipePedagogiq = new ArrayList<>();
    
    private List listCodeNiveau;   
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoParcoursFacadeLocal  parcoursFacadeL;
    @EJB private EcoUeParcoursFacadeLocal  parcours_UeFacadeL;
    @EJB private EcoSemetreFacadeLocal  semetreFacadeL;
    @EJB private EcoTypeUeFacadeLocal typeUeFacadeL;
    @EJB private EcoEcueFacadeLocal  ecueFacadeL;
    @EJB private EcoEquipePedagogiqueFacadeLocal equipePedagogiqFacadeL;
    @EJB private EcoNiveauFacadeLocal  niveauFacadeL;

    /**
     * Creates a new instance of ParcoursBean
     */
    public ParcoursBean(){
        parcours = new EcoParcours();
        parcours_Ue = new EcoUeParcours();
        semetre = new EcoSemetre();
        typeUE = new EcoTypeUe();
        ecue = new EcoEcue();
        equipePedagogiq = new EcoEquipePedagogique();
    }
    
    //getter et setter du Parcours
    public EcoParcours getParcours() {
        return parcours;
    }

    public void setParcours(EcoParcours parcours) {
        this.parcours = parcours;
    }
    
    //getter et setter de Ue_Parcours
    public EcoUeParcours getParcours_Ue() {
        return parcours_Ue;
    }

    public void setParcours_Ue(EcoUeParcours parcours_Ue) {
        this.parcours_Ue = parcours_Ue;
    }
   
    //getter et setter de semetre
     public EcoSemetre getSemetre() {    
        return semetre;
    }

    public void setSemetre(EcoSemetre semetre) {    
        this.semetre = semetre;
    }

    //getter et setter de typeUE
    public EcoTypeUe getTypeUE() {    
        return typeUE;
    }

    public void setTypeUE(EcoTypeUe typeUE) {    
        this.typeUE = typeUE;
    }        

    //getter et setter de ecue
    public EcoEcue getEcue() {    
        return ecue;
    }

    public void setEcue(EcoEcue ecue) {
        this.ecue = ecue;
    }
    
    //getter et setter de EquipePedagogiq
    public EcoEquipePedagogique getEquipePedagogiq() {
        return equipePedagogiq;
    }
    
    public void setEquipePedagogiq(EcoEquipePedagogique equipePedagogiq) {
        this.equipePedagogiq = equipePedagogiq;
    }
    
    //Liste de tous les parcours
    public List<EcoParcours> getListParcours() {
        try{
            listParcours = parcoursFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listParcours;
    }

    //Liste des codeParcours a afficher dans le combobox
    public List getListCodeParcours() {
        try{  
            listCodeParcours = new ArrayList();
            for(EcoParcours parcour : parcoursFacadeL.findAll()){          
              listCodeParcours.add(new SelectItem(parcour.getCodeParcours(), parcour.getCodeCycle()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeParcours;
    }
    
    //getter de ListParcours_Ue
    public List<EcoUeParcours> getListParcours_Ue() {    
        return listParcours_Ue;
    }

    //Liste des codeSemestres a afficher dans le combobox
    public List getListCodeSemetre() {
        try{  
            listCodeSemetre = new ArrayList();
            for(EcoSemetre semestr : semetreFacadeL.findAll()){
                listCodeSemetre.add(new SelectItem(semestr.getCodeSemetre(), semestr.getLibelleSemetre()));
            }
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listCodeSemetre;
    }
    
    //Liste des CodeTypeUe a afficher dans le combobox 
    public List getListCodeTypeUe() {
        try{  
            listCodeTypeUe = new ArrayList();
            for(EcoTypeUe type_ue : typeUeFacadeL.findAll()){          
              listCodeTypeUe.add(new SelectItem(type_ue.getCodeTypeUe(), type_ue.getLibelleTypeUe()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listCodeTypeUe;
    }
    
    //getter Liste des ECUE
    public List<EcoEcue> getListEcue() {        
        return listEcue;
    }

    //getter de listeEcue temporaire
    public List<EcoEcue> getListEcueTemporaire() {
        return listEcueTemporaire;
    }

    //getter de liste equipePedagogiue
    public List<EcoEquipePedagogique> getListEquipePedagogiq() {
        return listEquipePedagogiq;
    }        

    public String reinit_ecueTemporaire() {                
            ecue = new EcoEcue();

            return null;       
    }
    
    public String reinit_parcours_Ue() {                         
        //recuperation de l'heure systeme afin de la modifier et l'utiliser comme une variable entière
        TimeZone tz = TimeZone.getTimeZone("Africa/Abidjan");
        Calendar calendrier = Calendar.getInstance(tz);
        SimpleDateFormat formatH = new SimpleDateFormat("HH:mm:ss");
        formatH.setCalendar(calendrier);
        String heureActu = formatH.format(calendrier.getTime());
        
        int i = Integer.parseInt(heureActu.replaceAll(":", ""));
                
        //Ajout des donnée des champs et liste dans des variables
        parcours_Ue.setCodeUeParcours(i);
        parcours_Ue.setCodeSpecialite(parcours.getCodeSpecialite());

        for (EcoEcue ec : listEcueTemporaire) {
            if (ec.getCodeUeParcours() == null) {
                ec.setCodeUeParcours(i); 
                listEcue.add(ec);
            }            
        }
        
        equipePedagogiq.setCodeUeParcours(i);
        listEquipePedagogiq.add(equipePedagogiq);  
       
        //initialisation
        parcours_Ue = new EcoUeParcours();
        ecue = new EcoEcue();
        listEcueTemporaire = new ArrayList<>();
        equipePedagogiq = new EcoEquipePedagogique();                
        
        return null;              
    }
    
    // Méthode de réinitialisation de tous les champs du formulaire
    public void nouveau() { 
        parcours = new EcoParcours();
        parcours_Ue = new EcoUeParcours();
        ecue = new EcoEcue();
        listEcue = new ArrayList<>();
        listEcueTemporaire = new ArrayList<>();
        equipePedagogiq = new EcoEquipePedagogique();
        listParcours_Ue = new ArrayList<>();
        
    }
    
    //enregistrer un parcours
    public void enregistrer() { 
        //Enregistrer le parcours
        parcoursFacadeL.create(parcours);
        
        //Enregistrer les ue       
        for (EcoUeParcours parcour_ue : listParcours_Ue) {
            
            //variable de comparaison
            int parcour_ue_temporaire = parcour_ue.getCodeUeParcours();
            
            //réinitialisation de CodeUeParcours
            parcour_ue.setCodeUeParcours(null);
            
            parcour_ue.setCodeParcours(parcours.getCodeParcours());
            parcours_UeFacadeL.create(parcour_ue);
            
            //Enregistrer les ecues
            for (EcoEcue ecu : listEcue) {
                if(ecu.getCodeUeParcours() == parcour_ue_temporaire){
                    ecu.setCodeUeParcours(parcour_ue.getCodeUeParcours());                                        
                    ecueFacadeL.create(ecu);
                }                                
            }
            
            //Enregistrer l'equipe pédagogique
            for (EcoEquipePedagogique equipe_p : listEquipePedagogiq) {
                if(equipe_p.getCodeUeParcours() == parcour_ue_temporaire){
                    equipe_p.setCodeUeParcours(parcour_ue.getCodeUeParcours());
                    equipePedagogiqFacadeL.create(equipe_p);
                }                 
            }            
        }
        
        
//        if(ecue.getLibelleEcue() == null){
//            FacesMessage message = new FacesMessage("Veuillez saisir le libellé de l'ECUE!" );
//            message.setSeverity(FacesMessage.SEVERITY_ERROR);
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
        
        //réinitialisation de tous les champs du formulaire
        nouveau();
    }
    
    
    //Action ajax lors de la selection du cycle
    public void onCycleChange() {
        listCodeNiveau = new ArrayList();
        if(parcours.getCodeCycle() != null && !"".equals(parcours.getCodeCycle())){            
            List<EcoNiveau> data ;
            data = niveauFacadeL.findByCodeCycle(parcours.getCodeCycle());
            for(EcoNiveau niv : data){          
              listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
            }  
        }else listCodeNiveau = new ArrayList();        
    }
    
    //getter de la liste a aficher dans le combobox niveau
    public List getListCodeNiveau(){           
        listCodeNiveau = new ArrayList();
        if((parcours.getCodeCycle()) != null && !"".equals(parcours.getCodeCycle())){   
            for(EcoNiveau niv : niveauFacadeL.findAll()){   
                if (parcours.getCodeCycle().equals(niv.getCodeCycle())) {
                    listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
                }              
            }
            return listCodeNiveau;
        }else return new ArrayList();
    }
}
