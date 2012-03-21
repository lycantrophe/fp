/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.Connection;

/**
 *
 * @author lycantrophe
 */
public class ServerThread extends Thread {

    private Connection connection;

    public ServerThread(Connection connection) {
        this.connection = connection;
    }

    // handle exceptions
    public void run() {
        Query query = new Query();
        // TODO: Loop around sending user/pass for several login attempts
        try {
            String uname;
            uname = connection.receive();
            String pw = connection.receive();

            if (query.authorize(uname, pw)) {
                connection.send("Login successful");
                User user = new User(uname);
                user.bind(connection);
                // Sends "me" and (hopefully) all structures connected to "me"
                connection.send(Server.Serialize(user.initialSend()));

                // Sends other needed data structures
                connection.send(Server.Serialize(Server.persons));
                connection.send(Server.Serialize(Server.locations));

                String cmd;
                while ((cmd = connection.receive()) != null) {
                    // Exit if receives end call
                    if (cmd.equalsIgnoreCase("exit")) {
                        break;
                    }

                    if (cmd.equalsIgnoreCase("delete")) {

                        cmd = connection.receive();

                        if (user.deleteAppointment(cmd)) {
                            // takes: delete :: [id]
                            // TODO: Validate input
                            connection.send("delete ACK");
                        } else {
                            connection.send("delete FAIL");
                        }

                    } else if (cmd.equalsIgnoreCase("decline")) {
                        String id = connection.receive();
                        user.declineAppointment(id);
                        
                    } else if (cmd.equalsIgnoreCase("accept")) {
                        String id = connection.receive();
                        user.acceptAppointment(id);
                        
                    } else if (cmd.equalsIgnoreCase("edit")) {
                        cmd = connection.receive();
                        Appointment newAppointment = null;
                        try {
                            newAppointment = (Appointment) Server.Deserialize(connection.receive());
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                            ex.printStackTrace();
                        }
                        if (newAppointment != null) {
                            user.editAppointment(cmd, newAppointment);
                        } else {
                            throw new AssertionError("Could not get proper object from client");
                        }

                    } else if (cmd.equalsIgnoreCase("create")) {
                        System.out.println("Starting creation routine");
                        try {
                            Appointment app = query.createAppointment((Appointment) Server.Deserialize(connection.receive()));
                            connection.send(Server.Serialize(app));
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    } else if (cmd.equalsIgnoreCase("getNotifications")) {
                        connection.send(Server.Serialize(Server.persons.get(uname).getNotifications()));
                    }
                    /*
                     * do things
                     */
                }

                // Send notifications

            } else {
                // TODO: Wrong login handling.
                connection.send("Try again!");
            }
        } catch (EOFException e) {
            query.close();
            Log.writeToLog("Got close request (EOFException), closing.",
                    "TestServer");
            try {
                connection.close();


            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }


        } catch (ConnectException e) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
