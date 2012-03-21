package FP;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class NotificationWindow extends JFrame {

    private JLabel notification;
    private String appId;
    private JButton accept, decline;
    private CalendarWindow calWin;
    private int index;
    
    public NotificationWindow(String message, CalendarWindow calWin, int index) {
        this.calWin = calWin;
        this.index = index;
        appId = (message.split("::"))[1];
        drawWindow(message);
    }

    private void drawWindow(String message) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(15, 15, 10, 15);
        notification = new JLabel(message);
        notification.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        add(notification, c);

        c.gridy = 1;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(10, 15, 5, 15);

        ButtonAction al = new ButtonAction(calWin);

        accept = new JButton("Accept");
        accept.addActionListener(al);
        decline = new JButton("Decline");
        decline.addActionListener(al);
        JPanel pnl = new JPanel();
        pnl.add(accept);
        pnl.add(decline);
        add(pnl, c);
    }

    class ButtonAction implements ActionListener {

        private CalendarWindow calWin;

        public ButtonAction(CalendarWindow calWin) {
            this.calWin = calWin;
        }

        public void actionPerformed(ActionEvent e) {
            String status = "decline";
            if (e.getSource() == accept) {
                status = "accept";
            }
            calWin.sendStatusChange(status, appId, index);
            dispose();
        }
    }
}
