/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author lycantrophe
 */
public class Query {

    private Connection con;
    PreparedStatement statement;

    public Query() throws SQLException {
        con = DriverManager.getConnection(
                "jdbc:default:connection");
    }

    public void updateAppointment(Appointment appointment, ArrayList<Person> newParticipants, ArrayList<Person> oldParticipants) throws SQLException {

        statement = con.prepareStatement("UPDATE appointment SET start=?, end=?, owner=?, description=?, setLocation=?");
        statement.setDate(1, new java.sql.Date(appointment.getStart().getTime()));
        statement.setDate(2, new java.sql.Date(appointment.getEnd().getTime()));
        statement.setString(3, appointment.getOwner().getUsername());
        statement.setString(4, appointment.getDescription());

        con.prepareStatement("INSERT INTO appointment (username, appointmentid) VALUES ( ?, ? )");

        for (Person other : newParticipants) {
            statement.setString(1, other.getUsername());
            statement.setString(2, appointment.getId());
            statement.executeUpdate();
        }

        statement = con.prepareStatement("DELETE FROM appointment WHERE username=? AND appointmentid=? )");

        for (Person other : oldParticipants) {
            statement.setString(1, other.getUsername());
            statement.setString(2, appointment.getId());
            statement.executeUpdate();

        }
    }

    public void deleteAppointment(Appointment appointment) throws SQLException {
        statement = con.prepareStatement("DELETE FROM appointment WHERE  appointmentid = ?");
        statement.setString(1, appointment.getId());
    }
}