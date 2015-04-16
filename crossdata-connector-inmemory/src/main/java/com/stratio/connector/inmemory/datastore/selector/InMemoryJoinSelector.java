package com.stratio.connector.inmemory.datastore.selector;

import com.stratio.crossdata.common.statements.structures.Selector;

/**
 * Created by lcisneros on 9/04/15.
 */
public class InMemoryJoinSelector extends InMemorySelector{

    private Selector myTerm;
    private Selector otherTerm;

    public InMemoryJoinSelector(String name, Selector myTerm, Selector otherTerm) {
        super(name);
        this.myTerm = myTerm;
        this.otherTerm = otherTerm;
    }

    public Selector getOtherTerm() {
        return otherTerm;
    }

    public Selector getMyTerm() {
        return myTerm;
    }
}
