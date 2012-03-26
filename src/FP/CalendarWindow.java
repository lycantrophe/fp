/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import no.ntnu.fp.net.co.Connection;

public class CalendarWindow extends JFrame implements SelectionInterface {

    private Connection connection;
    private Person me;
    private JPanel leftArrowPanel, rightArrowPanel, topPanel, topPanel2, bottomPanel,
            monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private JButton newEventButton, addCalendarButton, removeCalendarButton, getNotificationsButton, leftArrowButton, rightArrowButton;
    private Font weekFont, daysFont;
    private JLabel labelWeek, labelYear;
    private ImageIcon leftArrowIcon, rightArrowIcon;
    private String[] dayNames = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    private JPanel[] dayColumns = {monday, tuesday, wednesday, thursday, friday, saturday, sunday};
    private GridBagConstraints gridConst;
    private Calendar today;
    private ArrowButtonListener arrowListener;
    private ButtonListener buttonListener;
    private labelClickListener cbl;
    private Map<String, Person> allPersons;
    private Map<String, Location> allLocations;
    private ArrayList<String> selectedPersons;
    private JList notifications;
    private DefaultListModel listModel;
    private JScrollPane listScrollPane;

    public CalendarWindow(Connection connection) throws IOException, ClassNotFoundException {

        me = (Person) Server.Deserialize(connection.receive());
        allPersons = (HashMap<String, Person>) Server.Deserialize(connection.receive());
        allLocations = (HashMap<String, Location>) Server.Deserialize(connection.receive());
        selectedPersons = new ArrayList<String>();
        selectedPersons.add(me.getUsername());
        today = Calendar.getInstance();
        today.add(Calendar.DATE, -(today.get(Calendar.DAY_OF_WEEK) + 1));
        this.connection = connection;
        cbl = new labelClickListener(this);

        setLayout(new GridBagLayout());
        gridConst = new GridBagConstraints();

        arrowListener = new ArrowButtonListener();
        buttonListener = new ButtonListener(this);

        drawWindow();
        mapAppointments();

        for (String notification : me.getNotifications()) {
            listModel.addElement(notification);
        }
        //c.add(Calendar.DATE, -(c.get(Calendar.DAY_OF_WEEK) + i));

    }

    private void mapAppointments() {
        labelWeek.setText("Week " + today.get(Calendar.WEEK_OF_YEAR));
        labelYear.setText("" + today.get(Calendar.YEAR));

        for (int i = 0; i < 7; i++) {
            dayColumns[i].removeAll();
            dayColumns[i].add(new JLabel(dayNames[i]));
            dayColumns[i].validate();
            dayColumns[i].repaint();
        }

        Calendar firstday = today;
        Calendar lastday = Calendar.getInstance();
        lastday.setTime(firstday.getTime());
        lastday.add(Calendar.DATE, 6);
        Calendar dayToPaint = Calendar.getInstance();

        if (me == null) {
            System.out.println("I DON'T EXIST");
        } else {
            System.out.println("I do exist! Yes I can " + me.getUsername());
        }
        Appointment appointment;
        for (String person : selectedPersons) {
            for (String appId : allPersons.get(person).getAppointmentIds()) {
                System.out.println("Appointment ID: " + appId);
                appointment = allPersons.get(person).getAppointment(appId);

                System.out.println("Start: " + appointment.getStart().toString());
                System.out.println("First: " + firstday.getTime().toString());
                System.out.println("Last: " + lastday.getTime().toString());

                if (isWithinRange(appointment.getStart(), firstday.getTime(), lastday.getTime())) {
                    dayToPaint.setTime(appointment.getStart());
                    JLabel toBeAdded = new JLabel();
                    toBeAdded.setText(appointment.getId() + " " + appointment.getStart().toString() + " - "
                            + appointment.getDescription());
                    toBeAdded.setName(appointment.getId());
                    toBeAdded.addMouseListener(cbl);
                    dayColumns[dayToPaint.get(Calendar.DAY_OF_WEEK) - 1].add(toBeAdded);
                    dayColumns[dayToPaint.get(Calendar.DAY_OF_WEEK) - 1].validate();
                    dayColumns[dayToPaint.get(Calendar.DAY_OF_WEEK) - 1].repaint();
                }
            }
        }
    }

