/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author lycantrophe
 */
public class AppointmentImpl extends AbstractAppointment {
    
    public AppointmentImpl( Person me, Date start, Date end, String description, ArrayList<String> participants, Location location ){
        super( me, start, end, description, participants );
        
        // TODO: Handle restrictions
        this.location = location;
    }
    
    @Override
    public ArrayList<Person> getInvited(){
        // Possible solution if this throws error: return empty list.
        // If so AppointmentImpl & Meeting should be merged
        return null;
    }
}