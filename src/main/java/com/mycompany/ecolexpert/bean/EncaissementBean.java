/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoEtudiantFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoInscriptionFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoSessionFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoVersementFacadeLocal;
import com.mycompany.ecolexpert.ejb.ViewEtudiantInscriptionFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoEtudiant;
import com.mycompany.ecolexpert.jpa.EcoInscription;
import com.mycompany.ecolexpert.jpa.EcoSession;
import com.mycompany.ecolexpert.jpa.EcoVersement;
import com.mycompany.ecolexpert.jpa.ViewEtudiantInscription;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author TOSHIBA
 */
@Named(value = "encaissementBean")
//@RequestScoped
@ViewScoped
public class EncaissementBean implements Serializable{
    private static final String CHAMP_NOM  = "nomUtilisateur";
    private static final String CHAMP_PRENOM  = "prenomUtilisateur";   
    
//    private EcoMyinfo myInfo;
//    private EcoAcademique anneeAcademiq;
    
    private EcoInscription inscription;
    private EcoVersement versement;
    private EcoSession sessionCpt;
    private ViewEtudiantInscription viewEtudiantInscription;
    
    private List<ViewEtudiantInscription> listRechercheEtudiantInscription;
    
    // Injection de notre EJB (Session Bean Stateless)
//    @EJB private EcoFraisScolariteFacadeLocal fraisScolariteFacadeL;   
    @EJB private ViewEtudiantInscriptionFacadeLocal viewEtudiantInscriptionFacadeL;
    
    //Injection des EJB d'enregistrement d'une inscription
    @EJB private EcoInscriptionFacadeLocal inscriptionFacadeL;
    @EJB private EcoVersementFacadeLocal versementFacadeL;
    @EJB private EcoSessionFacadeLocal sessionCptFacadeL;

    /**
     * Creates a new instance of EncaissementBean
     */
    public EncaissementBean() {
//        myInfo = new EcoMyinfo();    
//        anneeAcademiq = new EcoAcademique();       
      
        //initialisation des classes d'enregistrement d'une inscription
        inscription = new EcoInscription();
        versement = new EcoVersement();
        sessionCpt = new EcoSession();
        viewEtudiantInscription = new ViewEtudiantInscription();
    }
    
    //variable de visiblité et d'activation/desactivation des champs du formulaire encaissement
    String mode_versement = "Espèce";
    boolean visibilite_reglement_espece = true;
    boolean visibilite_reglement_cheq = false;
    boolean visibilite_reglement_virement = false;
    boolean visibilite_reglement_banq = false;
    
    //getter et setter de de mes variables de visibilité
    public String getMode_versement() {
        return mode_versement;
    }

    public void setMode_versement(String mode_versement) {
        this.mode_versement = mode_versement;
    }

    public boolean isVisibilite_reglement_espece() {
        return visibilite_reglement_espece;
    }

    public void setVisibilite_reglement_espece(boolean visibilite_reglement_espece) {
        this.visibilite_reglement_espece = visibilite_reglement_espece;
    }

    public boolean isVisibilite_reglement_cheq() {
        return visibilite_reglement_cheq;
    }

    public void setVisibilite_reglement_cheq(boolean visibilite_reglement_cheq) {
        this.visibilite_reglement_cheq = visibilite_reglement_cheq;
    }            

    public boolean isVisibilite_reglement_virement() {
        return visibilite_reglement_virement;
    }

    public void setVisibilite_reglement_virement(boolean visibilite_reglement_virement) {
        this.visibilite_reglement_virement = visibilite_reglement_virement;
    }

    public boolean isVisibilite_reglement_banq() {
        return visibilite_reglement_banq;
    }

    public void setVisibilite_reglement_banq(boolean visibilite_reglement_banq) {
        this.visibilite_reglement_banq = visibilite_reglement_banq;
    }    
    
    //getter et setter de versement, etudiant, inscription
    public EcoInscription getInscription() {
        return inscription;
    }

    public void setInscription(EcoInscription inscription) {
        this.inscription = inscription;
    }

    public EcoVersement getVersement() {
        return versement;
    }

    public void setVersement(EcoVersement versement) {
        this.versement = versement;
    }  
    
    //getter et setter ViewEtudiantInscription
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
    
