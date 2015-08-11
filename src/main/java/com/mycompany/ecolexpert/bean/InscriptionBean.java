/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoAcademiqueFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoClasseFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoEtudiantFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoFraisScolariteFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoInscriptionFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoMyinfoFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNumetudFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoTuteurFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoAcademique;
import com.mycompany.ecolexpert.jpa.EcoClasse;
import com.mycompany.ecolexpert.jpa.EcoEtudiant;
import com.mycompany.ecolexpert.jpa.EcoFraisScolarite;
import com.mycompany.ecolexpert.jpa.EcoInscription;
import com.mycompany.ecolexpert.jpa.EcoMyinfo;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
import com.mycompany.ecolexpert.jpa.EcoNumetud;
import com.mycompany.ecolexpert.jpa.EcoTuteur;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.enterprise.context.NormalScope;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author HP
 */
@Named(value = "inscriptionBean")
@ViewScoped
public class InscriptionBean implements Serializable{
    private static final String CHAMP_NOM  = "nomUtilisateur";
    private static final String CHAMP_PRENOM  = "prenomUtilisateur";       

    private List<EcoFraisScolarite> listFraisScolarite;
    private List listCodeFraisScolarite;  
    private EcoMyinfo myInfo;
    private EcoAcademique anneeAcademiq;
    
    private List listCodeNiveau; 
    
    private List listCodeClasse; 
    
    //Classe d'enregistrement d'une inscription
    private EcoInscription inscription;
    private EcoEtudiant etudiant;
    private EcoTuteur tuteur;
    private EcoNumetud numEtud;
    
    //variable contenant le ficher photo de l'etudiant
    private UploadedFile fichierPhoto;
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoFraisScolariteFacadeLocal fraisScolariteFacadeL;   
    @EJB private EcoMyinfoFacadeLocal myInfoFacadeL;
    @EJB private EcoAcademiqueFacadeLocal anneeAcademiqFacadeL;
    @EJB private EcoNiveauFacadeLocal  niveauFacadeL;
    @EJB private EcoClasseFacadeLocal  classeFacadeL;
    
    //Injection des EJB d'enregistrement d'une inscription
    @EJB private EcoInscriptionFacadeLocal inscriptionFacadeL;
    @EJB private EcoEtudiantFacadeLocal etudiantFacadeL;
    @EJB private EcoTuteurFacadeLocal tuteurFacadeL;
    @EJB private EcoNumetudFacadeLocal numetudFacadeL;
    
    /**
     * Creates a new instance of InscriptionBean
     */
    
    public InscriptionBean() {
        myInfo = new EcoMyinfo();    
        anneeAcademiq = new EcoAcademique();       
      
        //initialisation des classes d'enregistrement d'une inscription
        inscription = new EcoInscription();
        etudiant = new EcoEtudiant();
        tuteur = new EcoTuteur();
        numEtud = new EcoNumetud();
    }
    
    //getter et setter de myinfo
    public EcoMyinfo getMyInfo() {
        myInfo = myInfoFacadeL.findMyInfo_etablissementEnCours();
        return myInfo;           
    }

    public void setMyInfo(EcoMyinfo myInfo) {        
        this.myInfo = myInfo;
    }

    //getter et setter de l'année academique
    public EcoAcademique getAnneeAcademiq() {
        anneeAcademiq = anneeAcademiqFacadeL.findAnneeEnCours();
        return anneeAcademiq;
    }

    public void setAnneeAcademiq(EcoAcademique anneeAcademiq) {
        this.anneeAcademiq = anneeAcademiq;
    } 
    
    //getter et setter des classes d'enregistrement d'une inscription
    public EcoInscription getInscription() {
        return inscription;
    }

    public void setInscription(EcoInscription inscription) {
        this.inscription = inscription;
    }

