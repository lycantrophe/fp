/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.*;

/**
 *
 * @author lycantrophe
 */
public class SelectList extends JFrame {

    private JList invitees;
    private JButton buttonDone;
    private JButton buttonCancel;
    private selectAction al;

    public <T> SelectList(Collection<T> arglist) {

        JPanel panel = new JPanel();
        invitees = new JList(arglist.toArray());
        JScrollPane scrollPane = new JScrollPane(invitees);
        //add(scrollPane);
        panel.add(scrollPane);
        invitees.setCellRenderer(new SelectListRenderer());

        al = new selectAction();
        buttonDone = new JButton("Select");
        buttonCancel = new JButton("Cancel");

        buttonDone.addActionListener(al);
        buttonCancel.addActionListener(al);

        panel.add(buttonDone);
        panel.add(buttonCancel);

        add(panel);
    }

    public ArrayList<Object> getAllSelected() {
        ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(Arrays.asList(invitees.getSelectedValues()));
        return list;
    }

    private class selectAction implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == buttonDone) {
                // TODO: Implement announcment of selected values in this list
                // giveListToParent(getAllSelected);
            }
            dispose();
        }
    }

    public class SelectListRenderer extends JLabel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList jlist, Object o, int i, boolean bln, boolean bln1) {
            String s = "";
            if(o.getClass().getName().equals("FP.Person")){
                s = ((Person) o).getFirstname() + " " + ((Person) o).getSurname() + " [" + ((Person) o).getUsername() + "]";
            }
            else if( o.getClass().getName().equals("FP.Room")){
                s = ((Room) o).getType() + " - " + ((Room) o).getName();
            }
            setText(s);
            
            if (bln) {
                setBackground(jlist.getSelectionBackground());
                setForeground(jlist.getSelectionForeground());
            } else {
                setBackground(jlist.getBackground());
                setForeground(jlist.getForeground());
            }
            setEnabled(jlist.isEnabled());
            setFont(jlist.getFont());
            setOpaque(true);
            return this;
        }
    }
}
