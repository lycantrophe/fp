/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.JButton;
import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
public class EditAppointmentWindow extends AppointmentWindow {

    private JButton buttonDelete;

    public EditAppointmentWindow(Connection connection, Person me, Appointment appointment, Map<String, Person> persons, Map<String, Location> locations, CalendarWindow parent) {
        super(connection, me, persons, locations, parent);
        buttonDelete = new JButton("Delete");
        buttons.add(buttonDelete);
        buttonDelete.addActionListener(new appWinListenerDel(this));
        spinnerStartDate.setValue(appointment.getStart());
        spinnerEndDate.setValue(appointment.getEnd());

        textDescription.setText(appointment.getDescription());
        invited = appointment.getInvitedPersons();
        location = appointment.getLocation();
        isEdit = true;
        thisId = appointment.getId();
    }

    // This might not work, consider simply adding delete as a non-initialized field in parent class
    public class appWinListenerDel extends appWinListener {

        public appWinListenerDel(AppointmentWindow par) {
            super(par);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonDelete) {
                sendDeleteAppointment();
                dispose();
            } else {
                super.actionPerformed(e);
            }
        }
    }
}