    //code ajax produit lors de la selection du mode de versement
    public void modeVersementValueChanged() {
       if (null != mode_versement) switch (mode_versement) {
           case "Espèce":
               visibilite_reglement_espece = true;
               visibilite_reglement_cheq = false;
               visibilite_reglement_virement = false;
               visibilite_reglement_banq = false;
               break;
           case "Chèque":
               visibilite_reglement_espece = false;
               visibilite_reglement_cheq = true;
               visibilite_reglement_virement = false;
               visibilite_reglement_banq = false;
               break;
            case "Virement":
               visibilite_reglement_espece = false;
               visibilite_reglement_cheq = false;
               visibilite_reglement_virement = true;
               visibilite_reglement_banq = false;
               break;
            case "Banque":
               visibilite_reglement_espece = false;
               visibilite_reglement_cheq = false;
               visibilite_reglement_virement = false;
               visibilite_reglement_banq = true;
               break;
       }            
    }   
    
    //chargement des infos de l'etudiant en foncttion de son matricule saisi
    public void rechercheEtudiantParMatricule(){
        try {
            System.out.println("le matricule est est : "+versement.getMatriEtu());
            viewEtudiantInscription = viewEtudiantInscriptionFacadeL.findByMatricule(viewEtudiantInscription.getMatricule()); 
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Accun étudiant n'a été trouvé! ");
            message.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext.getCurrentInstance().addMessage(null, message); 
            
            viewEtudiantInscription = new ViewEtudiantInscription();
        }                
    }
    
