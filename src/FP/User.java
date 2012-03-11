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
public class User {

    private Person me;

    /**
     * Constructor
     *
     * @param myPerson Person object to bind User session to
     *
     */
    public User(Person myPerson) {
        me = myPerson;
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
     * @param participants Non-existing participants
     */
    /* TODO: Write as one constructor where invited can be passed as null?
     * 
     */
    public void addAppointment(Date start, Date end, String description, ArrayList<String> participants) {
        Appointment appointment = new Appointment( me, start, end, description, participants );
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
     * @param invited List of invited participants (existing objects)
     *
     * @param participants Non-existent participants
     */
    public void addAppointment(Date start, Date end, String description, ArrayList<Person> invited, ArrayList<String> participants) {
        Appointment appointment = new Appointment( me, start, end, description, invited, participants );
    }
}
