/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;

import com.mycompany.ecolexpert.ejb.EcoAcademiqueFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoClasseFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoCycleFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoEtudiantFacadeLocal;
import com.mycompany.ecolexpert.ejb.ViewListeEtudiantInscriptionFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoAcademique;
import com.mycompany.ecolexpert.jpa.EcoClasse;
import com.mycompany.ecolexpert.jpa.EcoCycle;
import com.mycompany.ecolexpert.jpa.EcoEtudiant;
import com.mycompany.ecolexpert.jpa.ViewListeEtudiantInscription;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author HP
 */
@Named(value = "etudiantBean")
//@RequestScoped
@ViewScoped
public class EtudiantBean implements Serializable{
    private EcoEtudiant etudiant;
    private List<EcoEtudiant> listEtudiant;
    private ViewListeEtudiantInscription viewListEtudiantInsc;
    private List<ViewListeEtudiantInscription> listViewListEtudiantInsc;
    private List listAnneeAcademiq;
    private List listCodeCycle;
    private List listCodeClasse;
    
    //les parametres de ma recherche d'etudiants
    String anneeAca = "";
    String cycle = "";
    String classe = "";
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB private EcoEtudiantFacadeLocal  etudiantFacadeL;
    @EJB private ViewListeEtudiantInscriptionFacadeLocal  viewListEtudiantInscFacadeL;
    @EJB private EcoAcademiqueFacadeLocal academiqFacadeL;
    @EJB private EcoCycleFacadeLocal cycleFacadeL;
    @EJB private EcoClasseFacadeLocal  classeFacadeL;
    @EJB private EcoAcademiqueFacadeLocal anneeAcademiqFacadeL;

    /**
     * Creates a new instance of EtudiantBean
     */
    public EtudiantBean() {
        etudiant = new EcoEtudiant();
        viewListEtudiantInsc = new ViewListeEtudiantInscription();
    }

    //getter et settter de l'etudiant
    public EcoEtudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(EcoEtudiant etudiant) {
        this.etudiant = etudiant;
    }    
    
    //getter et setter de viewListEtudiantInsc
    public ViewListeEtudiantInscription getViewListEtudiantInsc() {
        return viewListEtudiantInsc;
    }

    public void setViewListEtudiantInsc(ViewListeEtudiantInscription viewListEtudiantInsc) {
        this.viewListEtudiantInsc = viewListEtudiantInsc;
    } 

    //getter et setter de mes parametres de recherche   
    public String getAnneeAca() {    
        return anneeAca;
    }

    public void setAnneeAca(String anneeAca) {
        this.anneeAca = anneeAca;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getClasse() {
        return classe;
    }
    
     public void setClasse(String classe) {    
        this.classe = classe;
    }    

    //retourne la liste de tt les etudiants
    public List<EcoEtudiant> getListEtudiant() {
        try{
            listEtudiant= etudiantFacadeL.findAll();              
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listEtudiant;
    }

    //retourne la liste de tt les etudiants en fonction des paramètres : AnneeAcademique, Cycle, classe.
    public List<ViewListeEtudiantInscription> getListViewListEtudiantInsc() {
        try{ 
            if ("".equals(anneeAca)) anneeAca = anneeAcademiqFacadeL.findAnneeEnCours().getAnneeaca();
            listViewListEtudiantInsc = viewListEtudiantInscFacadeL.findEtudInscripByAnneCycleClasseWithJocker(anneeAca, cycle, classe);                                     
        }catch (Exception ex) {
            System.err.println("Erreur capturée : "+ex);
        }
        return listViewListEtudiantInsc;
    }
    
    //Liste des codes cycle a afficher dans le combobox 
    public List getListCodeCycle(){   
        return listCodeCycle;        
    }
    
    public List getListCodeClasse(){
        return listCodeClasse;
    }
    
    //Action ajax lors de la selection d'une Annee Academique
    public void onAnneeAcaChange(){
        listCodeCycle = new ArrayList();
        if(anneeAca != null && !"".equals(anneeAca)){                       
            for (EcoCycle cycl : cycleFacadeL.findAll()){
                listCodeCycle.add(new SelectItem(cycl.getCodeCycle(), cycl.getDesignation()));
            }
        }else {
            listCodeClasse = new ArrayList();
            cycle = "";
            classe = "";
        }   
    } 
    
    //Action ajax lors de la selection d'un cycle
    public void onCycleChange(){
       listCodeClasse = new ArrayList();
        if(cycle != null && !"".equals(cycle)){            
            List<EcoClasse> data ;
            data = classeFacadeL.findByCodeCycle(cycle);
            for(EcoClasse clas : data){          
              listCodeClasse.add(new SelectItem(clas.getCodeClasse(), clas.getNomclasse()));
            }   
        }else {
            listCodeClasse = new ArrayList();
            classe = "";
        }   
    } 
    
    //Action ajax lors de la selection d'une classe
    public void onClasseChange(){
       
    } 
    
    //Liste des années academique a afficher dans le combobox
    public List getListAnneeAcademiq(){   
        try{  
            listAnneeAcademiq = new ArrayList();
            for(EcoAcademique acad : academiqFacadeL.findAll()){          
              listAnneeAcademiq.add(new SelectItem(acad.getAnneeaca(), acad.getAnneeaca()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listAnneeAcademiq;
    } 
    
    public void imprimer(){
//        try {
//            String numRecu = versement.getMatriEtu().substring(0, 2)+"-"+versement.getIdversement();
//            executeReport(numRecu, versement.getAnneesco());
//        
//        }catch (NullPointerException ex) {
//            System.err.println("Erreur capturé : "+ex);
//        }
    }
    
    //Affichage et impression du réçu
    public void executeReport(String numRecu, String anneeAca){
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
