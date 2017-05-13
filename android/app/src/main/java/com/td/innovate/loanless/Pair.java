package com.td.innovate.loanless;


/**
 * Created by helenpark on 2015-09-09.
 */


public class Pair implements Comparable<Pair>{
    public int key;
    public double value;
    public Pair (int title, double total) {
        key = title;
        value = total;
    }

    @Override
    public int compareTo(Pair p2) {
        if (this.value>p2.value) {
            return 1;
        } else {
            return 0;
        }
    }



}
