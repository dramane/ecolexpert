/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoAcademiqueFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoClasseFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoFraisScolariteFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoInscriptionFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoMyinfoFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoNiveauFacadeLocal;
import com.mycompany.ecolexpert.ejb.ViewEtudiantInscriptionFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoAcademique;
import com.mycompany.ecolexpert.jpa.EcoClasse;
import com.mycompany.ecolexpert.jpa.EcoEtudiant;
import com.mycompany.ecolexpert.jpa.EcoFraisScolarite;
import com.mycompany.ecolexpert.jpa.EcoInscription;
import com.mycompany.ecolexpert.jpa.EcoMyinfo;
import com.mycompany.ecolexpert.jpa.EcoNiveau;
import com.mycompany.ecolexpert.jpa.ViewEtudiantInscription;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author HP
 */
@Named(value = "reinscriptionBean")
//@RequestScoped
@ViewScoped
public class reinscriptionBean implements Serializable{
    private static final String CHAMP_NOM  = "nomUtilisateur";
    private static final String CHAMP_PRENOM  = "prenomUtilisateur";      
    
    private EcoMyinfo myInfo;
    private EcoAcademique anneeAcademiq;
    
    private EcoInscription inscription;
    private ViewEtudiantInscription viewEtudiantInscription;
    
    private List listCodeNiveau;     
    private List listCodeClasse; 
    
    private List<ViewEtudiantInscription> listRechercheEtudiantInscription;
    
    // Injection de notre EJB (Session Bean Stateless)
//    @EJB private EcoFraisScolariteFacadeLocal fraisScolariteFacadeL;   
    @EJB private ViewEtudiantInscriptionFacadeLocal viewEtudiantInscriptionFacadeL;
    
    //Injection des EJB d'enregistrement d'une inscription
    @EJB private EcoInscriptionFacadeLocal inscriptionFacadeL;
    @EJB private EcoFraisScolariteFacadeLocal fraisScolariteFacadeL;   
    @EJB private EcoMyinfoFacadeLocal myInfoFacadeL;
    @EJB private EcoAcademiqueFacadeLocal anneeAcademiqFacadeL;
    @EJB private EcoNiveauFacadeLocal  niveauFacadeL;
    @EJB private EcoClasseFacadeLocal  classeFacadeL;

    /**
     * Creates a new instance of reinscriptionBean
     */
    public reinscriptionBean() {
        myInfo = new EcoMyinfo();    
        anneeAcademiq = new EcoAcademique();       
      
        //initialisation des classes d'enregistrement d'une inscription
        inscription = new EcoInscription();
        viewEtudiantInscription = new ViewEtudiantInscription();
    }
    
    //getter et settet des classes jpa
    public EcoMyinfo getMyInfo() {
        myInfo = myInfoFacadeL.findMyInfo_etablissementEnCours();
        return myInfo;  
    }

    public void setMyInfo(EcoMyinfo myInfo) {
        this.myInfo = myInfo;
    }

    public EcoAcademique getAnneeAcademiq() {
        anneeAcademiq = anneeAcademiqFacadeL.findAnneeEnCours();
        return anneeAcademiq;
    }

    public void setAnneeAcademiq(EcoAcademique anneeAcademiq) {
        this.anneeAcademiq = anneeAcademiq;
    }

    public EcoInscription getInscription() {
        return inscription;
    }

    public void setInscription(EcoInscription inscription) {
        this.inscription = inscription;
    }

    public ViewEtudiantInscription getViewEtudiantInscription() {
        return viewEtudiantInscription;
    }

    public void setViewEtudiantInscription(ViewEtudiantInscription viewEtudiantInscription) {
        this.viewEtudiantInscription = viewEtudiantInscription;
    }
    
    //getter et setter de la listRecherche de l'Etudiant et de l'inscription par le nom
    public List<ViewEtudiantInscription> getListRechercheEtudiantInscription() {
        return listRechercheEtudiantInscription;
    }

    public void setListRechercheEtudiantInscription(List<ViewEtudiantInscription> listRechercheEtudiantInscription) {
        this.listRechercheEtudiantInscription = listRechercheEtudiantInscription;
    }     
    
    //chargement des infos de l'etudiant en foncttion de son matricule saisi
    public void rechercheEtudiantParMatricule(){
        try {
            viewEtudiantInscription = viewEtudiantInscriptionFacadeL.findByMatricule(viewEtudiantInscription.getMatricule()); 
            System.out.println("L'ancien matricule est est : "+viewEtudiantInscription.getMatricule());
            inscription.setMatricule("");
            inscription.setCodeAcademique("");
            inscription.setCodeDomaine("");
            inscription.setCodeCycle("");
            inscription.setCodeNiveau("");
            inscription.setCodeClasse("");
            inscription.setCodeRegime("");

            montInscription = 0;
            montFormation = 0;
            montScolarte = 0;
            autreMontant = 0;
                      
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Accun étudiant n'a été trouvé! ");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, message); 
            
            viewEtudiantInscription = new ViewEtudiantInscription();
            inscription = new EcoInscription();
        }
    }
    
