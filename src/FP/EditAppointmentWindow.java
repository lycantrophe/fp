/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.util.Map;
import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
public class EditAppointmentWindow extends AppointmentWindow {
    
    public EditAppointmentWindow( Connection connection, Person me, Appointment appointment, Map<String, Person> persons, Map<String, Location> locations, CalendarWindow parent ){
        super( connection, me, persons, locations, parent );
        spinnerStartDate.setValue(appointment.getStart());
        spinnerEndDate.setValue(appointment.getEnd());
        
        textDescription.setText(appointment.getDescription());
        invited = appointment.getInvitedPersons();
        //participants = appointment.getParticipants();
        
        // TODO: Add support for changing owner
    }
    
}
