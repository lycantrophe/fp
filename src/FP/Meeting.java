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
        super(me, start, end, description, participants );

        // TODO: Handle restrictions
        this.location = location;
        this.invited = invited;
        
    }
}
