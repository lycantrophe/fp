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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import no.ntnu.fp.net.co.Connection;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;

/**
 *
 * Alt fra CalendarWindow() SKAL inn i CalendarWindow(Connection connection) og main() skal slettes!
 */
public class CalendarWindow extends JFrame {
    
    private Connection connection;
    private Person me;
    private JPanel monday, tuesday, wednesday, thursday, friday, saturday, sunday, 
    				leftArrowPanel, rightArrowPanel, topPanel, bottomPanel;
    private JButton newEventButton, addCalendarButton, removeCalendarButton, leftArrowButton, rightArrowButton;
    private JLabel week, mondayL, tuesdayL, wednesdayL, thursdayL, fridayL, saturdayL, sundayL;
    private Font font, daysFont;
    private ImageIcon leftArrowIcon, rightArrowIcon;
    private Image imgLeft, imgRight;
    
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
    public CalendarWindow(){
    	setLayout(new GridBagLayout());
    	GridBagConstraints gridConst = new GridBagConstraints();
    	/*
    	 * top panel
    	 */
    	gridConst.gridx = 1; gridConst.gridy = 0; 
    	gridConst.fill = GridBagConstraints.HORIZONTAL; gridConst.ipady = 40;
    	topPanel = new JPanel();
    	add(topPanel, gridConst);
    		font = new Font("", Font.BOLD, 20);
    		week = new JLabel("UKE .."); // TODO: have to generate week			!!!!!!!!!!!
    		week.setFont(font);
    		topPanel.add(week);
    	
    	/*
    	 * leftArrowPanel
    	 */
    	gridConst.gridx = 0; gridConst.gridy = 1; gridConst.ipady=300; gridConst.ipadx = 10;
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
    	leftArrowButton.addActionListener(new LeftArrowButtonAction());
    	leftArrowPanel.add(leftArrowButton, gridArrowL);
    	
    	
    	/*
    	 * monday
    	 */
    	gridConst.gridx = 1; gridConst.gridy = 1; gridConst.ipady=400; gridConst.ipadx=10; 
    	monday = new JPanel();
    	add(monday, gridConst);
    		//add monday-label
    		mondayL = new JLabel(" MONDAY  ");
    		daysFont = new Font("", Font.PLAIN, 16);
    		mondayL.setFont(daysFont);
    	monday.add(mondayL);
    	
    	/*
    	 * tuesday
    	 */
    	gridConst.gridx = 2; gridConst.gridy = 1; gridConst.ipady=400; gridConst.ipadx=10;
    	tuesday = new JPanel();
    	add(tuesday, gridConst);
	    	//add tuesday-label
    		tuesdayL = new JLabel(" TUESDAY ");
    		tuesdayL.setFont(daysFont);
    		tuesday.add(tuesdayL);

    	/*
    	 * wednesday
    	 */
    	gridConst.gridx = 3; gridConst.gridy = 1; gridConst.ipady=400; gridConst.ipadx=10;
    	wednesday = new JPanel();
    	add(wednesday, gridConst);
	    	//add wednesday-label
    		wednesdayL = new JLabel("WEDNESDAY");
    		wednesdayL.setFont(daysFont);
    		wednesday.add(wednesdayL);

    	/*
    	 * thursday
    	 */
    	gridConst.gridx = 4; gridConst.gridy = 1; gridConst.ipady=400; gridConst.ipadx=10;
    	thursday = new JPanel();
    	add(thursday, gridConst);
	    	//add thursday-label
    		thursdayL = new JLabel("THURSDAY ");
    		thursdayL.setFont(daysFont);
    		thursday.add(thursdayL);
    	
    	/*
    	 * friday
    	 */
    	gridConst.gridx = 5; gridConst.gridy = 1; gridConst.ipady=400; gridConst.ipadx=10;
    	friday = new JPanel();
    	add(friday, gridConst);
    		//add friday-label
    		fridayL = new JLabel(" FRIDAY  ");
    		fridayL.setFont(daysFont);
    		friday.add(fridayL);

    	/*
    	 * saturday
    	 */
    	gridConst.gridx = 6; gridConst.gridy = 1; gridConst.ipady=400; gridConst.ipadx=10;
    	saturday = new JPanel();
    	add(saturday, gridConst);
    		//add saturday-label
    		saturdayL = new JLabel("SATURDAY ");
    		saturdayL.setFont(daysFont);
    		saturday.add(saturdayL);

    	/*
    	 * sunday
    	 */
    	gridConst.gridx = 7; gridConst.gridy = 1; gridConst.ipady=400; gridConst.ipadx=10;
    	sunday = new JPanel();
    	add(sunday, gridConst);
    	//add sunday-label
    	sundayL = new JLabel(" SUNDAY  ");
    	sundayL.setFont(daysFont);
    	sunday.add(sundayL);
    
    	/*
    	 * rightArrowPanel
    	 */
    	gridConst.gridx = 8; gridConst.gridy = 1; gridConst.ipady=300; gridConst.ipadx = 10;
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
		rightArrowButton.addActionListener(new RightArrowButtonAction());
		rightArrowPanel.add(rightArrowButton, gridArrowR);

    	/*
    	 * bottomPanel
    	 */
    	gridConst.gridwidth = 4;
    	gridConst.gridx = 4; gridConst.gridy = 2; 
    	gridConst.fill = GridBagConstraints.HORIZONTAL; gridConst.ipady=40 ;
    	bottomPanel = new JPanel(new GridBagLayout());
    	add(bottomPanel, gridConst);
    		//bridBagLayout & constraints
    		GridBagConstraints gridBottom = new GridBagConstraints();
   		gridBottom.gridx = 0; gridBottom.weightx = 10;
   		newEventButton = new JButton("Ny avtale");
   		newEventButton.addActionListener(new NewEventButtonAction());
   		bottomPanel.add(newEventButton, gridBottom);
   		
   		gridBottom.gridx = 1; gridBottom.weightx = 10;
   		addCalendarButton = new JButton("Legg til kalender");
   		addCalendarButton.addActionListener(new AddCalendarButtomAction());
   		bottomPanel.add(addCalendarButton, gridBottom);
   		
   		gridBottom.gridx = 2; gridBottom.weightx = 10;
   		removeCalendarButton = new JButton("Fjern kalender");
   		removeCalendarButton.addActionListener(new RemoveCalendarButtomAction());
   		bottomPanel.add(removeCalendarButton, gridBottom);
        
        /*
         * add borders
         */
        monday.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        tuesday.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        wednesday.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        thursday.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        friday.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        saturday.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        sunday.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }
    
//    public void createNewAppointment() {
//        Date start = startdate.getValue();
//        Date end = enddate.getValue();
//        String description = textDescription.getValue();
//        
//    }

//    public static void main(String[] args){
//		JFrame frame = new CalendarWindow();
//		frame.setSize(new Dimension(1200,700));
//		frame.setLocationRelativeTo(null);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setVisible(true);
//	}
    /*
     * Arrow action classes
     */
    protected class LeftArrowButtonAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO: Have to generate previous week
		}
    	
    }
    protected class RightArrowButtonAction implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		// TODO: Have to generate next week
    	}
    	
    }
    /*
     * bottomPanel buttons action classes
     */
    protected class NewEventButtonAction implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		// TODO: 
    	}
    	
    }
    protected class AddCalendarButtomAction implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		// TODO: 
    	}
    	
    }
    protected class RemoveCalendarButtomAction implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
    		// TODO: 
    	}
    	
    }
    
    
    
}
