/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lycantrophe
 */
public class User {

    private static Map<String, Person> personMap = Collections.synchronizedMap(new HashMap<String, Person>());
    private Person me;

    /**
     * Constructor
     *
     * @param myPerson Person object to bind User session to
     *
     */
    public User(String myPerson) {
        me = personMap.get(myPerson);
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
    public void createAppointment(Date start, Date end, String description, ArrayList<Person> invited, ArrayList<String> participants, Location location) throws SQLException {

        Appointment appointment;
        // TODO: Add restrictions checking, throw error
        // TODO: Add restrictions to location. Should be in appointment constructor?
        appointment = new AppointmentImpl(me, start, end, description, invited, participants, location);

        // Give this appointment to everyone invited
        for (Person other : invited) {
            other.addAppointment(appointment);
        }
    }
    // TODO: Pass "me" in invited list?

    public void deleteAppointment(Appointment appointment) {
        ArrayList<Person> invited = appointment.getInvited();

        Query query;
        try {
            query = new Query();
            query.deleteAppointment(appointment);
            for (Person other : invited) {
                other.deleteAppointment(appointment);
                other.notify( "Appointment" + appointment.getId() + "deleted" );
            }
            query.close();
        } catch (SQLException ex) {
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void declineAppointment(Appointment appointment) {
        ArrayList<Person> invited = appointment.getInvited();
        Query query;
        
        try{
            query = new Query();
            query.updateStatus("DECLINED", appointment, me);
            // Should invited be tuple? enum with status + ID
            appointment.setUserStatus();
            for( Person other: invited ){
                other.notify( me.getUsername() + " declined " + appointment.getId() );
            }
        } catch (SQLException e) {
            
        }
    }

    /**
     * Edits an existing appointment and notifies all participants
     *
     * @param appointment Appointment to change
     * @param newAppointment Appointment object with changes
     */
    public void editAppointment(Appointment appointment, Appointment newAppointment) {
        appointment.updateAppointment(newAppointment);
    }
}
