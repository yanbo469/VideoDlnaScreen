package com.yanbo.lib_screen.entity;

import org.fourthline.cling.model.meta.Device;

/**
 * Created by lzan13 on 2018/3/5.
 */
public class ClingDevice {
    private Device device;
    private boolean isSelected = false;

    public ClingDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
