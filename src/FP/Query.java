/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lycantrophe
 */
public class Query {

    private Connection con;

    public Query() {
        try {
            String connection = "jdbc:mysql://mysql.stud.ntnu.no/adamczyk_fp";
            String user = "adamczyk_fpuser";
            String password = "fppassword";
            con = DriverManager.getConnection(connection, user, password);
        } catch (SQLException e) {
            System.out.println("Cannot connect to MYSQL database");
            e.printStackTrace();
        }
    }

    public void updateAppointment(Appointment appointment, ArrayList<Person> newParticipants, ArrayList<Person> oldParticipants) {

        PreparedStatement statement;
        try {
            statement = con.prepareStatement("UPDATE Appointment SET StartDate=?, EndDate=?, Owner=?, Description=?, LocationID=? WHERE ID = ?");
            statement.setTimestamp(1, new java.sql.Timestamp(appointment.getStart().getTime()));
            statement.setTimestamp(2, new java.sql.Timestamp(appointment.getEnd().getTime()));
            statement.setString(3, appointment.getOwner().getUsername());
            statement.setString(4, appointment.getDescription());
            if( appointment.getLocation() != null )
            statement.setInt(5, appointment.getLocation().getId());
            else statement.setNull(5, java.sql.Types.NULL);
            statement.setString(6, appointment.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        addAppointment(appointment, newParticipants);

        try {
            statement = con.prepareStatement("DELETE FROM AppointmentRel WHERE Username = ? AND ID = ? )");

            for (Person other : oldParticipants) {
                statement.setString(1, other.getUsername());
                statement.setString(2, appointment.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        updateStatus(Attending.Status.PENDING.toString(), appointment, null);
    }

    public void deleteAppointment(Appointment appointment) {
        PreparedStatement statement;
        try {
            // This should remove all entries in AppointmentRel
            statement = con.prepareStatement("DELETE FROM Appointment WHERE ID = ?");
            statement.setString(1, appointment.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            /*
             * handle exception
             */
        }
        try {
            // This should remove all entries in AppointmentRel
            statement = con.prepareStatement("DELETE FROM AppointmentRel WHERE ID = ?");
            statement.setString(1, appointment.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            /*
             * handle exception
             */
        }
    }

    public void updateStatus(String status, Appointment appointment, Person person) {
        PreparedStatement statement;

        String query;
        if (person != null) {
            query = "UPDATE AppointmentRel SET Status = ? WHERE ID = ? AND Person = ? ";
        } else {
            query = "UPDATE appointmentRel SET Status = ? WHERE ID = ?";
        }

        try {
            statement = con.prepareStatement(query);
            statement.setString(1, status);
            statement.setString(2, appointment.getId());
            if (person != null) {
                statement.setString(3, person.getUsername());
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAppointment(Appointment appointment, ArrayList<Person> persons) {

        PreparedStatement statement;
        try {
            statement = con.prepareStatement("INSERT INTO AppointmentRel ( Person, ID, Status ) VALUES ( ?, ?, ? )");

            for (Person other : persons) {
                statement.setString(1, other.getUsername());
                statement.setString(2, appointment.getId());
                statement.setString(3, Attending.Status.PENDING.toString());
                statement.executeUpdate();
                Server.persons.get(other.getUsername()).notify("You have been invited to an event created by " + appointment.getOwner().getUsername() + "::" + appointment.getId());
                Server.persons.get(other.getUsername()).addAppointment(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authorize(String username, String password) {

        System.out.println("Starting authorize");
        PreparedStatement statement;
        try {
            statement = con.prepareStatement("SELECT COUNT(*) AS Valid FROM Users WHERE Username = ? AND Password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            System.out.println("Executing query");
            ResultSet rs = statement.executeQuery();
            rs.first();
            System.out.println("Got result: " + rs.getInt(1));
            return rs.getInt(1) == 1 ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Person> getPersons() {
        PreparedStatement statement;
        ArrayList<Person> persons = new ArrayList<Person>();
        try {
            statement = con.prepareStatement("SELECT * FROM Person");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                persons.add(new Person(rs.getString("Username"), rs.getString("Firstname"), rs.getString("Surname"), rs.getString("Email"), rs.getString("Phonenumber")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    public void createAllAppointments(Map<String, Person> persons, Map<String, Location> locations) {
        PreparedStatement statement;
        HashMap<String, ArrayList<Attending>> appointments = new HashMap<String, ArrayList<Attending>>();
        try {
            // Get the relations
            statement = con.prepareStatement("SELECT * FROM AppointmentRel");
            ResultSet rs = statement.executeQuery();
            // Builds the map of all the appointment participants
            System.out.println("About to iterate over resultSet");
            while (rs.next()) {
                String key = rs.getString("ID");
                System.out.println("KEY: " + key);
                if (!appointments.containsKey(key)) {
                    appointments.put(key, new ArrayList<Attending>());
                }
                // Adds the newly constructed Attending tuple to the appointments list
                System.out.println("Adding attending object for: " + rs.getString("Person"));
                appointments.get(key).add(new Attending(persons.get(rs.getString("Person")), Attending.Status.valueOf(rs.getString("Status"))));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Starts the work
        try {
            statement = con.prepareStatement("SELECT * FROM Appointment");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                String id = rs.getString("ID");
                Person owner = persons.get(rs.getString("Owner"));

                System.out.println("CREATING APPOINTMENT: " + id);

                // Constructs the appointment
                Appointment appointment = new AppointmentImpl(owner, rs.getTimestamp("StartDate"), rs.getTimestamp("EndDate"), rs.getString("Description"), appointments.get(id), null, locations.get(rs.getString("LocationID")));
                appointment.setId(id);
                // Adds this appointment to everyone invited
                for (Attending other : appointments.get(id)) {
                    Server.persons.get(other.getPerson().getUsername()).addAppointment(appointment);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Appointment createAppointment(Appointment newAppointment) {
        Appointment appointment = new AppointmentImpl();
        appointment.clone(newAppointment);
        ResultSet rs;
        PreparedStatement statement;
        String key;

        try {
            statement = con.prepareStatement("INSERT INTO Appointment ( Owner, StartDate, EndDate, Description, LocationID)"
                    + "VALUES( ?, ?, ?, ?, ? )", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, appointment.getOwner().getUsername());
            statement.setTimestamp(2, new java.sql.Timestamp(appointment.getStart().getTime()));
            statement.setTimestamp(3, new java.sql.Timestamp(appointment.getEnd().getTime()));
            statement.setString(4, appointment.getDescription());
            if (appointment.getLocation() != null) {
                statement.setInt(5, appointment.getLocation().getId());
            } else {
                statement.setNull(5, java.sql.Types.NULL);
            }

            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            rs.first();
            key = rs.getString(1);
            appointment.setId(key);

            addAppointment(appointment, appointment.getInvitedPersons());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    public ArrayList<Location> getLocations() {
        PreparedStatement statement;
        ArrayList<Location> locations = new ArrayList<Location>();
        try {
            statement = con.prepareStatement("SELECT * FROM Location "
                    + "INNER JOIN Room ON Location.RoomID = Room.RoomID "
                    + "INNER JOIN RoomType ON Room.RoomTypeID = RTID");
            ResultSet rs = statement.executeQuery();
            // Send in hash or arraylist to map up persons?
            while (rs.next()) {

                locations.add(new Room(rs.getInt("LocID"), rs.getString("Location.Description"), rs.getInt("Size"), AbstractLocation.Roomtype.valueOf(rs.getString("RoomType.Description"))));
                System.out.println("Adding location: " + rs.getInt("LocID"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return locations;
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}