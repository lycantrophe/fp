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
    private ButtonListener buttonListener;
    private Map<String, Person> allPersons;
    private Map<String, Location> allLocations;

    public CalendarWindow(Connection connection) throws IOException, ClassNotFoundException {

        me = (Person) Server.Deserialize(connection.receive());
        allPersons = (HashMap<String, Person>) Server.Deserialize(connection.receive());
        allLocations = (HashMap<String, Location>) Server.Deserialize(connection.receive());
        today = Calendar.getInstance();
        this.connection = connection;

        setLayout(new GridBagLayout());
        gridConst = new GridBagConstraints();

        thisWeek = new HashMap<String, Calendar>();

        arrowListener = new ArrowButtonListener();
        buttonListener = new ButtonListener();

        daysFont = new Font("", Font.PLAIN, 16);

        drawWindow();
        mapAppointments();
        //c.add(Calendar.DATE, -(c.get(Calendar.DAY_OF_WEEK) + i));

    }

    private void mapAppointments() {
        labelWeek.setText("Week " + today.get(Calendar.WEEK_OF_YEAR) + " of " + today.get(Calendar.YEAR));

        for (JPanel panel : dayColumns) {
            panel.removeAll();
        }

        Calendar firstday = Calendar.getInstance();
        firstday.setTime(today.getTime());
        firstday.add(Calendar.DATE, -(today.get(Calendar.DAY_OF_WEEK) + 1));
        Calendar lastday = Calendar.getInstance();
        lastday.setTime(firstday.getTime());
        lastday.add(Calendar.DATE, 6);

        if (me == null) {
            System.out.println("I DON'T EXIST");
        } else {
            System.out.println("I do exist! Yes I can " + me.getUsername());
        }
        Appointment appointment;
        // TODO: Implement getAppointments properly
        for (String appId : me.getAppointmentIds()) {
            System.out.println("Appointment ID: " + appId);
            appointment = me.getAppointment(appId);

            System.out.println("Start: " + appointment.getStart().toString());
            System.out.println("First: " + firstday.getTime().toString());
            System.out.println("Last: " + lastday.getTime().toString());

            if (isWithinRange(appointment.getStart(), firstday.getTime(), lastday.getTime())) {
                firstday.setTime(appointment.getStart());
                JLabel toBeAdded = new JLabel();
                toBeAdded.setText(appointment.getStart().toString() + " - "
                        + appointment.getDescription());
                dayColumns[firstday.get(Calendar.DAY_OF_WEEK) - 1].add(toBeAdded);
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
        //imgLeft = getToolkit().createImage(getClass().getResource("leftArrow.png"));
        //imgLeft = getToolkit().createImage("/home/lycantrophe/NetbeansProjects/fp/src/FP/leftArrow.png");
        //leftArrowIcon = new ImageIcon(imgLeft);
        leftArrowIcon = new ImageIcon("/home/lycantrophe/NetBeansProjects/fp/src/FP/leftArrow.png");
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
        //imgRight = getToolkit().createImage(getClass().getResource("./rightArrow.png"));
        //imgRight = getToolkit().createImage("/home/lycantrophe/NetbeansProjects/fp/src/FP/rightArrow.png");
        //rightArrowIcon = new ImageIcon(imgRight);
        rightArrowIcon = new ImageIcon("/home/lycantrophe/NetBeansProjects/fp/src/FP/rightArrow.png");
        rightArrowButton = new JButton();
        rightArrowButton.setIcon(rightArrowIcon);
        rightArrowButton.addActionListener(arrowListener);
        rightArrowPanel.add(rightArrowButton, gridArrowR);

        /*
         * bottomPanel
         */

        gridConst.weightx = 4;
        gridConst.gridx = 4;
        gridConst.gridy = 2;
        //    gridConst.fill = GridBagConstraints.HORIZONTAL;
        gridConst.ipady = 40;
        bottomPanel = new JPanel(new GridBagLayout());
        add(bottomPanel, gridConst);
        //bridBagLayout & constraints
        GridBagConstraints gridBottom = new GridBagConstraints();
        gridBottom.gridx = 0;
        gridBottom.weightx = 10;
        newEventButton = new JButton("New Appointment");
        newEventButton.addActionListener(buttonListener);
        bottomPanel.add(newEventButton, gridBottom);

        gridBottom.gridx = 1;
        gridBottom.weightx = 10;
        addCalendarButton = new JButton("Add Calendar");
        addCalendarButton.addActionListener(buttonListener);
        bottomPanel.add(addCalendarButton, gridBottom);

        gridBottom.gridx = 2;
        gridBottom.weightx = 10;
        removeCalendarButton = new JButton("Remove Calendar");
        removeCalendarButton.addActionListener(buttonListener);
        bottomPanel.add(removeCalendarButton, gridBottom);
        // Determine what week this is

        for (int i = 0; i < 7; i++) {
            drawDayColumn(i);
        }
    }

    private void drawDayColumn(int i) {
        gridConst.gridx = i + 1;
        gridConst.gridy = 1;
        gridConst.ipady = 400;
        gridConst.ipadx = 100;
        dayColumns[i] = new JPanel();
        add(dayColumns[i], gridConst);
        //add label
        JLabel dayLabel = new JLabel(dayNames[i]);

        dayLabel.setFont(daysFont);

        System.out.println("Setting label: " + dayNames[i]);
        // Consider not defining dayColumns by name
        dayColumns[i].add(dayLabel);
        dayColumns[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }

    private boolean isWithinRange(Date date, Date start, Date end) {
        return !(date.before(start) || date.after(end));
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
    private class ButtonListener implements ActionListener {

        public ButtonListener() {
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == addCalendarButton) {
                /*
                 * TODO: Implement addCalendar
                 */
            } else if (ae.getSource() == newEventButton) {
                AppointmentWindow appWin = new AppointmentWindow(connection, me);
                appWin.pack();
                appWin.setVisible(true);
                appWin.setLocationRelativeTo(null);
            } else if (ae.getSource() == removeCalendarButton) {
                /*
                 * TODO: Implement removeCalendar()
                 */
            }
        }
    }
}
