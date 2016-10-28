package com.uw_cse.str;

import android.graphics.Point;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by James on 10/20/2016.
 * The class for a node where the user must
 * enter a range and different ranges have different functions.
 */
public class RangeNode extends Node{

    private Map<RangePair, Node> connections;
    private int max;
    private int min;

    public RangeNode(int max, int min, int connectNum, String QID, String question) {
        super(connectNum, QID, question);
        this.max = max;
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

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
    }

}
