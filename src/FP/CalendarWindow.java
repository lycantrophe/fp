/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import no.ntnu.fp.net.co.Connection;
import java.util.Calendar;
import javax.swing.JButton;

/**
 *
 * @author lycantrophe
 */
public class CalendarWindow extends JFrame {
    
    private Connection connection;
    private Person me;
    private JPanel monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private JButton newEventButton;
    
    public CalendarWindow(Connection connection) throws IOException, ClassNotFoundException {
        
        monday = new JPanel();
        tuesday = new JPanel();
        wednesday = new JPanel();
        thursday = new JPanel();
        friday = new JPanel();
        saturday = new JPanel();
        sunday = new JPanel();


        // Determine what week this is
        //Date date = Calendar.getInstance().getTime();



        /*
         * Build calendar UI
         */
        
        me = (Person) Server.Deserialize(connection.receive());
        for (String appId : me.getAppointmentIds()) {
            /*
             * Map appointments to the proper days
             */
            me.getAppointment(appId);
        }
    }
    
    public void createNewAppointment() {
        Date start = startdate.getValue();
        Date end = enddate.getValue();
        String description = textDescription.getValue();
        
    }
}
