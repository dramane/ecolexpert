/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.servlet;

import com.mycompany.ecolexpert.ejb.EcoEtudiantFacadeLocal;
import com.mycompany.ecolexpert.ejb.EcoInscriptionFacade;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HP
 */
@WebServlet(name = "AffichePhotoServlet", urlPatterns = {"/AffichePhotoServlet"})
public class AffichePhotoServlet extends HttpServlet {
    @EJB private EcoEtudiantFacadeLocal etudiantFacadeL;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (PrintWriter out = response.getWriter()) {    

            Object id = request.getParameter("numetu");
            
            System.out.println("Le numero de l'etudiant est : "+id);
            
            if (id != "") {
                byte[] bytearray = new byte[1048576];
                int size = 0;

                ByteArrayInputStream bisImage = new ByteArrayInputStream(etudiantFacadeL.find(id).getPhoto());    

                response.reset();
                response.setContentType("image/jpeg");
                if ((size = bisImage.read(bytearray)) != -1) {
                    response.getOutputStream().
                    write(bytearray, 0, size);
                }
            }            
        }catch (Exception e) {
            System.err.println("ERREUR CAPTUREE : "+e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}