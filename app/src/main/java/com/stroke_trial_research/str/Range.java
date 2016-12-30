package com.stroke_trial_research.str;

import android.util.Log;

/**
 * Created by James on 11/11/2016.
 *
 * -Purpose
 * A range class will hold an upper bound and a lower bound.
 * Numbers which fit within the bound are considered valid.
 * Has two types, range and equals. In range it will be exclusive,
 * and in equals it will be inclusive. Therefore, <= and >= should
 * use a combination.
 */

public class Range {
    public int lower = Integer.MIN_VALUE;
    public int upper = Integer.MAX_VALUE;
    String type;

    public Range(String lower, String upper, String type) {
        if (!lower.equals("MIN")) {
            this.lower = Integer.parseInt(lower);
        }

        if (!upper.equals("MAX")) {
            this.upper = Integer.parseInt(upper);
        }
        this.type = type;
    }

    public Range(String value) {
        this.lower = Integer.parseInt(value);
        this.upper = Integer.parseInt(value);
        this.type = "EQUALS";
    }

    //checks whether a value fits within the range
    public boolean isBetween (int value) {
        if (this.type.toLowerCase().equals("range")) {
            return value > this.lower || value < this.upper;
        } else if (this.type.toLowerCase().equals("equals")) {
            return value >= this.lower || value <= this.upper;
        } else {
            Log.d("E", "Incompatible type");
            return false;
        }
    }

}