    //retourne la liste des etudiant recherche par le nom avec un jocker
    public List<ViewEtudiantInscription> rechercheEtudiantInscriptionParNomEtPrenom(){  
        try{             
            listRechercheEtudiantInscription = viewEtudiantInscriptionFacadeL.findEtudInscripByNomEtPrenomWithJocker(viewEtudiantInscription.getNomEtPrenom());
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
         return listRechercheEtudiantInscription;
    }
        
     //Enregistrement d'un encaissement
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
            }else if(versement.getDateReglement() == null){
                FacesMessage message = new FacesMessage("Veuillez saisir la date du versement" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                
            }else if(versement.getMontant() == null || versement.getMontant() == 0){
                FacesMessage message = new FacesMessage("Veuillez saisir le montant du versement" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                
            }else if("".equals(versement.getMotifReglement())){
                FacesMessage message = new FacesMessage("Veuillez saisir le motif du versement" );
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);                
                FacesContext.getCurrentInstance().addMessage(null, message);                
            }else {
            
                //Affectation des données du versement dans les champs
                versement.setNumetu(viewEtudiantInscription.getNumetu());
                versement.setMatriEtu(viewEtudiantInscription.getMatricule());
                versement.setAnneesco(viewEtudiantInscription.getAnneeaca());
                versement.setCodeCycle(viewEtudiantInscription.getCodeCycle());
                versement.setCodeNiveau(viewEtudiantInscription.getCodeNiveau());
                versement.setCodeClasse(viewEtudiantInscription.getCodeClasse());
                versement.setCours(viewEtudiantInscription.getCours());
                versement.setModeReglement(getMode_versement());

                if ("Jour".equals(viewEtudiantInscription.getCours())) {
                    versement.setFormation("Initiale");
                } else if ("Soir".equals(viewEtudiantInscription.getCours())){
                    versement.setFormation("Continue");
                }
                
                //Récupération du context et de la session
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();        
                HttpSession session = (HttpSession) context.getSession(true); 
                versement.setCaissiere(session.getAttribute(CHAMP_NOM)+" "+session.getAttribute(CHAMP_PRENOM));
                
                //recuperation de l'heure systeme
                TimeZone tz = TimeZone.getTimeZone("Africa/Abidjan");
                Calendar calendrier = Calendar.getInstance(tz);
                SimpleDateFormat formatH = new SimpleDateFormat("HH:mm");
                formatH.setCalendar(calendrier);
                String heureActu = formatH.format(calendrier.getTime());

                versement.setHeureReglement(heureActu);                                
                versement.setAnnule("N");               

                //Versement dateVersement, montant, dateCheq, numCheq, emetteur sont recuperé dans le formulaire
                
                //enreistrement de l'encaissement/versement 
                versementFacadeL.create(versement);
                
                //modification de l'encaissement afin d'ajouté le numero du réçu
                String numRecu = versement.getMatriEtu().substring(0, 2)+"-"+versement.getIdversement();
                versement.setNumressu(numRecu);
                versementFacadeL.edit(versement);
                
                //chargement des infos d'inscription de l'étudiant
                inscription = inscriptionFacadeL.findByMatricule(viewEtudiantInscription.getMatricule());
                
                //Ajout (modification) des montant du frais concerné par le versement dans la table Inscription de l'étudiant
                if("Inscription".equals(versement.getMotifReglement())) {
                    inscription.setInscriptionPaye(inscription.getInscriptionPaye()+versement.getMontant());
                    inscription.setScolaritePaye(inscription.getScolaritePaye()+versement.getMontant());
                           
                    inscriptionFacadeL.edit(inscription);
                }else if ("Formation".equals(versement.getMotifReglement())) {
                    inscription.setFormationPaye(inscription.getFormationPaye()+versement.getMontant());
                    inscription.setScolaritePaye(inscription.getScolaritePaye()+versement.getMontant());
                                        
                    inscriptionFacadeL.edit(inscription);
                }else if ("Assurance".equals(versement.getMotifReglement())) {
                    inscription.setAssurancePaye(inscription.getAssurancePaye()+versement.getMontant());                                       
                    inscriptionFacadeL.edit(inscription);
                }else if ("Cantine".equals(versement.getMotifReglement())) {
                    inscription.setCantinePaye(inscription.getCantinePaye()+versement.getMontant());                                        
                    inscriptionFacadeL.edit(inscription);
                }else if ("Examen".equals(versement.getMotifReglement())) {
                    inscription.setExamenPaye(inscription.getExamenPaye()+versement.getMontant());                                       
                    inscriptionFacadeL.edit(inscription);
                }else if ("Fourniture".equals(versement.getMotifReglement())) {
                    inscription.setFourniturePaye(inscription.getFourniturePaye()+versement.getMontant());                                       
                    inscriptionFacadeL.edit(inscription);
                }else if ("Informatique".equals(versement.getMotifReglement())) {
                    inscription.setInformatiquePaye(inscription.getInformatiquePaye()+versement.getMontant());                                        
                    inscriptionFacadeL.edit(inscription);
                }else if ("Medical".equals(versement.getMotifReglement())) {
                    inscription.setMedicalPaye(inscription.getMedicalPaye()+versement.getMontant());                    
                    System.out.println("Versement : "+versement.getMotifReglement()+" , montant : "+versement.getMontant());
                    inscriptionFacadeL.edit(inscription);
                }else if ("Soutenance".equals(versement.getMotifReglement())) {
                    inscription.setSoutenancePaye(inscription.getSoutenancePaye()+versement.getMontant());                                       
                    inscriptionFacadeL.edit(inscription);
                }else if ("Timbre".equals(versement.getMotifReglement())) {
                    inscription.setTimbrePaye(inscription.getTimbrePaye()+versement.getMontant());                    
                    inscriptionFacadeL.edit(inscription);
                }else if ("Transport".equals(versement.getMotifReglement())) {
                    inscription.setTransportPaye(inscription.getTransportPaye()+versement.getMontant());                                        
                    inscriptionFacadeL.edit(inscription);
                }   
                
                //Enregistrement de la session comptable
                //Enregistrement de la 2ème ecriture comptable dans la table session
                    // get a calendar using the default time zone and locale.                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(viewEtudiantInscription.getDtinscri());
                System.out.println(calendar.get(Calendar.YEAR));
                String stringVal = String.valueOf(calendar.get(Calendar.YEAR));
                sessionCpt.setExercice(stringVal);
                sessionCpt.setDateSession(viewEtudiantInscription.getDtinscri());
                sessionCpt.setCompte("571000000");
                sessionCpt.setLibelle(viewEtudiantInscription.getNometu()+" "+viewEtudiantInscription.getPrenetu());
                sessionCpt.setDebit(versement.getMontant());
                sessionCpt.setCredit(0);
                
                if ("Espèce".equals(versement.getModeReglement())) {
                    sessionCpt.setTypeVersement(0);
                }else if ("Chèque".equals(versement.getModeReglement())){
                    sessionCpt.setTypeVersement(1);
                }else if ("Virement".equals(versement.getModeReglement())){
                    sessionCpt.setTypeVersement(2);
                }else if ("Banque".equals(versement.getModeReglement())){
                    sessionCpt.setTypeVersement(3);
                }
                
                sessionCpt.setNumressu(numRecu);
                sessionCpt.setMatriEtu(versement.getMatriEtu());                    
                sessionCpt.setAnnule("N");                            
                
                sessionCptFacadeL.create(sessionCpt);
                
                //modification dans la table Eco_Session afin d'ajouter la PIECE comptable a la prémiere ecriture
                String pieceCpt = sessionCpt.getIdsession().toString();
                sessionCpt.setPiece(pieceCpt);
                
                sessionCptFacadeL.edit(sessionCpt); 
                
                //Enregistrement de la 2ème ecriture comptable dans la table session
                sessionCpt.setCompte("411"+viewEtudiantInscription.getNumetu());
                sessionCpt.setLibelle("Versement "+versement.getModeReglement()+". "+versement.getMotifReglement());
                sessionCpt.setDebit(0);
                sessionCpt.setCredit(versement.getMontant());
                
                sessionCptFacadeL.create(sessionCpt);

                //notification
                FacesMessage message = new FacesMessage("Encaissement enregistré avec succès. Le numéro du reçu est : "+numRecu);
                FacesContext.getCurrentInstance().addMessage(null, message); 
                
                //Impression réçu
//                executeReport("14-5", "2014/2015");
                
                //Réinitialisation des champs du formulaire
//                viewEtudiantInscription = new ViewEtudiantInscription();
                viewEtudiantInscription.setMatricule(null);
                inscription = new EcoInscription();
//                versement = new EcoVersement();
                sessionCpt = new EcoSession();
                mode_versement = "Espèce";
                visibilite_reglement_espece = true;
                visibilite_reglement_cheq = false;
                visibilite_reglement_virement = false;
                visibilite_reglement_banq = false;
                
//                montApayer = 0;
                montRecu = 0;
                montArendre = 0;
                
            }
        } catch (Exception e) {
            System.err.println("Erreur capturée : "+e);
        }
    }          
    
    //variables du montant a payer, du montant reçu et du montant à rendre
    private int montApayer;
    private int montRecu;
    private int montArendre;
        
    //getter et setter des variables du montant a payer, du montant reçu et du montant à rendre
    public int getMontApayer() {
        return montApayer;
    }

    public void setMontApayer(int montApayer) {    
        this.montApayer = montApayer;
    }

    public int getMontRecu() {
        return montRecu;
    }

    public void setMontRecu(int montRecu) {
        this.montRecu = montRecu;
    }

    public int getMontArendre() {
        return montArendre;
    }

    public void setMontArendre(int montArendre) {
        this.montArendre = montArendre;
    }
    
    //Methode ajax qui affiche le montant a payer
    public void AffichMontantApayer(){
        setMontApayer(this.getVersement().getMontant());
//        montApayer = versement.getMontant();
    }
    
    //Methode ajax qui calcule et affiche le montant a rendre
    public void calculMontantArendre(){
        if (this.getMontApayer() == 0) {
            //Notification 
            FacesMessage message = new FacesMessage("Veuillez saisir le montant à payer");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else {
            if (this.getMontRecu() < this.getMontApayer()) {
                //Notification 
                FacesMessage message = new FacesMessage("Le montant réçu doit être supérieur ou égale à "+this.montApayer);
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                setMontArendre(this.getMontRecu() - this.getMontApayer());
            }  
        }
    }
    
    public void afficheRecu(){
        try {
//        executeReport("14-5", "2014/2015");
            String numRecu = versement.getMatriEtu().substring(0, 2)+"-"+versement.getIdversement();
            executeReport(numRecu, versement.getAnneesco());
        
        }catch (NullPointerException ex) {
            System.err.println("Erreur capturé : "+ex);
        }
    }
    
    //Affichage et impression du réçu
    public void executeReport(String numRecu, String anneeAca){
        //C:\\Users\\HP\\Documents\\NetBeansProjects\\projettest\\src\\main\\webapp\\resources\\report\\design\\
        // - Paramètres de connexion à la base de données
            String url = "jdbc:mysql://localhost:3306/ecole_expert";
            String login = "dramane";
            String password = "admin";
  
            Connection conn = null;
            byte[] bytes = null;     
            
        try { 
            // - Connexion à la base
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, login, password);    
            
            FacesContext facesContext = FacesContext.getCurrentInstance();  
            facesContext.responseComplete();  
  
            ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext(); 
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();      
            
            String reportXml = scontext.getRealPath("./resources/reports/ressuECG.jrxml");   
                        
            // - Chargement et compilation du rapport
            JasperDesign jasperDesign = JRXmlLoader.load(reportXml);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            // - Paramètres à envoyer au rapport
            Map parameters = new HashMap();
//            parameters.put("Titre", "Titre");   
            parameters.put("NUMRESSU", numRecu);   
            parameters.put("ANNEEACA", anneeAca);   
//                        
            //creation et affichache du fichier pdf dans une page web
            bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, conn);  
            response.setContentType("application/pdf");                          
                        
            response.setContentLength(bytes.length);    
            ServletOutputStream ouputStream = response.getOutputStream(); 
            ouputStream.write(bytes, 0, bytes.length);  
            ouputStream.flush();     
            ouputStream.close();   
            
        }catch (SQLException | JRException | IOException | ClassNotFoundException ex) {
            System.err.println("Erreur capturé : "+ex);
        }finally{
                try{
                    if(conn!=null){conn.close();}
                }catch (SQLException ex) { System.err.println("Erreur SQL : "+ex);}
        }
    }
}
