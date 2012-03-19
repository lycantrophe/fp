/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
public class AbstractAppointmentWindow extends JFrame {
    
    private Connection connection;
    private JTextField textDescription;
    private JSpinner spinnerStartDate, spinnerEndDate, spinnerStartTime, spinnerEndTime;
    private JButton buttonInvite, buttonAddParticipant, buttonLocation;
    private ArrayList<Person> invited;
    private ArrayList<String> participants;
    private Person me;
    
    public AbstractAppointmentWindow ( Connection connection, Person me ) {
        this.connection = connection;
        this.textDescription = new JTextField();
        this.spinnerStartDate = new JSpinner( new SpinnerDateModel() );
        this.spinnerEndDate = new JSpinner( new SpinnerDateModel() );
        this.buttonInvite = new JButton();
        this.buttonAddParticipant = new JButton();
        this.buttonLocation = new JButton();
        invited = new ArrayList<Person>();
        this.me = me;
        
        
        textDescription.setToolTipText("Event description");
        
        // Create time spinners and editors
        
        buttonInvite.setText("Invite people");
        
        buttonLocation.setText( "Set location" );
        
        
        buttonAddParticipant.setText( "Add external participants" );
    }
    
    public void sendEditAppointment() {
        Date startDate = spinnerStartDate.getValue();
        Date endDate = spinnerEndDate.getValue();
        String description = textDescription.getText();
        
        Appointment appointment = new AppointmentImpl( me, startDate, endDate, description, invited, participants, location );
        String serialized = Server.Serialize(appointment);
        connection.send("create::"+serialized);
    }
    
}
