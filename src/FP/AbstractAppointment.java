/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author lycantrophe
 */
public abstract class AbstractAppointment implements Appointment {
    
    protected Date start;
    protected Date end;
    protected Person owner;
    protected String description;
    protected AbstractLocation location;
    protected ArrayList<String> participants;
    
    public AbstractAppointment( Person owner, Date start, Date end, String description, ArrayList<String> participants ){
        this.owner = owner;
        this.start = start;
        this.end = end;
        this.description = description;
        this.participants = participants;
    }
}
