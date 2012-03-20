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
public interface Appointment extends Serializable {

    public ArrayList<Attending> getInvited();

    public ArrayList<Person> getInvitedPersons();
    
    public void setId(String id);

    /**
     * @return the start
     */
    public Date getStart();

    /**
     * @return the end
     */
    public Date getEnd();

    /**
     * @return the description
     */
    public String getDescription();

    /**
     * @return the owner
     */
    public Person getOwner();

    /**
     * @return the location
     */
    public Location getLocation();

    /**
     * @return the Id
     */
    public String getId();
    
    /**
     * @return the participants
     */
    public ArrayList<String> getParticipants();

    /**
     * @param start the start to set
     */
    public void setStart(Date start);

    /**
     * @param end the end to set
     */
    public void setEnd(Date end);

    /**
     * @param owner the owner to set
     */
    public void setOwner(Person owner);

    /**
     * @param description the description to set
     */
    public void setDescription(String description);

    /**
     * @param location the location to set
     */
    public void setLocation(Location location);

    /**
     * @param participants the participants to set
     */
    public void setParticipants(ArrayList<String> participants);

    /**
     * @param invited the invited to set
     */
    public void setInvited(ArrayList<Attending> invited);
    
    public void clone(Appointment appointment);
}
