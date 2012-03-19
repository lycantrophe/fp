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
    protected JButton buttonInvite, buttonAddParticipant, buttonNewLocation;
    protected JComboBox comboLocations;
    protected ArrayList<Person> invited;
    protected ArrayList<String> participants;
    protected Person me;
    protected Location location;
    protected appWinListener al;
    
    public AppointmentWindow(Connection connection, Person me) {

        this.connection = connection;
        Date date = new Date();
        al = new appWinListener();
        
        this.textDescription = new JTextField();

        // Date pickers
        this.spinnerStartDate = new JSpinner(new SpinnerDateModel());
        this.spinnerEndDate = new JSpinner(new SpinnerDateModel());
        this.spinnerStartTime = new JSpinner(new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY));
        this.spinnerEndTime = new JSpinner(new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY));


        this.buttonInvite = new JButton();
        this.buttonAddParticipant = new JButton();
        this.buttonNewLocation = new JButton();
        this.comboLocations = new JComboBox((Location[]) Arrays.asList(locations));
        invited = new ArrayList<Person>();
        this.me = me;

        textDescription.setToolTipText("Event description");

        // Create time spinners and editors
        JSpinner.DateEditor timeSpinnerStart = new JSpinner.DateEditor(spinnerStartTime, "hh:mm");
        JSpinner.DateEditor timeSpinnerEnd = new JSpinner.DateEditor(spinnerStartTime, "hh:mm");

        spinnerStartTime.setEditor(timeSpinnerStart);
        spinnerEndTime.setEditor(timeSpinnerEnd);

        buttonInvite.setText("Invite people");
        buttonInvite.addActionListener(al);

        buttonNewLocation.setText("Add location");
        buttonNewLocation.addActionListener(al);
        comboLocations.addActionListener(al);

        buttonAddParticipant.setText("Add external participants");
        buttonAddParticipant.addActionListener(al);
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

    protected class appWinListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == buttonInvite) {
                InvitePeople inv = new InvitePeople();
            } else if (ae.getSource() == buttonNewLocation) {
                /*
                 * fix
                 */
            } else if (ae.getSource() == buttonAddParticipant) {
            } else if (ae.getSource() == comboLocations) {
                location = (Location) comboLocations.getSelectedItem();
            }
        }
    }

    protected class InvitePeople extends JFrame {

        private JList invitees;
        private JButton buttonDone;
        private JButton buttonCancel;
        invAction al;

        public InvitePeople() {
            // availiblePersons is the arraylist of registered persons ;

            ArrayList<Person> testList = new ArrayList<Person>();
            invitees = new JList(testList.toArray());
            JScrollPane scrollPane = new JScrollPane(invitees);
            add(scrollPane);

            al = new invAction();
            buttonDone = new JButton();
            buttonCancel = new JButton();

            buttonDone.addActionListener(al);
            buttonCancel.addActionListener(al);

            add(buttonDone);
            add(buttonCancel);
        }

        public ArrayList<Person> getInvited() {
            ArrayList<Person> persons = new ArrayList<Person>();
            persons.addAll(Arrays.asList((Person[]) invitees.getSelectedValues()));
            return persons;
        }

        private class invAction implements ActionListener {

            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == buttonDone) {
                    // giveListToParent(getInvited);
                }
                dispose();
            }
        }
    }
}
