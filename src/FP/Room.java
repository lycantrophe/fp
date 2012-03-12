/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

/**
 *
 * @author lycantrophe
 */
public class Room extends AbstractLocation {

    private int capacity;
    private Roomtype type;

    public Room( int id, String name, int capacity, Roomtype type ){
        super( name);
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.type = type;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
