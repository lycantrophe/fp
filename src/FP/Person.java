/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lycantrophe
 */
public class Person implements Serializable {

    private String username;
    private String firstname;
    private String surname;
    private String email;
    private String phoneNumber;
    private ArrayList<String> notifications;
    private Map<String, Appointment> appointments;
//    private User user;

    public Person(String username, String firstname, String surname, String email, String phoneNumber) {

        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;

        notifications = new ArrayList<String>();
        appointments = new HashMap<String, Appointment>();
    }

    public void bindUser(User user) {
        //      this.user = user;
    }

    /**
     * Adds an appointment to this objects list and adds a notification to the
     * notification queue
     *
     * @param appointment Appointment to add
     */
    public void addAppointment(Appointment appointment) {
        System.out.println("Appointment being added! " + appointment.getId());
        appointments.put(appointment.getId(), appointment);
    }

    /**
     * Removes the given appointment from this objects appointments list
     *
     * @param appointmentId Appointment to remove
     */
    public void deleteAppointment(String id) {
        appointments.remove(id);
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getNotifications() {
        // TODO: Handle ack before notifications are removed. 
        // getNextNotification()? If so, use queue instead of arraylist.
        return notifications;
    }
    
    public void removeAppointment( String id ) {
        appointments.remove(id);
    }
    public void notify(String notification) {
        notifications.add(notification);
        // TODO: Rewrite notification support
    /*
         * if (user != null) { try { user.sendNotification(notification); }
         * catch (ConnectException ex) {
         * Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
         * } catch (IOException ex) {
         * Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
         * } }
         */
    }

    public Appointment getAppointment(String id) {
        return appointments.get(id);
    }

    public Set<String> getAppointmentIds() {
        return appointments.keySet();
    }

    public ArrayList<Attending> getInvited(String id) {
        return appointments.get(id).getInvited();
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }
        
    public void removeNotification( String message ) {
        Server.persons.get(username).notifications.remove(message);
    }
}
