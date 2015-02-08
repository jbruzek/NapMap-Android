package com.bruzek.jbruzek.myapplication;

/**
 * Created by jbruzek on 2/7/15.
 */
public interface ParseCallbacks {

    public void complete();
    public void commentsComplete();
    public void completeFullness(int full);
}
