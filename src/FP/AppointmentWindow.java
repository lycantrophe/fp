/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
public class AppointmentWindow extends JFrame implements ActionListener {

    protected Connection connection;
    protected JTextField textDescription;
    protected JSpinner spinnerStartDate, spinnerEndDate, spinnerStartTime, spinnerEndTime;
    protected JButton buttonInvite, buttonAddParticipant, buttonLocation;
    protected ArrayList<Person> invited;
    protected ArrayList<String> participants;
    protected Person me;

    public AppointmentWindow(Connection connection, Person me) {

        this.connection = connection;
        Date date = new Date();

        this.textDescription = new JTextField();

        // Date pickers
        this.spinnerStartDate = new JSpinner(new SpinnerDateModel());
        this.spinnerEndDate = new JSpinner(new SpinnerDateModel());
        this.spinnerStartTime = new JSpinner(new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY));
        this.spinnerEndTime = new JSpinner(new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY));


        this.buttonInvite = new JButton();
        this.buttonAddParticipant = new JButton();
        this.buttonLocation = new JButton();
        invited = new ArrayList<Person>();
        this.me = me;

        textDescription.setToolTipText("Event description");

        // Create time spinners and editors
        JSpinner.DateEditor timeSpinnerStart = new JSpinner.DateEditor(spinnerStartTime, "hh:mm");
        JSpinner.DateEditor timeSpinnerEnd = new JSpinner.DateEditor(spinnerStartTime, "hh:mm");

        spinnerStartTime.setEditor(timeSpinnerStart);
        spinnerEndTime.setEditor(timeSpinnerEnd);

        buttonInvite.setText("Invite people");

        buttonLocation.setText("Set location");


        buttonAddParticipant.setText("Add external participants");
    }

    public void sendEditAppointment() {
        // Get values somehow
        Date startDate = spinnerStartDate.getValue();
        Date endDate = spinnerEndDate.getValue();
        String description = textDescription.getText();

        Appointment appointment = new AppointmentImpl(me, startDate, endDate, description, invited, participants, location);
        String serialized = Server.Serialize(appointment);
        connection.send("create::" + serialized);
    }

    public void actionPerformed(ActionEvent e) {
    }

    private class invitePeople extends JFrame {

        JList invitees;

        public invitePeople() {
            // availiblePersons is the arraylist of registered persons ;

            ArrayList<Person> testList = new ArrayList<Person>();
            invitees = new JList(testList.toArray());
            JScrollPane scrollPane = new JScrollPane(invitees);
            add(scrollPane);
        }

        public ArrayList<Person> getInvited() {
            ArrayList<Person> persons = new ArrayList<Person>();
            persons.addAll(Arrays.asList((Person[]) invitees.getSelectedValues()));
            return persons;
        }
    }
}
