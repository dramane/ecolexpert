/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.filters;

import java.io.IOException;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
public class RestrictionFilter implements Filter {
    private static final String ACCES_PUBLIC   = "/connexion.jsf";
    private static final String ATT_SESSION_USER = "loginUtilisateur";
 
    @Override
    public void init( FilterConfig config ) throws ServletException {
    }
 
    @Override
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
            ServletException {
        /* Cast des objets request et response */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
 
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();
 
        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
        if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
            System.out.println("La session est : "+session.getAttribute( ATT_SESSION_USER ) );
            /* Redirection vers la page publique */
            response.sendRedirect( request.getContextPath() + ACCES_PUBLIC );
        } else {
            System.out.println("La session est : "+session.getAttribute( ATT_SESSION_USER ) );
            /* Affichage de la page restreinte */
            chain.doFilter( request, response );
        }
    }
 
     @Override
    public void destroy() {
    }
    
    
    
//    public static final String ACCES_CONNEXION  = "/connexion.jsf";
//    public static final String ATT_SESSION_USER = "sessionUtilisateur";
// 
//    @Override
//    public void init( FilterConfig config ) throws ServletException {
//    }
// 
//    @Override
//    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException,
//            ServletException {
//        /* Cast des objets request et response */
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
// 
//        /* Non-filtrage des ressources statiques */
//        String chemin = request.getRequestURI().substring( request.getContextPath().length() );
//        if ( chemin.startsWith( "/resources" ) ) {
//            chain.doFilter( request, response );
//            return;
//        }
// 
//        /* Récupération de la session depuis la requête */
//        HttpSession session = request.getSession();
// 
//        /**
//         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
//         * l'utilisateur n'est pas connecté.
//         */
//        if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
//            /* Redirection vers la page publique */
//            request.getRequestDispatcher( ACCES_CONNEXION ).forward( request, response );
//        } else {
//            /* Affichage de la page restreinte */
//            chain.doFilter( request, response );
//        }
//    }
// 
//    @Override
//    public void destroy() {
//    }
}