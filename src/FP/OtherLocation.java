/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

/**
 *
 * @author lycantrophe
 */
public class OtherLocation extends AbstractLocation {

    public OtherLocation( String name ){
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
}
