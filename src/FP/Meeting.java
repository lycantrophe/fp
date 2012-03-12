/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author lycantrophe
 */
public class Meeting extends AbstractAppointment {

    private ArrayList<Person> invited;

    public Meeting(Person me, Date start, Date end, String description, ArrayList<Person> invited, ArrayList<String> participants, Location location) {
        super(me, start, end, description, participants);

        // TODO: Handle restrictions
        this.location = location;
        this.invited = invited;

    }

    @Override
    public ArrayList<Person> getInvited() {
        return invited;
    }

    @Override
    public void updateAppointment(Appointment newAppointment) {

        super.updateAppointment(newAppointment);
        ArrayList<Person> oldInvited = invited;

        if (newAppointment.getInvited() != null) {
            invited = newAppointment.getInvited();
        }

        ArrayList<Person> newInvited = invited;
        oldInvited.removeAll(invited);
        newInvited.removeAll(oldInvited);

        // Notifies everyone removed from the meeting
        for (Person removed : oldInvited) {
            removed.notify("REMOVED!");
        }

        // Notifies everyone else
        for (Person other : invited) {
            other.notify("Changed!");
        }

        Connection con;
        PreparedStatement sql = null;

        try {
            con = DriverManager.getConnection(
                    "jdbc:default:connection");

            sql = con.prepareStatement("INSERT INTO appointment (username, appointmentid) VALUES ( ?, ? )");

            for (Person other : newInvited) {
                sql.setString(1, other.getUsername());
                sql.setString(2, appointment.getId());
                sql.executeUpdate();
            }
            // TODO: Consider splitting in two separate try blocks
            sql = con.prepareStatement("DELETE FROM appointment WHERE username=? AND appointmentid=? )");

            for (Person other : newInvited) {
                sql.setString(1, other.getUsername());
                sql.setString(2, appointment.getId());
                sql.executeUpdate();
            }

        } finally {
            if (sql != null) {
                sql.close();
            }
        }

    }
}
