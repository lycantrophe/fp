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
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import no.ntnu.fp.net.co.Connection;

/**
 * @author lycantrophe
 */

public class AppointmentWindow extends JFrame {

    protected Connection connection;
    protected JTextField txtDescription, txtLocation;
    protected JSpinner spinnerStartDate, spinnerEndDate, spinnerStartTime, spinnerEndTime;
    protected JButton buttonInvite, buttonAddParticipant, btnReserveRoom, buttonSave, buttonCancel;
    protected ArrayList<Person> invited;
    protected ArrayList<String> participants;
    protected ArrayList<Location> locations;
    protected Person me;
    protected Location location;
    protected appWinListener al;
    

    public AppointmentWindow(Connection connection, Person me)
    {
        this.connection = connection;
        this.me = me;
        
        this.al = new appWinListener();
        this.participants = new ArrayList<String>();
        this.invited = new ArrayList<Person>();

        try {
            connection.send("getLocations");
            locations = (ArrayList<Location>) Server.Deserialize(connection.receive());
        } catch (ConnectException ex) {
            Logger.getLogger(AppointmentWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppointmentWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppointmentWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
        	locations = new ArrayList<Location>();
        }
        
        InitializeGUI();
    }
    
    public void InitializeGUI()
    {
    	Date date = new Date();
    	
    	txtDescription = new JTextField(16);
    	txtDescription.setToolTipText("Event description");
    	spinnerStartDate = new JSpinner(new SpinnerDateModel());
    	spinnerStartDate.setEditor(new DateEditor(spinnerStartDate, "dd.mm.yy"));
    	spinnerStartTime = new JSpinner(new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY));
    	spinnerStartTime.setEditor(new DateEditor(spinnerStartTime, "kk:mm"));
        spinnerEndDate = new JSpinner(new SpinnerDateModel());
        spinnerEndDate.setEditor(new DateEditor(spinnerEndDate, "dd.mm.yy"));
        spinnerEndTime = new JSpinner(new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY));
        spinnerEndTime.setEditor(new DateEditor(spinnerEndTime, "kk:mm"));
    	txtLocation = new JTextField(16);
        btnReserveRoom = new JButton("Reserver rom");
        btnReserveRoom.addActionListener(al);
        buttonInvite = new JButton("Invite people");
        buttonInvite.addActionListener(al);
        buttonAddParticipant = new JButton("Add external participants");
        buttonAddParticipant.addActionListener(al);
        buttonSave = new JButton("Save");
        buttonCancel = new JButton("Cancel");
        
        JPanel des = new JPanel();
        des.add(txtDescription);
        
        JPanel start = new JPanel();
        start.add(spinnerStartDate);
        start.add(spinnerStartTime);
        
        JPanel end = new JPanel();
        end.add(spinnerEndDate);
        end.add(spinnerEndTime);
        
        JPanel loc = new JPanel();
        loc.add(txtLocation);
        loc.add(btnReserveRoom);
        
        JPanel par = new JPanel();
        par.add(buttonInvite);
        par.add(buttonAddParticipant);
        
        JPanel big = new JPanel();
        big.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        big.add(new JLabel("Beskrivelse:"), c);
        c.gridy = 1;
        big.add(new JLabel("Fra:"), c);
        c.gridy = 2;
        big.add(new JLabel("Til:"), c);
        c.gridy = 3;
        big.add(new JLabel("Sted:"), c);
        c.gridy = 4;
        big.add(new JLabel("Deltakere:"), c);
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

        String description = txtDescription.getText();
        
        ArrayList<Attending> attending = new ArrayList<Attending>();
        for( Person other : invited ){
            attending.add(new Attending(other));
        }
        // TODO: Handle connection and sending exceptions
        Appointment appointment = new AppointmentImpl(me, startDate, endDate, description, attending, participants, location);
        try {
            String serialized = Server.Serialize(appointment);
            connection.send("create::" + serialized);
            appointment = (Appointment) Server.Deserialize(connection.receive());
        } catch (IOException e) {
            // Implement exception handling
        } catch (ClassNotFoundException e) {
            // Implement exception handling
        }
        // Add appointment to calendar
    }

    protected class appWinListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == buttonInvite) {
                InvitePeople inv = new InvitePeople();
                // TODO: Make window visible (and later modal if possible)
            } else if (ae.getSource() == btnReserveRoom) {
                // TODO: Implement Location creation and selection
            } else if (ae.getSource() == buttonAddParticipant) {
                // TODO: Implement participant creation
            }else if (ae.getSource() == buttonSave) {
                sendEditAppointment();
                dispose();
            } else if (ae.getSource() == buttonCancel) {
                dispose();
            }
        }
    }

    protected class InvitePeople extends JFrame {

        private JList invitees;
        private JButton buttonDone;
        private JButton buttonCancel;
        private invAction al;

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
                    // TODO: Implement announcment of selected values in this list
                    // giveListToParent(getInvited);
                }
                dispose();
            }
        }
    }
}
