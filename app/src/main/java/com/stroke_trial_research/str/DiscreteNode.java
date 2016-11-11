package com.stroke_trial_research.str;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 10/20/2016.
 *
 * -Purpose
 *  For use with the BUTTON tag in the json file.
 *  Allows user to link a response to a node connection.
 *
 *  Contains a Hashmap linking phrases for buttons
 *  to connection node QIDS.
 *
 */
public class DiscreteNode extends Node {
    //Connections of phrase to QID
    private Map<String, String> connections;

    //Constructor for all fields
    public DiscreteNode(int connectNum, String QID, String question) {
        super(connectNum, QID, question, "BUTTON");
        this.connections = new HashMap<String, String>();
    }

    //Adds a new node to the connections map.
    //requires a phrase option and a QID for next
    public void addNode(String option, String next){
        connections.put(option, next);
    }

    public Map<String, String> getConnections() {
        return connections;
    }
}
