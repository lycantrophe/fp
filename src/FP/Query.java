/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    public void updateAppointment( Appointment appointment ) throws SQLException {
        
        statement = con.prepareStatement("UPDATE appointment SET start=?, end=?, owner=?, description=?, setLocation=?");
        statement.setDate(1, new java.sql.Date(appointment.getStart().getTime()));
        statement.setDate(2, new java.sql.Date(appointment.getEnd().getTime()));
        statement.setString(3, appointment.getOwner().getUsername());
        statement.setString(4, appointment.getDescription());
    }
}
