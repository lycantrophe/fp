/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;

/**
 *
 * @author lycantrophe
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        // Create log
        Log log = new Log();
        log.setLogName("Server-side application");

        // server connection instance, listen on port 5555
        Connection server = new ConnectionImpl(5555);
        // each new connection lives in its own instance
        Connection conn;
        // TODO: Thread at this point?

        buildAllObjects();

        try {
            conn = server.accept();

            Query query = new Query();
            try {
                String uname = conn.receive();
                String pw = conn.receive();
                if (query.authorize(uname, pw)) {
                    query.close();
                    conn.send("Login successful");
                    User user = new User(uname);
                    user.bind(conn);
                    // Sends "me" and (hopefully) all structures connected to "me"
                    conn.send(Serialize(user.initialSend()));
                    String cmd;
                    while ((cmd = conn.receive()) != null) {
                        // Exit if receives end call
                        if (cmd.equalsIgnoreCase("exit")) {
                            break;
                        }
                        // Input should be in the form: command :: arg1 :: arg2 :: arg3 
                        String[] opts = cmd.split("::");
                        if (opts[0].equalsIgnoreCase("delete")) {

                            if (user.deleteAppointment(opts[1])) {
                                // takes: delete :: [id]
                                // TODO: Validate input
                                conn.send("delete ACK");
                            } else {
                                conn.send("delete FAIL");
                            }

                        } else if (opts[0].equalsIgnoreCase("decline")) {

                            if (user.declineAppointment(opts[1])) {

                                // takes: decline :: [id]
                                conn.send("decline ACK");
                            } else {
                                conn.send("decline FAIL");
                            }
                        } else if (opts[0].equalsIgnoreCase("edit")) {
                            // parse json
                            // TODO: validate input
                            //user.editAppointment(null, null, null, null, cmd, null, null, null); )
                        }
                        /*
                         * do things
                         */
                    }

                    // Send notifications

                } else {
                    // TODO: Wrong login handling.
                    conn.send("Try again!");
                }


            } catch (EOFException e) {
                query.close();
                Log.writeToLog("Got close request (EOFException), closing.",
                        "TestServer");
                conn.close();
            }

            System.out.println("SERVER TEST FINISHED");
            Log.writeToLog("TEST SERVER FINISHED", "TestServer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void buildAllObjects() {

        Query query = new Query();
        Map<String, Person> personMap = new HashMap<String, Person>();
        // Get persons
        for (Person other : query.getPersons()) {
            User.addPerson(other);
            personMap.put(other.getUsername(), other);
        }

        // Get locations
        Map<String, Location> locationMap = new HashMap<String, Location>();
        for (Location location : query.getLocations()) {
            locationMap.put(location.getName(), location);
        }

        // Create appointments

        query.createAppointments(personMap, locationMap);
        query.close();
    }

    public static String Serialize(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new ObjectOutputStream(baos);
        return out.toString();

        /*
         * ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
         * ObjectInputStream ois = new ObjectInputStream (bis); obj =
         * ois.readObject(); TO GET BACK
         */
    }

    public static Object Deserialize(String input) throws IOException, ClassNotFoundException {

        byte[] bytes = input.getBytes();
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }
}
