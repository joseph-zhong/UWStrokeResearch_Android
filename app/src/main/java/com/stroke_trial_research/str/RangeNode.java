package com.stroke_trial_research.str;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 10/20/2016.
 *
 * -Purpose
 * The class for a node where the user inputs a number
 * on a scale and it is checked to see if it fits within
 * a certain range.
 * Contains a map where certain Range instances are connected
 * to corresponding QIDS
 */
public class RangeNode extends Node{

    private Map<Range, String> connections;

    public RangeNode(int connectNum, String QID, String question) {
        super(connectNum, QID, question, "NUMBER");
        this.connections = new HashMap<Range, String>();
    }

    //Adds a node for an explicit range. Requires an upper or lower bound
    public void addRangeNode(String lower, String upper, String type, String next) {
        Range range = new Range(lower, upper, type);
        this.connections.put(range, next);
    }

    //For adding a new connection
    public void addEqualsNode(String value, String next) {
        Range equals = new Range(value);
        this.connections.put(equals, next);
    }


    /*
    //Possible implementation of Storing ranges
    //wherein we use regular expressions.
    public class RangePair {
        public final String[] SYMBOLS= {"!=", "==", ">", "<", ">=", "<=", "\0"};
        public final Set<String> VALID_COMPS = new HashSet<String>(Arrays.asList(SYMBOLS));

        private int value;
        private String comparator;

        public RangePair(int value, String comparator) {
            this.value = value;
            //gives special symbol if it can't tell the comparison
            if (VALID_COMPS.contains(comparator)) {
                this.comparator = comparator;
            } else {
                Log.e("RangeNode", "Illegal expression");
                this.comparator = "\0";
            }
        }
    } */
}