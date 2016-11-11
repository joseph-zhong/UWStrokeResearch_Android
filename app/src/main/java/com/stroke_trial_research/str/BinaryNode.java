package com.stroke_trial_research.str;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 10/20/2016.
 *
 * -Purpose
 * The class for a yes no answer node.
 * Possible future implementation will inherit from, or
 * be replaced by the Discrete Node class.
 *
 * Contains a Hashmap of boolean to String of connections
 * Where the string is the name of the corresponding node
 */
public class BinaryNode extends Node {
    //yes corresponds to true, and no corresponds to false
    private Map<Boolean, String> connections;

    //Constructor for all fields
    public BinaryNode(int connectNum, String QID, String question) {
        super(connectNum, QID, question, "BINBUTTON");
        this.connections = new HashMap<Boolean, String>();
    }

    //Adds a node to the list
    //need a yes or no option and a string for the QUID of another node
    public void addNode(String option, String next) {
        if (option.toLowerCase().equals("yes")){
            connections.put(true, next);
        } else {
            connections.put(false, next);
        }
    }

    public Map<Boolean, String> getConnections() {
        return connections;
    }
}
