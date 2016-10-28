package com.uw_cse.str;

import java.util.Map;

/**
 * Created by James on 10/20/2016.
 *
 * The class for a yes no answer node.
 * Possibly inherits from Discrete Node?
 */
public class BinaryNode extends Node {
    //yes corresponds to true, and no corresponds to false
    private Map<Boolean, Node> connections;

    public BinaryNode(int connectNum, String QID, String question) {
        super(connectNum, QID, question);
    }
}
