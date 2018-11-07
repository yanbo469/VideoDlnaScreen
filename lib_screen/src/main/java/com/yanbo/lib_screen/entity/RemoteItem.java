package com.yanbo.lib_screen.entity;

/**
 * Created by lzan13 on 2018/3/24.
 * 网络资源实体类
 */
public class RemoteItem {
    private String title;
    private String id;
    private String creator;
    private long size;
    private String duration;
    private String resolution;
    private String url;

    public RemoteItem(String title, String id, String creator, long size, String duration,
                      String resolution, String url) {
        setTitle(title);
        setId(id);
        setCreator(creator);
        setSize(size);
        setDuration(duration);
        setResolution(resolution);
        setUrl(url);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
