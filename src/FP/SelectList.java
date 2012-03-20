/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author lycantrophe
 */
public class SelectList extends JFrame {

    private JList invitees;
    private JButton buttonDone;
    private JButton buttonCancel;
    private selectAction al;

    public SelectList( ArrayList<Object> arglist) {
        // availiblePersons is the arraylist of registered persons ;
        // TODO: Consider sending an object instance as 2nd argument  for calling a useSelected() method

        invitees = new JList(arglist.toArray());
        JScrollPane scrollPane = new JScrollPane(invitees);
        add(scrollPane);

        al = new selectAction();
        buttonDone = new JButton();
        buttonCancel = new JButton();

        buttonDone.addActionListener(al);
        buttonCancel.addActionListener(al);

        add(buttonDone);
        add(buttonCancel);
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
}
