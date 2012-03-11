/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

/**
 *
 * @author lycantrophe
 */
public abstract class AbstractLocation implements Location {
    
    protected String name;
    protected int id;
    protected enum Roomtype {

        CONFERENCE_ROOM, CAFE, AUDITORIUM
    }
}
