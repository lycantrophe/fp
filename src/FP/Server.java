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

    private static Map<String, Person> persons;
    private static Map<String, Location> locations;
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

        while (true) {
            try {
                conn = server.accept();

                Thread thread = new ServerThread(conn, persons, locations );
                thread.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void buildAllObjects() {

        Query query = new Query();
        Map<String, Person> persons = new HashMap<String, Person>();
        // Get persons
        for (Person other : query.getPersons()) {
            User.addPerson(other);
            persons.put(other.getUsername(), other);
        }

        // Get locations
        locations = new HashMap<String, Location>();
        for (Location location : query.getLocations()) {
            locations.put(location.getName(), location);
        }

        // Create appointments

        query.createAllAppointments(persons, locations);
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
