/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import no.ntnu.fp.net.co.Connection;

public class CalendarWindow extends JFrame {

    private Connection connection;
    private Person me;
    private JPanel leftArrowPanel, rightArrowPanel, topPanel, bottomPanel,
            monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private JButton newEventButton, addCalendarButton, removeCalendarButton, leftArrowButton, rightArrowButton;
    private Font font, daysFont;
    private JLabel labelWeek;
    private ImageIcon leftArrowIcon, rightArrowIcon;
    private Image imgLeft, imgRight;
    private Map<String, Calendar> thisWeek;
    private String[] dayNames = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    private JPanel[] dayColumns = {monday, tuesday, wednesday, thursday, friday, saturday, sunday};
    private GridBagConstraints gridConst;
    private Calendar today;
    private ArrowButtonListener arrowListener;

    public CalendarWindow(Connection connection) throws IOException, ClassNotFoundException {

        me = (Person) Server.Deserialize(connection.receive());
        today = Calendar.getInstance();

        setLayout(new GridBagLayout());
        gridConst = new GridBagConstraints();

        thisWeek = new HashMap<String, Calendar>();

        daysFont = new Font("", Font.PLAIN, 16);

        drawWindow();
        mapAppointments();
        //c.add(Calendar.DATE, -(c.get(Calendar.DAY_OF_WEEK) + i));

    }

    private void mapAppointments() {
        labelWeek.setText("Week " + today.get(Calendar.WEEK_OF_YEAR));
        
        /*
         * Remove entries belonging to wrong week
         */
        
        Appointment appointment;
        for (String appId : me.getAppointmentIds()) {
            /*
             * Map appointments to the proper days
             */
            appointment = me.getAppointment(appId);
            Calendar firstday = today;
            firstday.add( Calendar.DATE, -(today.get(Calendar.DAY_OF_WEEK)+1 ) );
            Calendar lastday = firstday;
            lastday.add( Calendar.DATE, 6 );
            if( isWithinRange( appointment.getStart(), firstday.getTime(), lastday.getTime() ) ){
                firstday.setTime( appointment.getStart() );
                // TODO: New JLabel() should be approperiate element
                dayColumns[firstday.get(Calendar.DAY_OF_WEEK) - 1].add(new JLabel());
            }
        }
    }

    private void drawWindow() {

        gridConst.gridx = 1;
        gridConst.gridy = 0;
        gridConst.fill = GridBagConstraints.HORIZONTAL;
        gridConst.ipady = 40;
        topPanel = new JPanel();
        add(topPanel, gridConst);
        font = new Font("", Font.BOLD, 20);
        labelWeek = new JLabel();
        labelWeek.setFont(font);
        topPanel.add(labelWeek);

        /*
         * Arrow panels
         */
        arrowListener = new ArrowButtonListener();
        gridConst.gridx = 0;
        gridConst.gridy = 1;
        gridConst.ipady = 300;
        gridConst.ipadx = 10;
        leftArrowPanel = new JPanel(new GridBagLayout());
        add(leftArrowPanel, gridConst);
        //intern gridBagLayout & constraints
        GridBagConstraints gridArrowL = new GridBagConstraints();
        gridArrowL.anchor = GridBagConstraints.CENTER;
        //add img as buttton
        imgLeft = getToolkit().createImage(getClass().getResource("leftArrow.png"));
        leftArrowIcon = new ImageIcon(imgLeft);
        leftArrowButton = new JButton();
        leftArrowButton.setIcon(leftArrowIcon);
        leftArrowButton.addActionListener(arrowListener);
        leftArrowPanel.add(leftArrowButton, gridArrowL);

        /*
         * rightArrowPanel
         */
        gridConst.gridx = 8;
        gridConst.gridy = 1;
        gridConst.ipady = 300;
        gridConst.ipadx = 10;
        rightArrowPanel = new JPanel(new GridBagLayout());
        add(rightArrowPanel, gridConst);
        //intern gridBagLayout & constraints
        GridBagConstraints gridArrowR = new GridBagConstraints();
        gridArrowR.anchor = GridBagConstraints.CENTER;
        //add img as buttton
        imgRight = getToolkit().createImage(getClass().getResource("rightArrow.png"));
        rightArrowIcon = new ImageIcon(imgRight);
        rightArrowButton = new JButton();
        rightArrowButton.setIcon(rightArrowIcon);
        rightArrowButton.addActionListener(arrowListener);
        rightArrowPanel.add(rightArrowButton, gridArrowR);

        /*
         * bottomPanel
         */

        gridConst.gridwidth = 4;
        gridConst.gridx = 4;
        gridConst.gridy = 2;
        gridConst.fill = GridBagConstraints.HORIZONTAL;
        gridConst.ipady = 40;
        bottomPanel = new JPanel(new GridBagLayout());
        add(bottomPanel, gridConst);
        //bridBagLayout & constraints
        GridBagConstraints gridBottom = new GridBagConstraints();
        gridBottom.gridx = 0;
        gridBottom.weightx = 10;
        newEventButton = new JButton("New Appointment");
        newEventButton.addActionListener(new NewEventButtonAction());
        bottomPanel.add(newEventButton, gridBottom);

        gridBottom.gridx = 1;
        gridBottom.weightx = 10;
        addCalendarButton = new JButton("Add Calendar");
        addCalendarButton.addActionListener(new AddCalendarButtomAction());
        bottomPanel.add(addCalendarButton, gridBottom);

        gridBottom.gridx = 2;
        gridBottom.weightx = 10;
        removeCalendarButton = new JButton("Remove Calendar");
        removeCalendarButton.addActionListener(new RemoveCalendarButtomAction());
        bottomPanel.add(removeCalendarButton, gridBottom);
        // Determine what week this is

        for (int i = 0; i < 7; i++) {
            drawDayColumn(i);
        }
    }

    private void drawDayColumn(int i) {
        gridConst.gridx = i + 1;
        gridConst.gridy = 1;
        // gridConst.ipady = 400;
        //gridConst.ipadx = 10;
        dayColumns[i] = new JPanel();
        add(dayColumns[i], gridConst);
        //add label
        JLabel dayLabel = new JLabel(dayNames[i]);

        dayLabel.setFont(daysFont);

        // Consider not defining dayColumns by name
        dayColumns[i].add(dayLabel);
        dayColumns[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }

    /*
     * Arrow action classes
     */
    private class ArrowButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == leftArrowButton) {
                today.add(Calendar.DATE, -7);
            } else {
                today.add(Calendar.DATE, 7);
            }
            mapAppointments();
        }
    }
    
    /*
     * bottomPanel buttons action classes
     */

    private class NewEventButtonAction implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            // TODO: 
        }
    }

    private class AddCalendarButtomAction implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            // TODO: 
        }
    }

    private class RemoveCalendarButtomAction implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            // TODO: 
        }
    }

    boolean isWithinRange(Date date, Date start, Date end) {
        return !(date.before(start) || date.after(end));
    }
}
