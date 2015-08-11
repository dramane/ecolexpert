/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.bean;


import com.mycompany.ecolexpert.ejb.EcoTypeEtudiantFacadeLocal;
import com.mycompany.ecolexpert.jpa.EcoTypeEtudiant;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author toshiba
 */
@Named(value = "typeEtudiantBean")
@RequestScoped
public class TypeEtudiantBean {
    private EcoTypeEtudiant typeEtudiant;
    private List listTypeEtudiant;   
    
    // Injection de notre EJB (Session Bean Stateless)
    @EJB
    private EcoTypeEtudiantFacadeLocal TypeEtudiantFacadeL;

    /**
     * Creates a new instance of typeEtudiantBean
     */
    public TypeEtudiantBean() {
        typeEtudiant = new EcoTypeEtudiant();
    }
    
    //getter et setter type d'etudiant
    public EcoTypeEtudiant getTypeEtudiant() {
        return typeEtudiant;
    }

    public void setTypeEtudiant(EcoTypeEtudiant typeEtudiant) {
        this.typeEtudiant = typeEtudiant;
    }
    
    
    //Liste des type etudiant a afficher dans le champ select
    public List getListTypeEtudiant(){   
        try{  
            listTypeEtudiant = new ArrayList();
            for(EcoTypeEtudiant typeEtu : TypeEtudiantFacadeL.findAll()){          
              listTypeEtudiant.add(new SelectItem(typeEtu.getTypeEtudiant(), typeEtu.getTypeEtudiant()));
            }
        }catch (Exception ex) {
              System.err.println("Erreur capturée : "+ex);
        }
        return listTypeEtudiant;
    }
}
