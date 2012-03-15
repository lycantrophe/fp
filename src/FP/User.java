/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.IOException;
import java.net.ConnectException;
import java.util.*;
import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
public class User {

    private static Map<String, Person> personMap = Collections.synchronizedMap(new HashMap<String, Person>());
    private Person me;
    private Connection connection;

    /**
     * Constructor
     *
     * @param myPerson Person object to bind User session to
     *
     */
    public User(String myPerson) {
        me = personMap.get(myPerson);
    }

    public void bind(Connection connection) {
        me.bindUser(this);
        this.connection = connection;
    }

    /**
     * Creates and adds an appointment to this User's Person
     *
     * @param start Appointment start time
     *
     * @param end Appointment end time
     *
     * @param description Appointment description
     *
     * @param invited Existing participants. Can be null
     *
     * @param participants Non-existing participants. Can be null
     *
     * @param location Location. Can be null
     */
    public void createAppointment(Date start, Date end, String description, ArrayList<Attending> invited, ArrayList<String> participants, Location location) {

        Appointment appointment;
        // TODO: Add restrictions checking, throw error
        // TODO: Add restrictions to location. Should be in appointment constructor?
        appointment = new AppointmentImpl(me, start, end, description, invited, participants, location);


        Query query = new Query();
        /*
         * TODO: Evaluate possible threat: if updateStatus throws error status
         * will not change and noone will be notified (but entries will be added
         * to the database figure out a way to handle
         */
        query.addAppointment(appointment, appointment.getInvitedPersons());
        query.updateStatus("ATTENDING", appointment, me);
        query.close();

        // Give this appointment to everyone invited
        for (Attending other : invited) {
            other.getPerson().addAppointment(appointment);
            other.getPerson().notify("You are invited to " + appointment.getId() + ". Please respond");
        }
    }

    public String initialSend() {
        String protocol = me.getUsername();
        Set<String> appointmentIds = me.getAppointmentIds();

        // Iterate over appointments
        for (String appointment : appointmentIds) {

            ArrayList<Attending> invited = me.getInvited(appointment);
            // Get all participants
            for (Attending other : invited) {
                // TODO: add protocol parameters for serializing objects
                protocol += other.getPerson().getUsername();
            }
        }
        return protocol;
    }

    public void deleteAppointment(String id) {
        Appointment appointment = me.getAppointment(id);
        ArrayList<Attending> invited = appointment.getInvited();

        Query query = new Query();
        query.deleteAppointment(appointment);
        for (Attending other : invited) {
            other.getPerson().deleteAppointment(id);
            other.getPerson().notify("Appointment" + id + "deleted");
        }
        query.close();
    }

    // TODO: Consider merging decline/accept
    public void declineAppointment(String id) {
        Appointment appointment = me.getAppointment(id);
        ArrayList<Attending> invited = appointment.getInvited();

        Query query = new Query();
        query.updateStatus("DECLINED", appointment, me);

        for (Attending other : invited) {
            other.getPerson().notify(me.getUsername() + " declined " + id);
            // Harms efficiency a bit, but should be usable
            if (other.getPerson().equals(me)) {
                other.setStatus(Attending.Status.DECLINED);
            }
        }
    }

    public void acceptAppointment(String id) {
        Appointment appointment = me.getAppointment(id);
        ArrayList<Attending> invited = appointment.getInvited();

        Query query = new Query();
        query.updateStatus("ATTENDING", appointment, me);

        // Should accepted appointments notify?
        for (Attending other : invited) {
            other.getPerson().notify(me.getUsername() + " declined " + id);
            // Harms efficiency a bit, but should be usable
            if (other.getPerson().equals(me)) {
                other.setStatus(Attending.Status.ATTENDING);
            }
        }
    }

    /**
     * Edits an existing appointment and notifies all participants
     *
     * @param appointment Appointment to change
     * @param newAppointment Appointment object with changes
     */
    public void editAppointment(Appointment appointment, Date start, Date end, Person owner, String description, ArrayList<Person> invited, ArrayList<String> participants, Location location) {

        if (appointment.getStart() != null && !start.equals(appointment.getStart())) {
            appointment.setStart(start);
        }
        if (appointment.getEnd() != null && !end.equals(appointment.getEnd())) {
            appointment.setEnd(end);
        }
        if (appointment.getOwner() != null && !owner.equals(appointment.getOwner())) {
            appointment.setOwner(owner);
        }
        if (appointment.getDescription() != null && !description.equals(appointment.getDescription())) {
            appointment.setDescription(description);
        }
        if (appointment.getLocation() != null && !location.equals(appointment.getLocation())) {
            appointment.setLocation(location);
        }
        if (appointment.getParticipants() != null && !participants.equals(appointment.getParticipants())) {
            appointment.setParticipants(participants);
        }

        ArrayList<Person> oldInvited = invited;
        ArrayList<Person> newInvited = oldInvited;

        if (invited.size() > 1 || appointment.getInvited().size() > 1) {

            oldInvited.removeAll(invited);
            newInvited.removeAll(oldInvited);

            // Remove owner from the list so he does not get notifications
            // TODO: Support for changing owner? If so: handle cases where people have been removed/added and the new owner shall receive notification
            invited.remove(owner);
            oldInvited.remove(owner);
            // Notifies everyone removed from the meeting
            for (Person removed : oldInvited) {
                removed.notify("You have been removed from " + appointment.getId());
            }

            // Notifies everyone else
            for (Person other : invited) {
                other.notify(appointment.getId() + " has changed!");
            }
        }
        Query query = new Query();
        query.updateAppointment(appointment, newInvited, oldInvited);
        query.close();
    }

    public void sendNotification(String notification) throws ConnectException, IOException {
        connection.send(notification);
    }

    public static void addPerson(Person person) {
        personMap.put(person.getUsername(), person);
    }
}
