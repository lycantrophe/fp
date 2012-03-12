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
    protected Location location;
    protected ArrayList<String> participants;

    public AbstractAppointment(Person owner, Date start, Date end, String description, ArrayList<String> participants) {
        this.owner = owner;
        this.start = start;
        this.end = end;
        this.description = description;
        this.participants = participants;
    }

    public void updateAppointment(Appointment newAppointment) {
        if (newAppointment.getStart() != null) {
            start = newAppointment.getStart();
        }
        if (newAppointment.getEnd() != null) {
            end = newAppointment.getEnd();
        }
        if (newAppointment.getOwner() != null) {
            owner = newAppointment.getOwner();
        }
        if (newAppointment.getDescription() != null) {
            description = newAppointment.getDescription();
        }
        if (newAppointment.getLocation() != null) {
            location = newAppointment.getLocation();
        }
        if (newAppointment.getParticipants() != null) {
            participants = newAppointment.getParticipants();
        }
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public Person getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }
    
    public Location getLocation() {
        return location;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }
}
