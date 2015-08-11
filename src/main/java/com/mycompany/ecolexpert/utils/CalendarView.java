/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ecolexpert.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Harrison
 */
@Named(value = "calendarView")
public class CalendarView {

    private Date date9;

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

public void click(){
    RequestContext requestContext = RequestContext.getCurrentInstance();
    requestContext.update("form:display");
    requestContext.execute("PF('dlg').show()");
}
    
    public Date getDate9() {
        return date9;
    }

    public void setDate9(Date date9) {
        this.date9 = date9;
    }

}
