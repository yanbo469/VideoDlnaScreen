package com.yanbo.lib_screen.entity;

import org.fourthline.cling.support.model.DIDLObject;

/**
 * Created by lzan13 on 2018/3/21.
 * 自定义 Cling 实体类，
 */
public class VItem {

    public final static String ROOT_ID = "0";
    public final static String AUDIO_ID = "10";
    public final static String VIDEO_ID = "20";
    public final static String IMAGE_ID = "30";

    public static final DIDLObject.Class AUDIO_CLASS = new DIDLObject.Class(
            "object.container.audio");
    public static final DIDLObject.Class IMAGE_CLASS = new DIDLObject.Class(
            "object.item.imageItem");
    public static final DIDLObject.Class VIDEO_CLASS = new DIDLObject.Class(
            "object.container.video");

}
