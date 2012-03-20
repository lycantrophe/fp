/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import javax.swing.JSpinner.DateEditor;
import javax.swing.*;
import no.ntnu.fp.net.co.Connection;

/**
 * @author lycantrophe
 */
public class AppointmentWindow extends JFrame {

    protected Connection connection;
    protected JTextField textDescription;
    protected JSpinner spinnerStartDate, spinnerEndDate;
    protected JButton buttonInvite, buttonSave, buttonCancel, buttonLocation;
    protected ArrayList<Person> invited;
    protected Map<String, Location> locations;
    protected Map<String, Person> persons;
    protected Person me;
    protected Location location;
    protected appWinListener al;

    public AppointmentWindow(Connection connection, Person me, Map<String, Person> allPersons, Map<String, Location> allLocations) {

        this.connection = connection;
        this.me = me;
        locations = allLocations;
        persons = allPersons;

        this.invited = new ArrayList<Person>();

        InitializeGUI();
    }

    protected void InitializeGUI() {
        Date date = new Date();
        al = new appWinListener(this);

        textDescription = new JTextField(16);
        textDescription.setToolTipText("Event description");

        spinnerStartDate = new JSpinner(new SpinnerDateModel());
        spinnerStartDate.setEditor(new DateEditor(spinnerStartDate));

        spinnerEndDate = new JSpinner(new SpinnerDateModel());
        spinnerEndDate.setEditor(new DateEditor(spinnerEndDate));

        buttonInvite = new JButton("Invite people");
        buttonInvite.addActionListener(al);

        buttonLocation = new JButton("Reserve room");
        buttonLocation.addActionListener(al);

        buttonSave = new JButton("Save");
        buttonCancel = new JButton("Cancel");

        JPanel des = new JPanel();
        des.add(textDescription);

        JPanel start = new JPanel();
        start.add(spinnerStartDate);

        JPanel end = new JPanel();
        end.add(spinnerEndDate);

        JPanel loc = new JPanel();
        loc.add(buttonLocation);

        JPanel par = new JPanel();
        par.add(buttonInvite);

        JPanel big = new JPanel();
        big.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        big.add(new JLabel("Description:"), c);
        c.gridy = 1;
        big.add(new JLabel("From:"), c);
        c.gridy = 2;
        big.add(new JLabel("To:"), c);
        c.gridy = 3;
        big.add(new JLabel("Location:"), c);
        c.gridy = 4;
        big.add(new JLabel("Participants:"), c);

        c.gridx = 1;
        c.gridy = 0;
        big.add(des, c);
        c.gridy = 1;
        big.add(start, c);
        c.gridy = 2;
        big.add(end, c);
        c.gridy = 3;
        big.add(loc, c);
        c.gridy = 4;
        big.add(par, c);

        JPanel buttons = new JPanel();
        buttons.add(buttonSave);
        buttons.add(buttonCancel);

        buttonCancel.addActionListener(al);
        buttonSave.addActionListener(al);

        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        container.add(big, c);

        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridy = 1;
        container.add(buttons, c);

        this.add(container);
    }

    public void sendEditAppointment() {
        // Get values somehow
        Date startDate = (Date) spinnerStartDate.getValue();
        Date endDate = (Date) spinnerEndDate.getValue();
        // TODO: Get values from fields

        String description = textDescription.getText();

        ArrayList<Attending> attending = new ArrayList<Attending>();
        for (Person other : invited) {
            attending.add(new Attending(other));
        }
        // TODO: Handle connection and sending exceptions
        Appointment appointment = new AppointmentImpl(me, startDate, endDate, description, attending, null, location);
        try {
            String serialized = Server.Serialize(appointment);
            System.out.println("Starting create");
            connection.send("create");
            System.out.println("Sending object for creation");
            connection.send(serialized);
            appointment = (Appointment) Server.Deserialize(connection.receive());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Add appointment to calendar
    }

    public void getSelectedValues(Location location ) {
        this.location = location;
    }

    public void getSelectedValues(ArrayList<Person> invited) {
        this.invited = invited;
    }

    protected class appWinListener implements ActionListener {

        private AppointmentWindow par;
        public appWinListener( AppointmentWindow par) {
            this.par = par;
        }
        
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == buttonInvite) {

                ArrayList<Person> arr = new ArrayList<Person>();
                for (String other : persons.keySet()) {
                    arr.add(persons.get(other));
                    System.out.println("Adding " + other + "to invSel");
                }
                SelectList invList = new SelectList(arr, par);
                invList.pack();
                invList.setVisible(true);
                invList.setLocationRelativeTo(null);
            } else if (ae.getSource() == buttonLocation) {

                ArrayList<Location> arr = new ArrayList<Location>();
                for (String other : locations.keySet()) {
                    arr.add(locations.get(other));
                    System.out.println(locations.get(other));
                }
                SelectList invList = new SelectList(arr, par);
                invList.pack();
                invList.setVisible(true);
                invList.setLocationRelativeTo(null);
            } else if (ae.getSource() == buttonSave) {
                sendEditAppointment();
                dispose();
            } else if (ae.getSource() == buttonCancel) {
                dispose();
            }
        }
    }
}