/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
public class EditAppointmentWindow extends AppointmentWindow {
    
    public EditAppointmentWindow( Connection connection, Person me, Appointment appointment ){
        super( connection, me );
        spinnerStartDate.setValue(appointment.getStart());
        spinnerEndDate.setValue(appointment.getEnd());
        
        textDescription.setText(appointment.getDescription());
        invited = appointment.getInvitedPersons();
        participants = appointment.getParticipants();
        
        // TODO: Add support for changing owner
    }
    
}