    private void drawWindow() {
        /*
         * Top panel
         */

        weekFont = new Font("", Font.BOLD, 18);
        labelWeek = new JLabel("12");
        labelWeek.setFont(weekFont);
        topPanel = new JPanel();
        topPanel.add(labelWeek);

        gridConst.gridx = 1;
        gridConst.gridy = 0;

        add(topPanel, gridConst);

        /*
         * Top panel2
         */

        weekFont = new Font("", Font.BOLD, 18);
        labelYear = new JLabel("12");
        labelYear.setFont(weekFont);
        topPanel = new JPanel();
        topPanel.add(labelYear);

        gridConst.gridx = 2;
        gridConst.gridy = 0;

        add(topPanel, gridConst);

        /*
         * Left arrow panel
         */

        leftArrowIcon = new ImageIcon("/home/lycantrophe/NetBeansProjects/fp/src/FP/leftArrow.png");
        leftArrowButton = new JButton(leftArrowIcon);
        leftArrowButton.addActionListener(arrowListener);
        leftArrowPanel = new JPanel();
        leftArrowPanel.add(leftArrowButton);

        gridConst.gridx = 0;
        gridConst.gridy = 1;

        add(leftArrowPanel, gridConst);

        /*
         * Day panels
         */

        daysFont = new Font("", Font.PLAIN, 16);
        JLabel dayLabel;

        for (int i = 0; i < 7; i++) {
            dayLabel = new JLabel(dayNames[i]);
            dayLabel.setFont(daysFont);
            dayColumns[i] = new JPanel();
            dayColumns[i].add(dayLabel);
            dayColumns[i].setPreferredSize(new Dimension(150, 400));
            dayColumns[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

            gridConst.gridx = i + 1;

            add(dayColumns[i], gridConst);
        }

        /*
         * Right arrow panel
         */

        rightArrowIcon = new ImageIcon("/home/lycantrophe/NetBeansProjects/fp/src/FP/rightArrow.png");
        rightArrowButton = new JButton(rightArrowIcon);
        rightArrowButton.addActionListener(arrowListener);
        rightArrowPanel = new JPanel();
        rightArrowPanel.add(rightArrowButton);

        gridConst.gridx = 8;
        gridConst.gridy = 1;

        add(rightArrowPanel, gridConst);

        /*
         * Bottom panel
         */

        listModel = new DefaultListModel();
        notifications = new JList(listModel);
        notifications.addMouseListener(new DoubleClickListener(this));
        listScrollPane = new JScrollPane(notifications);
        listScrollPane.setPreferredSize(new Dimension(300, 60));
        getNotificationsButton = new JButton("Get notifications");
        getNotificationsButton.addActionListener(buttonListener);
        newEventButton = new JButton("New Appointment");
        newEventButton.addActionListener(buttonListener);
        addCalendarButton = new JButton("Add Calendar");
        addCalendarButton.addActionListener(buttonListener);
        removeCalendarButton = new JButton("Remove Calendar");
        removeCalendarButton.addActionListener(buttonListener);

        bottomPanel = new JPanel();
        bottomPanel.add(listScrollPane);
        bottomPanel.add(getNotificationsButton);
        bottomPanel.add(newEventButton);
        bottomPanel.add(addCalendarButton);
        bottomPanel.add(removeCalendarButton);

        gridConst.anchor = GridBagConstraints.PAGE_START;
        gridConst.gridwidth = 7;
        gridConst.gridx = 1;
        gridConst.gridy = 2;

        add(bottomPanel, gridConst);
    }

    public void addNewAppointment(Appointment newAppointment) {
        allPersons.get(me.getUsername()).addAppointment(newAppointment);
        mapAppointments();

        try {
            connection.send("getNotifications");
            for (String notification :
                    (ArrayList<String>) Server.Deserialize(connection.receive())) {
                listModel.addElement(notification);
            }
        } catch (ConnectException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void sendStatusChange(String status, String appid, int index) {
        try {
            connection.send(status);
            connection.send(appid);
            listModel.remove(index);
        } catch (ConnectException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean isWithinRange(Date date, Date start, Date end) {
        return !(date.before(start) || date.after(end));
    }

    @Override
    public <T> void getSelectedValues(ArrayList<T> selected) {
        this.selectedPersons = (ArrayList<String>) selected;
        if (!selectedPersons.contains(me.getUsername())) {
            selectedPersons.add(me.getUsername());
        }
        mapAppointments();
    }

    @Override
    public <T> void getSelectedValues(T t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class labelClickListener extends MouseAdapter {

        private CalendarWindow calWin;

        public labelClickListener(CalendarWindow calWin) {
            this.calWin = calWin;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                // HACK
                String appid = e.getComponent().getName();
                EditAppointmentWindow editAppWin = new EditAppointmentWindow(connection, me, me.getAppointment(appid), allPersons, allLocations, calWin);
                editAppWin.pack();
                editAppWin.setVisible(true);
                editAppWin.setLocationRelativeTo(null);

            }
        }
    }

    class DoubleClickListener extends MouseAdapter {

        private CalendarWindow calWin;

        public DoubleClickListener(CalendarWindow calWin) {
            this.calWin = calWin;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = notifications.locationToIndex(e.getPoint());
                System.out.println("Double clicked on Item " + index);
                Object item = listModel.getElementAt(index);
                System.out.println(item.toString());
                
                try {
                    connection.send("removeNotification");
                    connection.send(item.toString());
                } catch (ConnectException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
                NotificationWindow notificationWin = new NotificationWindow(item.toString(), calWin, index);
                notificationWin.setLocationRelativeTo(null);
                notificationWin.pack();
                notificationWin.setVisible(true);
            }
        }
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

        private CalendarWindow parent;

        public ButtonListener(CalendarWindow parent) {
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == addCalendarButton) {

                ArrayList<String> arr = new ArrayList<String>();
                for (String other : allPersons.keySet()) {
                    arr.add(other);
                    System.out.println("Adding " + other + "to invSel");
                }
                SelectList invList = new SelectList(arr, parent);
                invList.pack();
                invList.setVisible(true);
                invList.setLocationRelativeTo(null);

            } else if (ae.getSource() == newEventButton) {
                AppointmentWindow appWin = new AppointmentWindow(connection, me, allPersons, allLocations, parent);
                appWin.pack();
                appWin.setVisible(true);
                appWin.setLocationRelativeTo(null);
            } else if (ae.getSource() == removeCalendarButton) {
                selectedPersons.clear();
                selectedPersons.add(me.getUsername());
                mapAppointments();
            } else if (ae.getSource() == getNotificationsButton) {
                try {
                    connection.send("getNotifications");
                    for (String notification :
                            (ArrayList<String>) Server.Deserialize(connection.receive())) {
                        listModel.addElement(notification);
                    }
                } catch (ConnectException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}