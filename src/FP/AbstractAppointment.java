/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.util.Date;

/**
 *
 * @author lycantrophe
 */
public abstract class AbstractAppointment implements Appointment {
    
    private Date start;
    private Date end;
    private Person owner;
    private String description;
    private AbstractLocation location;
    
    public AbstractAppointment( Person owner, Date start, Date end, String description ){
        this.owner = owner;
        this.start = start;
        this.end = end;
        this.description = description;
    }
}
