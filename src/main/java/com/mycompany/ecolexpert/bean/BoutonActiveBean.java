/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.ecolexpert.bean;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author TOSHIBA
 */
@Named(value = "boutonActiveBean")
@RequestScoped
public class BoutonActiveBean {
    private boolean setBtnModifierDesactive;
    private boolean btnSupprimerDesactive;
    private boolean btnValiderDesactive;
    private boolean champLectureSeul;
    private boolean champDesactive;
    private boolean champCleNonAuto;

    /**
     * Creates a new instance of BoutonActiveBean
     */
    public BoutonActiveBean() {
    }
    
    //getter et setter de l'activation des boutons modifier et valider
    public boolean isBtnModifierDesactive() {
        return setBtnModifierDesactive;
    }

    public void setBtnModifierDesactive(boolean setBtnModifierDesactive) {
        this.setBtnModifierDesactive = setBtnModifierDesactive;
    }

    public boolean isBtnSupprimerDesactive() {
        return btnSupprimerDesactive;
    }

    public void setBtnSupprimerDesactive(boolean btnSupprimerDesactive) {
        this.btnSupprimerDesactive = btnSupprimerDesactive;
    }

    public boolean isBtnValiderDesactive() {
        return btnValiderDesactive;
    }

    public void setBtnValiderDesactive(boolean btnValiderDesactive) {
        this.btnValiderDesactive = btnValiderDesactive;
    }

    public boolean isChampLectureSeul() {
        return champLectureSeul;
    }

    public void setChampLectureSeul(boolean champLectureSeul) {
        this.champLectureSeul = champLectureSeul;
    }

    public boolean isChampDesactive() {
        return champDesactive;
    }

    public void setChampDesactive(boolean champDesactive) {
        this.champDesactive = champDesactive;
    }
    
    public boolean isChampCleNonAuto() {
        return champCleNonAuto;
    }

    public void setChampCleNonAuto(boolean champCleNonAuto) {
        this.champCleNonAuto = champCleNonAuto;
    }        
}
