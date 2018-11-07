package com.yanbo.lib_screen.entity;

/**
 * Created by lzan13 on 2018/4/11.
 */
public class AVTransportInfo {

    public static String TRANSITIONING = "TRANSITIONING";
    public static String PLAYING = "PLAYING";
    public static String PAUSED_PLAYBACK = "PAUSED_PLAYBACK";
    public static String STOPPED = "STOPPED";

    private String state;
    private String mediaDuration;
    private String timePosition;

    public AVTransportInfo() {}

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMediaDuration() {
        return mediaDuration;
    }

    public void setMediaDuration(String mediaDuration) {
        this.mediaDuration = mediaDuration;
    }

    public String getTimePosition() {
        return timePosition;
    }

    public void setTimePosition(String timePosition) {
        this.timePosition = timePosition;
    }
}
