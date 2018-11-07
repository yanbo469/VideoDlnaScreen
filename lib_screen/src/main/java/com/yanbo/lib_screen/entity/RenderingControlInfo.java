package com.yanbo.lib_screen.entity;

/**
 * Created by lzan13 on 2018/4/11.
 */
public class RenderingControlInfo {
    private boolean isMute;
    private int volume;
    private String presetNameList;

    public RenderingControlInfo() {}

    public RenderingControlInfo(boolean mute, int volume) {
        this.isMute = mute;
        this.volume = volume;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getPresetNameList() {
        return presetNameList;
    }

    public void setPresetNameList(String presetNameList) {
        this.presetNameList = presetNameList;
    }
}
