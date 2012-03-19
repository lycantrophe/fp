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
public class Attending implements Serializable {

    public enum Status {

        ATTENDING, PENDING, DECLINED
    };
    private Person person;
    private Status status;

    public Attending(Person person) {
        this.person = person;
        this.status = Status.PENDING;
    }

    public Attending(Person person, Status status) {
        this.person = person;
        this.status = status;
    }

    public Person getPerson() {
        return person;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}