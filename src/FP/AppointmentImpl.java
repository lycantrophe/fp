/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author lycantrophe
 */
public class AppointmentImpl implements Appointment, Serializable {

    private Date start;
    private Date end;
    private Person owner;
    private String description;
    private Location location;
    private ArrayList<String> participants;
    private ArrayList< Attending > invited;
    private String id;

    public AppointmentImpl(Person owner, Date start, Date end, String description, ArrayList<Attending> invited, ArrayList<String> participants, Location location) {
        this.owner = owner;
        this.start = start;
        this.end = end;
        this.id = "newId()";
        this.description = description;
        this.participants = participants;
        this.location = location;
        this.invited = invited;
        invited.add(new Attending(owner, Attending.Status.ATTENDING));
    }

    public ArrayList<Attending> getInvited() {
        return invited;
    }
    
    public ArrayList<Person> getInvitedPersons() {
        ArrayList persons = new ArrayList<Person>();
        for( Attending other : invited ){
            persons.add(other.getPerson());
        }
        return persons;
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

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public void setInvited(ArrayList<Attending> invited) {
        this.invited = invited;
    }
}