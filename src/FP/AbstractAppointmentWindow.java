/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

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
    private JButton buttonInvite, buttonAddParticipant;
    
    public AbstractAppointmentWindow ( Connection connection ) {
        this.connection = connection;
        this.textDescription = new JTextField();
        this.spinnerStartDate = new JSpinner( new SpinnerDateModel() );
        this.spinnerEndDate = new JSpinner( new SpinnerDateModel() );
        this.buttonInvite = new JButton();
        this.buttonAddParticipant = new JButton();
        
        
        textDescription.setToolTipText("Event description");
        
        // Create time spinners and editors
        
        buttonInvite.setText("Invite people");
        
        
        buttonAddParticipant.setText( "Add external participants" );
        
        
    }
    
}