    public EcoEtudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EcoEtudiant etudiant) {
        this.etudiant = etudiant;
    }

    public EcoTuteur getTuteur() {
        return tuteur;
    }

    public void setTuteur(EcoTuteur tuteur) {
        this.tuteur = tuteur;
    }

    public EcoNumetud getNumEtud() {
        return numEtud;
    }

    public void setNumEtud(EcoNumetud numEtud) {
        this.numEtud = numEtud;
    }    
    
    //getter et setter de la variable contenant le ficher photo de l'etudiant
    public UploadedFile getFichierPhoto() {
        return fichierPhoto;
    }

    public void setFichierPhoto(UploadedFile fichierPhoto) {
        this.fichierPhoto = fichierPhoto;
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

    //recuperation du fichier photo et transformation du type en byte[]
    private byte[] uploadFichier(UploadedFile file){ 
        byte[] image = null;
        try {
            if (file != null) {
                InputStream stream = file.getInputstream();                                
                image = IOUtils.toByteArray(stream); // Apache commons IO.                              
            }
            else{
                FacesMessage msg = new FacesMessage("Veuillez choisir la photo de l'étudiant!!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }                  
        } catch (Exception e) {
            System.out.println("Exception-File Upload." + e.getMessage());
        }  
        
        return image;
    }
    
    
    
    //Chargement et calcule des frais de la scolarité normale sans reduction
    public void calculFraisInscription(){
        String etablissementEnCours = myInfoFacadeL.findMyInfo_etablissementEnCours().getCodeMyinfo();
        int AnneeEnCours = anneeAcademiqFacadeL.findAnneeEnCours().getIdacademique();
        List<EcoFraisScolarite> data1 ;        
        List <EcoFraisScolarite> data2;
        
        //Retourne tous les frais liés au regime "Normal", sans aucune reduction        
        data1 = fraisScolariteFacadeL.findByCodeCycleCodeRegime(etablissementEnCours, AnneeEnCours, inscription.getCodeCycle(), inscription.getCodeNiveau(), "Cas-Normal");
        int scolariteNormale = 0;
        for(EcoFraisScolarite frais : data1){
            if("Inscription".equals(frais.getCodeElementFrais())){                
//                inscription.setFormationAPaye(frais.getMontantFrais());
                scolariteNormale += frais.getMontantFrais();
            } else if("Formation".equals(frais.getCodeElementFrais())) {                
                inscription.setDrtformaNormal(frais.getMontantFrais());
                scolariteNormale += frais.getMontantFrais();
            }           
        }
        inscription.setScolariteNormal(scolariteNormale);
                
        //Scolarité en fonction du regime de l'etudiant
        int scolariteApayer = 0;
        
        //Retourne tous les frais liés à l'etablisement, l'année academique en cours, le cycle, le niveau et le regime
        data2 = fraisScolariteFacadeL.findByCodeCycleCodeRegime(etablissementEnCours, AnneeEnCours, inscription.getCodeCycle(), inscription.getCodeNiveau(), inscription.getCodeRegime());
        for(EcoFraisScolarite frais : data2){                           
            if("Inscription".equals(frais.getCodeElementFrais())) {                
                inscription.setInscriptionAPaye(frais.getMontantFrais());
                scolariteApayer += frais.getMontantFrais();
            }else if("Formation".equals(frais.getCodeElementFrais())){                
                inscription.setFormationAPaye(frais.getMontantFrais());
                scolariteApayer += frais.getMontantFrais();
            }else if("Assurance".equals(frais.getCodeElementFrais())){
                inscription.setAssuranceAPaye(frais.getMontantFrais());
            }else if("Cantine".equals(frais.getCodeElementFrais())){
                inscription.setCantineAPaye(frais.getMontantFrais());
            }else if("Examen".equals(frais.getCodeElementFrais())){
                inscription.setExamenAPaye(frais.getMontantFrais());
            }else if("Fourniture".equals(frais.getCodeElementFrais())){
                inscription.setFournitureAPaye(frais.getMontantFrais());
            }else if("Informatique".equals(frais.getCodeElementFrais())){
                inscription.setInformatiqueAPaye(frais.getMontantFrais());
            }else if("Medical".equals(frais.getCodeElementFrais())){
                inscription.setMedicalAPaye(frais.getMontantFrais());
            }else if("Soutenance".equals(frais.getCodeElementFrais())){
                inscription.setSoutenanceAPaye(frais.getMontantFrais());
            }else if("Timbre".equals(frais.getCodeElementFrais())){
                inscription.setTimbreAPaye(frais.getMontantFrais());
            }else if("Transport".equals(frais.getCodeElementFrais())){
                inscription.setTransportAPaye(frais.getMontantFrais());
            }              
        }
        inscription.setScolariteAPaye(scolariteApayer);
    }
            
    //Enregistrement  d'une inscription
    public void creer(){
        try {             
            //Enregistrement de numetud;                        
            numEtud.setLibelle(etudiant.getNometu()+" "+etudiant.getPrenetu());         
            numetudFacadeL.create(numEtud);
            
            //modification de numetud afin d'ajouter le numero de compte
            
                //Recuperation du dernier enregistrement de numetud           
                String idNumetu = numEtud.getIdnumetu().toString();
                String compte = null ;
                int longueur = idNumetu.length();
                String numetu_val_concat = null;

                if (longueur == 6){
                   numetu_val_concat = idNumetu;
                   compte = ("411").concat(numetu_val_concat);
                }else if (longueur == 5){
                   numetu_val_concat = ("0").concat(idNumetu);
                   compte = ("411").concat(numetu_val_concat);
                }else if (longueur == 4){
                   numetu_val_concat = "00".concat(idNumetu);
                   compte = ("411").concat(numetu_val_concat);
                }else if (longueur == 3){
                    numetu_val_concat = "000".concat(idNumetu);
                    compte = ("411").concat(numetu_val_concat);
                }else if (longueur == 2){
                    numetu_val_concat = "0000".concat(idNumetu);
                    compte = ("411").concat(numetu_val_concat);
                }else if (longueur == 1){
                    numetu_val_concat = "00000".concat(idNumetu);
                    compte = ("411").concat(numetu_val_concat);
                }  
                
            numEtud.setCompte(compte);
            numetudFacadeL.edit(numEtud);

            //Enregistrement d'un etudiant
            etudiant.setNumetu(numetu_val_concat);
            etudiant.setIdnumetu(Integer.parseInt(idNumetu));  
            etudiant.setPhoto(uploadFichier(fichierPhoto));            
            etudiant.setCompte("411"+numetu_val_concat);
            etudiant.setIntitule(numEtud.getLibelle());
            etudiant.setTypeCompte("0".charAt(0));

            if("M.".equals(etudiant.getCivilite())){
                etudiant.setSexe("M".charAt(0));
            }else etudiant.setSexe("F".charAt(0));

            etudiantFacadeL.create(etudiant);

            //Enregistrement du tuteur de l'étudiant
            tuteur.setNumetu(numetu_val_concat);
            tuteurFacadeL.create(tuteur);

            //Enregistrement de l'inscription dans la table eco_inscription
            inscription.setAnneeaca(anneeAcademiq.getAnneeaca());
            inscription.setIdacademique(anneeAcademiq.getIdacademique());
            inscription.setMatricule(anneeAcademiq.getAnneeaca().substring(2, 4)+"/"+numetu_val_concat+"/"+inscription.getCodeNiveau());
            inscription.setNumetu(numetu_val_concat);

            //Récupération du context et de la session
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();        
            HttpSession session = (HttpSession) context.getSession(true); 
            inscription.setRespins(session.getAttribute(CHAMP_NOM)+" "+session.getAttribute(CHAMP_PRENOM));
            
            //recuperation de l'heure systeme
            TimeZone tz = TimeZone.getTimeZone("Africa/Abidjan");
            Calendar calendrier = Calendar.getInstance(tz);
            SimpleDateFormat formatH = new SimpleDateFormat("HH:mm");
            formatH.setCalendar(calendrier);
            String heureActu = formatH.format(calendrier.getTime());

            inscription.setHInscri(heureActu);

                //les codes des clé primaire sont recuperés automatiquement sur le formulaire

                //Appel de la méthode de calcul et d'affectation des montant à payés
            calculFraisInscription();
            inscriptionFacadeL.create(inscription);            

            //Notification d'inscription
            FacesMessage message = new FacesMessage("L'inscription de "+etudiant.getCivilite()+" "+etudiant.getNometu()+" "+etudiant.getPrenetu()+" à été effectué avec succès");
            message.setSeverity(FacesMessage.SEVERITY_INFO);
            FacesContext.getCurrentInstance().addMessage(null, message); 

            //Initialisation des champs a null après un enregistrement  
            numEtud = new EcoNumetud();
            etudiant = new EcoEtudiant();
            tuteur = new EcoTuteur();
            inscription = new EcoInscription();

            montInscription = 0;
            montFormation = 0;
            montScolarte = 0;
            autreMontant = 0;
  
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }
        
    //getter de la liste a aficher dans le combobox niveau
    public List getListCodeNiveau(){           
        listCodeNiveau = new ArrayList();
        if((inscription.getCodeCycle()) != null && !"".equals(inscription.getCodeCycle())){   
            for(EcoNiveau niv : niveauFacadeL.findAll()){   
                if (inscription.getCodeCycle().equals(niv.getCodeCycle())) {
                    listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
                }              
            }
            return listCodeNiveau;
        }else return new ArrayList();
    }
    
    //getter de la liste a aficher dans le combobox classe
    public List getListCodeClasse() {
        listCodeClasse = new ArrayList();
        if((inscription.getCodeNiveau()) != null && !"".equals(inscription.getCodeNiveau())){   
            for(EcoClasse clas : classeFacadeL.findAll()){   
                if (inscription.getCodeNiveau().equals(clas.getCodeNiveau())) {
                    listCodeClasse.add(new SelectItem(clas.getCodeClasse(), clas.getNomclasse()));
                }              
            }
            return listCodeClasse;
        }else return new ArrayList();
    }
    
    //Action ajax lors de la selection du cycle
    public void onCycleChange() {
        listCodeNiveau = new ArrayList();
        if(inscription.getCodeCycle() != null && !"".equals(inscription.getCodeCycle())){            
            List<EcoNiveau> data ;
            data = niveauFacadeL.findByCodeCycle(inscription.getCodeCycle());
            for(EcoNiveau niv : data){          
              listCodeNiveau.add(new SelectItem(niv.getCodeNiveau(), niv.getCodeNiveau()));
            }  
        }else listCodeNiveau = new ArrayList();
        
        inscription.setCodeNiveau("");
        inscription.setCodeClasse("");
        inscription.setCodeRegime("");
        montInscription = 0;
        montFormation = 0;
        montScolarte = 0;
        autreMontant = 0;  
    }
    
    //événement ajax l'ors du clic sur le combobox classe
    public void onNiveauChange() {
        listCodeClasse = new ArrayList();
        if(inscription.getCodeNiveau() != null && !"".equals(inscription.getCodeNiveau())){            
            List<EcoClasse> data ;
            data = classeFacadeL.findByCodeNiveau(inscription.getCodeNiveau());
            for(EcoClasse clas : data){          
              listCodeClasse.add(new SelectItem(clas.getCodeClasse(), clas.getNomclasse()));
            }  
        }else listCodeClasse = new ArrayList();                         
                
        inscription.setCodeClasse("");
        inscription.setCodeRegime("");            
        montInscription = 0;
        montFormation = 0;
        montScolarte = 0;
        autreMontant = 0;   
    }         
    
    //Action ajax lors de la selection du classe
    public void onClasseChange(){
        inscription.setCodeRegime("");            
        montInscription = 0;
        montFormation = 0;
        montScolarte = 0;
        autreMontant = 0;     
    }
    
    //Action ajax lors de la selection du regime afin d'afficher les mondants
    public void onRegimeChange(){
        int inscriptions = 0;
        int formation = 0;
        int autreFrais = 0;
        int scolarite = 0;
        
        String etablissementEnCours = myInfoFacadeL.findMyInfo_etablissementEnCours().getCodeMyinfo();
        int AnneeEnCours = anneeAcademiqFacadeL.findAnneeEnCours().getIdacademique();
        List<EcoFraisScolarite> data ;
        
        //Retourne tous les frais liés à l'etablisement, l'année academique en cours, le cycle, le niveau et le regime
        data = fraisScolariteFacadeL.findByCodeCycleCodeRegime(etablissementEnCours, AnneeEnCours, inscription.getCodeCycle(), inscription.getCodeNiveau(), inscription.getCodeRegime());
        for(EcoFraisScolarite frais : data){                           
            if("Inscription".equals(frais.getCodeElementFrais())) {
                inscriptions = frais.getMontantFrais();
                setMontInscription(inscriptions);                
            }else if("Formation".equals(frais.getCodeElementFrais())){
                formation = frais.getMontantFrais();
                setMontFormation(formation);
            }else autreFrais += frais.getMontantFrais();
            
        }
        setAutreMontant(autreFrais);
        scolarite = inscriptions+formation;
        setMontScolarte(scolarite);
    }
    
    //variable des montants
    private int montInscription;
    private int montFormation;
    private int autreMontant;
    private int montScolarte;
    
    //getter et setter des montants
    public int getMontInscription() {
        return montInscription;
    }

    public void setMontInscription(int montInscription) {
        this.montInscription = montInscription;
    }

    public int getMontFormation() {
        return montFormation;
    }

    public void setMontFormation(int montFormation) {
        this.montFormation = montFormation;
    }

    public int getAutreMontant() {
        return autreMontant;
    }

    public void setAutreMontant(int autreMontant) {
        this.autreMontant = autreMontant;
    }

    public int getMontScolarte() {
        return montScolarte;
    }

    public void setMontScolarte(int montScolarte) {
        this.montScolarte = montScolarte;
    }
    
}