    //retourne la liste des etudiant recherche par le nom avec un jocker
    public List<ViewEtudiantInscription> rechercheEtudiantInscriptionParNomEtPrenom(){  
        try{             
            listRechercheEtudiantInscription = viewEtudiantInscriptionFacadeL.findEtudInscripByNomEtPrenomWithJocker(viewEtudiantInscription.getNomEtPrenom());            
//            listRechercheEtudiantInscription = viewEtudiantInscriptionFacadeL.findAll();
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
         return listRechercheEtudiantInscription;
    }
    
    //retourne l'inscription de l'etudiant
    public void selectEtudiantInscription(ViewEtudiantInscription viewEtudiantInscrip) {
        try{     
            viewEtudiantInscription = viewEtudiantInscriptionFacadeL.find(viewEtudiantInscrip.getIdinscription()); 
            inscription = new EcoInscription();
            
            montInscription = 0;
            montFormation = 0;
            montScolarte = 0;
            autreMontant = 0;            
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
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
        
        //Ajout du nouveau matricule de l'etudiant
        inscription.setMatricule(anneeAcademiq.getAnneeaca().substring(2, 4)+"/"+viewEtudiantInscription.getNumetu()+"/"+inscription.getCodeNiveau());
        
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
            
    //Enregistrement d'une réinscription
    public void creer(){     
        try {                           
            //Controle des valeurs saisies dans le formulaire encaissements
            if (viewEtudiantInscription.getMatricule() == null || "".equals(viewEtudiantInscription.getMatricule())) {
                FacesMessage message = new FacesMessage("Veuillez selectionner un étudiant");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);             
            }else if("".equals(viewEtudiantInscription.getNometu())){
                FacesMessage message = new FacesMessage("Veuillez selectionner un étudiant" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                
            }else if(inscription.getDtinscri() == null){
                FacesMessage message = new FacesMessage("Veuillez saisir la date de réinscription" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                
            }else if("".equals(inscription.getCours())){
                FacesMessage message = new FacesMessage("Veuillez sélectionner le type de cours de l'étudiant" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                
            }else if("".equals(inscription.getCodeAcademique())){
                FacesMessage message = new FacesMessage("Veuillez sélectionner le système académique de l'étudiant" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                                
            }else if("".equals(inscription.getCodeCycle())){
                FacesMessage message = new FacesMessage("Veuillez sélectionner le parcours/cycle suivi par l'étudiant" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                                             
            }else if("".equals(inscription.getCodeNiveau())){
                FacesMessage message = new FacesMessage("Veuillez sélectionner le niveau d'étude" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                                            
            }else if(!viewEtudiantInscription.getMatricule().substring(3, 9).equals(inscription.getMatricule().substring(3, 9))){
                inscription.setMatricule("");
                inscription.setCodeNiveau("");
                FacesMessage message = new FacesMessage("Veuillez resélectionner le niveau d'étude");
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                                            
            }else if("".equals(inscription.getCodeClasse())){
                FacesMessage message = new FacesMessage("Veuillez sélectionner la classe de l'étudiant" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                                     
            }else if("".equals(inscription.getCodeRegime())){
                FacesMessage message = new FacesMessage("Veuillez sélectionner le régime de l'étudiant" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                                    
            }else {
                List<EcoInscription> listInscription = inscriptionFacadeL.findAllByAnneeAcademiq(anneeAcademiq.getAnneeaca());
                            
                //on vérifie si l'étudiant a deja été inscrit dans l'année en cours  
                boolean trouver = false;
                for (EcoInscription inscript : listInscription) {
                    if (inscript.getMatricule().substring(0, 9).equals(inscription.getMatricule().substring(0, 9))){
                        FacesMessage message = new FacesMessage("Impossible de réinscrire cet étudiant. Car il à déjà été inscrire pour l'année académique en cours");
                        message.setSeverity(FacesMessage.SEVERITY_ERROR);
                        FacesContext.getCurrentInstance().addMessage(null, message); 
                        
                        trouver = true;
                        break; //Met fin a la boucle
                    }
                }
                
                if (trouver == false){
                    //Enregistrement de la réinscription dans la table eco_inscription
                    inscription.setAnneeaca(anneeAcademiq.getAnneeaca());
                    inscription.setIdacademique(anneeAcademiq.getIdacademique());            
        //            inscription.setMatricule(anneeAcademiq.getAnneeaca().substring(2, 4)+"/"+viewEtudiantInscription.getNumetu()+"/"+inscription.getCodeNiveau());
                    inscription.setNumetu(viewEtudiantInscription.getNumetu());
                    inscription.setOrigineRessource(viewEtudiantInscription.getOrigineRessource());
                    inscription.setProfessionEtu(viewEtudiantInscription.getProfessionEtu());
                    inscription.setActiviteEtu(viewEtudiantInscription.getActiviteEtu());
                    inscription.setDomaineAtiviteEtu(viewEtudiantInscription.getDomaineAtiviteEtu());
                    inscription.setLangueVivante(viewEtudiantInscription.getLangueVivante());
                    inscription.setDemandeurEmploi(viewEtudiantInscription.getDemandeurEmploi());
                    inscription.setInterruptionEtude(viewEtudiantInscription.getInterruptionEtude());
                    inscription.setDureeInterruption(viewEtudiantInscription.getDureeInterruption());
                    inscription.setSportifHautNivo(viewEtudiantInscription.getSportifHautNivo());
                    inscription.setServiceNational(viewEtudiantInscription.getServiceNational());
                    inscription.setDernierEtablisFreqte(viewEtudiantInscription.getDernierEtablisFreqte());
                    inscription.setAnneeEtablisFreqte(viewEtudiantInscription.getAnneeEtablisFreqte());

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
                    FacesMessage message = new FacesMessage("La réinscription de "+viewEtudiantInscription.getNometu()+" "+viewEtudiantInscription.getPrenetu()+" à été effectué avec succès");
                    message.setSeverity(FacesMessage.SEVERITY_INFO);
                    FacesContext.getCurrentInstance().addMessage(null, message); 

                    //Initialisation des champs a null après un enregistrement  
                    viewEtudiantInscription = new ViewEtudiantInscription();
                    inscription = new EcoInscription();

                    montInscription = 0;
                    montFormation = 0;
                    montScolarte = 0;
                    autreMontant = 0;
                }              
            }            
        }catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    } 
}
