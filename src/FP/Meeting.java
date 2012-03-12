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
public class Meeting extends AbstractAppointment {

    private ArrayList<Person> invited;

    public Meeting(Person me, Date start, Date end, String description, ArrayList<Person> invited, ArrayList<String> participants, Location location) {
        super(me, start, end, description, participants);

        // TODO: Handle restrictions
        this.location = location;
        this.invited = invited;

    }

    @Override
    public ArrayList<Person> getInvited() {
        return invited;
    }

    @Override
    public void updateAppointment(Appointment newAppointment) {
        super.updateAppointment(newAppointment);
        ArrayList<Person> oldInvited = invited;
        if (newAppointment.getInvited() != null) {
            invited = newAppointment.getInvited();
        }
        oldInvited.removeAll(invited);
        // Notifies everyone removed from the meeting
        for( Person removed : oldInvited ){
            removed.notify( "REMOVED!" );
        }
        
        // Notifies everyone else
        for( Person.other : invited ){
            other.notify( "Changed!" );
        }
    }
}
