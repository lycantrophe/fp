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
public class AppointmentWindow extends JFrame implements SelectionInterface {

    protected Connection connection;
    protected CalendarWindow parentWindow;
    protected JTextField textDescription;
    protected JSpinner spinnerStartDate, spinnerEndDate;
    protected JButton buttonInvite, buttonSave, buttonCancel, buttonLocation;
    protected JPanel buttons, container;
    protected ArrayList<Person> invited;
    protected Map<String, Location> locations;
    protected Map<String, Person> persons;
    protected Person me;
    protected Location location;
    protected boolean isEdit;
    protected appWinListener al;
    protected String thisId;

    public AppointmentWindow(Connection connection, Person me, Map<String, Person> allPersons, Map<String, Location> allLocations, CalendarWindow parentWindow) {

        this.connection = connection;
        this.parentWindow = parentWindow;
        this.me = me;
        locations = allLocations;
        persons = allPersons;
        isEdit = false;

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

        buttons = new JPanel();
        buttons.add(buttonSave);
        buttons.add(buttonCancel);

        buttonCancel.addActionListener(al);
        buttonSave.addActionListener(al);
        
        container = new JPanel();
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

        Date startDate = (Date) spinnerStartDate.getValue();
        Date endDate = (Date) spinnerEndDate.getValue();

        String description = textDescription.getText();

        ArrayList<Attending> attending = new ArrayList<Attending>();

        System.out.println("Adding to attending array:");
        for (Person other : invited) {
            attending.add(new Attending(other));
            System.out.println(other.getUsername());
        }
        // TODO: Handle connection and sending exceptions
        Appointment appointment = new AppointmentImpl(me, startDate, endDate, description, attending, null, location);
        try {

            System.out.println("Starting create");
            if (!isEdit) {
                connection.send("create");
            } else {
                connection.send("edit");
                appointment.setId(getThisId());
            }
            String serialized = Server.Serialize(appointment);
            System.out.println("Sending object for creation");
            connection.send(serialized);
            appointment = (Appointment) Server.Deserialize(connection.receive());
            parentWindow.addNewAppointment(appointment);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Add appointment to calendar
    }

    public void sendDeleteAppointment() {
        try {
            connection.send("delete");
            connection.send(getThisId());
            parentWindow.removeAppointment(getThisId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getThisId() {
        return thisId;
    }

    @Override
    public <T> void getSelectedValues(T location) {
        this.location = (Location) location;
    }

    @Override
    public <T> void getSelectedValues(ArrayList<T> invited) {
        this.invited = (ArrayList<Person>) invited;
        System.out.println("Invited set to:");
        for (Person other : this.invited) {
            System.out.println(other.getUsername());
        }
        System.out.println("That's all, folks!");
    }

    protected class appWinListener implements ActionListener {

        protected AppointmentWindow par;

        public appWinListener(AppointmentWindow par) {
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