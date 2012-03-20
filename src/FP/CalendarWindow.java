/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import no.ntnu.fp.net.co.Connection;

public class CalendarWindow extends JFrame {

	/*
	 * delete 2. constructor!
	 */
    private Connection connection;
    private Person me;
    private JPanel leftArrowPanel, rightArrowPanel, topPanel, bottomPanel,
            monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private JButton newEventButton, addCalendarButton, removeCalendarButton, leftArrowButton, rightArrowButton;
    private Font weekFont, daysFont;
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
    private JList notifications;
    private DefaultListModel listModel;
    private JScrollPane listScrollPane;

    public CalendarWindow(){
        today = Calendar.getInstance();

        setLayout(new GridBagLayout());
        gridConst = new GridBagConstraints();

        thisWeek = new HashMap<String, Calendar>();

        arrowListener = new ArrowButtonListener();
        buttonListener = new ButtonListener();

        drawWindow();
    }
    public CalendarWindow(Connection connection) throws IOException, ClassNotFoundException {

        me = (Person) Server.Deserialize(connection.receive());
        today = Calendar.getInstance();

        setLayout(new GridBagLayout());
        gridConst = new GridBagConstraints();

        thisWeek = new HashMap<String, Calendar>();

        arrowListener = new ArrowButtonListener();
        buttonListener = new ButtonListener();

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
            firstday.add(Calendar.DATE, -(today.get(Calendar.DAY_OF_WEEK) + 1));
            Calendar lastday = firstday;
            lastday.add(Calendar.DATE, 6);
            if (isWithinRange(appointment.getStart(), firstday.getTime(), lastday.getTime())) {
                firstday.setTime(appointment.getStart());
                // TODO: New JLabel() should be approperiate element
                dayColumns[firstday.get(Calendar.DAY_OF_WEEK) - 1].add(new JLabel());
            }
        }
    }

    private void drawWindow()
    {
    	/*
    	 * Top panel
    	 */
    	
    	weekFont = new Font("", Font.BOLD, 20);
    	labelWeek = new JLabel("12");
        labelWeek.setFont(weekFont);
    	topPanel = new JPanel();
    	topPanel.add(labelWeek);
    	
    	gridConst.gridx = 1;
        gridConst.gridy = 0;
        
        add(topPanel, gridConst);
        
        /*
         * Left arrow panel
         */
        
        imgLeft = getToolkit().createImage(getClass().getResource("leftArrow.png"));
        leftArrowIcon = new ImageIcon(imgLeft);
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
        
        for (int i = 0; i < 7; i++)
        {
        	dayLabel = new JLabel(dayNames[i]);
            dayLabel.setFont(daysFont);
        	dayColumns[i] = new JPanel();
        	dayColumns[i].add(dayLabel);
        	dayColumns[i].setPreferredSize(new Dimension(130, 400));
            dayColumns[i].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        	gridConst.gridx = i + 1;
            
            add(dayColumns[i], gridConst);
        }

        /*
         * Right arrow panel
         */
        
        imgRight = getToolkit().createImage(getClass().getResource("rightArrow.png"));
        rightArrowIcon = new ImageIcon(imgRight);
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
        listModel.addElement("Hei");
        listModel.addElement("Hei");
        listModel.addElement("Hei");
        listModel.addElement("Hei");
        listModel.addElement("Hei");
        listModel.addElement("Hei");
        listModel.addElement("Hei");
        notifications = new JList(listModel);
        notifications.addMouseListener(new DoubleClickListener());
        listScrollPane = new JScrollPane(notifications);
        listScrollPane.setPreferredSize(new Dimension(500, 60));
        newEventButton = new JButton("New Appointment");
        newEventButton.addActionListener(buttonListener);
        addCalendarButton = new JButton("Add Calendar");
        addCalendarButton.addActionListener(buttonListener);
        removeCalendarButton = new JButton("Remove Calendar");
        removeCalendarButton.addActionListener(buttonListener);
        bottomPanel = new JPanel();
        bottomPanel.add(listScrollPane);
        bottomPanel.add(newEventButton);
        bottomPanel.add(addCalendarButton);
        bottomPanel.add(removeCalendarButton);
        
        gridConst.anchor = GridBagConstraints.PAGE_START;
        gridConst.gridwidth = 7;
        gridConst.gridx = 1;
        gridConst.gridy = 2;
        
        add(bottomPanel, gridConst);
    }

    private boolean isWithinRange(Date date, Date start, Date end) {
        return !(date.before(start) || date.after(end));
    }
    
    class DoubleClickListener extends MouseAdapter
    {
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (e.getClickCount() == 2)
			{
				int index = notifications.locationToIndex(e.getPoint());
	            System.out.println("Double clicked on Item " + index);
	            Object item = listModel.getElementAt(index);
	            System.out.println(item.toString());
	            
	            NotificationWindow notificationWin = new NotificationWindow(item.toString());
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

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == addCalendarButton) {
                /*
                 * TODO: Implement addCalendar
                 */
            } else if (ae.getSource() == newEventButton) {
                
                AppointmentWindow appWin = new AppointmentWindow(connection, me);
                appWin.pack();
                appWin.setVisible(true);
                
            } else if (ae.getSource() == removeCalendarButton) {
                /*
                 * TODO: Implement removeCalendar()
                 */
            }
        }
    }
    
    public static void main(String[] args){
	JFrame frame = new CalendarWindow();
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
}
}
