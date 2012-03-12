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
public interface Appointment {

    public ArrayList<Person> getInvited();

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
     * @return the participants
     */
    public ArrayList<String> getParticipants();

    /**
     * Updates the appointment according to differences (and not-null fields) in
     * newAppointment
     *
     * @param newAppointment Appointment object holding fields to modify
     */
    public void updateAppointment(Appointment newAppointment);
}
