/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lycantrophe
 */
public class AppointmentImpl implements Appointment {

    private Date start;
    private Date end;
    private Person owner;
    private String description;
    private Location location;
    private ArrayList<String> participants;
    private ArrayList<Person> invited;
    private String id;

    public AppointmentImpl(Person owner, Date start, Date end, String description, ArrayList<Person> invited, ArrayList<String> participants, Location location) {
        this.owner = owner;
        this.start = start;
        this.end = end;
        this.id = "newId()";
        this.description = description;
        this.participants = participants;
        this.location = location;
        this.invited = invited;
        invited.add(owner);
    }

    @Override
    public ArrayList<Person> getInvited() {
        return invited;
    }

    @Override
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

        ArrayList<Person> oldInvited = invited;

        if (newAppointment.getInvited() != null) {
            invited = newAppointment.getInvited();
        }

        ArrayList<Person> newInvited = invited;
        oldInvited.removeAll(invited);
        newInvited.removeAll(oldInvited);

        // Remove owner from the list so he does not get notifications
        invited.remove(owner);
        oldInvited.remove(owner);
        // Notifies everyone removed from the meeting
        for (Person removed : oldInvited) {
            removed.notify("You have been removed from " + id);
        }

        // Notifies everyone else
        for (Person other : invited) {
            other.notify(id + " has changed!");
        }
        Query query;
        try {
            query = new Query();
            query.updateAppointment(newAppointment, newInvited, oldInvited);
            query.close();
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentImpl.class.getName()).log(Level.SEVERE, null, ex);
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

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }
}
