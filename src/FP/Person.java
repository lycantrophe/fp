/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lycantrophe
 */
public class Person {

    private String username;
    private String firstname;
    private String surname;
    private String email;
    private String phoneNumber;
    private ArrayList<String> notifications;
    private static Map<String, Appointment> appointments;

    public Person(String username, String firstname, String surname, String email, String phoneNumber) {

        this.username = username;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;

        notifications = new ArrayList<String>();
        appointments = new HashMap<String, Appointment>();
    }

    /**
     * Adds an appointment to this objects list and adds a notification to the
     * notification queue
     *
     * @param appointment Appointment to add
     */
    public void addAppointment(Appointment appointment) {
        appointments.put(appointment.getId(), appointment);
        notify("Appointment added");
        notifications.add("Appointment added");
    }

    /**
     * Removes the given appointment from this objects appointments list
     *
     * @param appointmentId Appointment to remove
     */
    public void deleteAppointment(Appointment appointment) {
        appointments.remove(appointment.getId());
    }

    public void declined(Appointment appointment, Person person) {
        Query query;
        try {
            query = new Query();
            query.updateStatus("DECLINED", appointment, person);
            query.close();
        } catch (SQLException ex) {
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getNotifications() {
        // TODO: Handle ack before notifications are removed. 
        // getNextNotification()? If so, use queue instead of arraylist.
        return notifications;
    }

    public void notify(String notification) {
        notifications.add(notification);
        fireNotificationsUpdated();
    }
}
