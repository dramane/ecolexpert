/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Harrison
 */
@ManagedBean
public class AutoCompleteView {

    private String ouiNon;
    private String sexe;

    public List<String> complete(String query) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(query + i);
        }
        return results;
    }

    public List<String> toComplete() {
        List<String> arrayList = new ArrayList<>();

        arrayList.add("Oui");
        arrayList.add("Non");

        Enumeration e = Collections.enumeration(arrayList);

        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }
        return arrayList;
    }

    public List<String> sexComplete1() {
        List<String> leSexe = new ArrayList<>();
        leSexe.add("M");
        leSexe.add("F");
        Enumeration e = Collections.enumeration(leSexe);
        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());

        }
        return leSexe;
    }

    public List<String> sexComplete2() {
        List<String> leSexe = new ArrayList<>();
        leSexe.add("M.");
        leSexe.add("F.");
        Enumeration e = Collections.enumeration(leSexe);
        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());

        }
        return leSexe;
    }

    public List<String> sexComplete3() {
        List<String> leSexe = new ArrayList<>();
        leSexe.add("Mme");
        leSexe.add("Fem");
        Enumeration e = Collections.enumeration(leSexe);
        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }
        return leSexe;
    }

    /**
     * @return the ouiNon
     */
    public String getOuiNon() {
        return ouiNon;
    }

    /**
     * @param ouiNon the ouiNon to set
     */
    public void setOuiNon(String ouiNon) {
        this.ouiNon = ouiNon;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @param sexe the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

}
