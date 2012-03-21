/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FP;

import java.util.ArrayList;

/**
 *
 * @author lycantrophe
 */
public interface SelectionInterface {
    public <T> void getSelectedValues( ArrayList<T> list );
     public <T> void getSelectedValues( T t );
}
