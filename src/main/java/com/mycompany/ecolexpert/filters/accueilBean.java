/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.filters;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author toshiba
 */
@Named(value = "accueilBean")
@RequestScoped
public class accueilBean {
    private static final String CHAMP_NOM  = "loginUtilisateur";
    private static final String CHAMP_PASS   = "motDePasse";  
    private static final String CHAMP_NOM_PRENOM  = "nomEtPrenom";
    private static final String CHAMP_EMAIL   = "eMail";  
    private static final String CHAMP_PROFIL = "profil";
    private String user ;

    /**
     * Creates a new instance of accueilBean
     */
    public accueilBean() {
    }

    public String getUser() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        HttpSession session = (HttpSession) context.getSession(false);
        user = (String) session.getAttribute(CHAMP_NOM_PRENOM);
                
        System.out.println("RequestPathInfo : "+context.getRequestPathInfo());
        System.out.println("RequestServletPath() : "+context.getRequestServletPath().toString());
        System.out.println("RequestContextPath : "+context.getRequestContextPath());
        System.out.println("RequestURI : "+request.getRequestURI());
        System.out.println("RequestServerName : "+context.getRequestServerName());
        System.out.println(" la session de l'utilisateur est : "+user);
        System.out.println("-------------------------------------------------");
        
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }   
}
