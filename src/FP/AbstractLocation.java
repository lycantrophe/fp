/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.io.Serializable;

/**
 *
 * @author lycantrophe
 */
public abstract class AbstractLocation implements Location, Serializable {

    protected String name;
    protected int id;

    protected enum Roomtype {

        CONFERENCE_ROOM, CAFE, AUDITORIUM
    }

    protected AbstractLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
}
