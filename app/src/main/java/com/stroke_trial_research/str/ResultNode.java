package com.stroke_trial_research.str;
/**
 * Created by James on 11/10/2016.
 *
 * -Purpose
 * For use with tags with the "r" prefix instead of the "q" prefix.
 * Holds a message to display to users in the question field.
 * Has no connecting nodes.
 * Contains a phone number string and a researcher name for contacting
 *
 * -Current valid "types" based on JSON file
 * UNKNOWN
 * RESULT
 */

public class ResultNode extends Node {
    //Possibly defunct fields
    private String phone;
    private String researcher;

    //Constructor for if a phone and researcher are not given
         public ResultNode(String QID, String question, String type) {
        super(0, QID, question, type);
        this.phone = "";
        this.researcher = "";
    }


     //Complete field constructor
    public ResultNode(String QID, String question, String type,
                      String phone, String researcher) {
        super(0, QID, question, type);
        this.phone = phone;
        this.researcher = researcher;
    }

    public String getPhone() {
        return phone;
    }

    public String getResearcher() {
        return researcher;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setResearcher(String researcher) {
        this.researcher = researcher;
    }
}
