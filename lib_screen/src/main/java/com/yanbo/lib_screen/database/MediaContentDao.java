/*
 * Copyright (C) 2014 Kevin Shen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanbo.lib_screen.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;

import com.yanbo.lib_screen.VConstants;
import com.yanbo.lib_screen.entity.VItem;
import com.yanbo.lib_screen.utils.VMNetwork;

import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.ImageItem;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.support.model.item.Movie;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.seamless.util.MimeType;

import java.io.File;
import java.util.ArrayList;


public class MediaContentDao {

    private ContentResolver cr;
    private String serverURL;

    public MediaContentDao(Context context) {
        cr = context.getContentResolver();
        serverURL = "http://" + VMNetwork.getLocalIP() + ":" + VConstants.JETTY_SERVER_PORT + "/";
    }

    public ArrayList<Item> getAudioItems() {
        ArrayList<Item> items = new ArrayList<>();

        String[] audioColumns = {Audio.Media._ID, Audio.Media.TITLE, Audio.Media.DATA,
                Audio.Media.ARTIST, Audio.Media.MIME_TYPE, Audio.Media.SIZE, Audio.Media.DURATION,
                Audio.Media.ALBUM};

        Cursor cur = cr.query(Audio.Media.EXTERNAL_CONTENT_URI, audioColumns, null, null, null);

        if (cur == null) {
            return items;
        }

        while (cur.moveToNext()) {
            String id = String.valueOf(cur.getInt(cur.getColumnIndex(Audio.Media._ID)));
            String title = cur.getString(cur.getColumnIndex(Audio.Media.TITLE));
            String creator = cur.getString(cur.getColumnIndex(Audio.Media.ARTIST));
            String mimeType = cur.getString(cur.getColumnIndex(Audio.Media.MIME_TYPE));
            long size = cur.getLong(cur.getColumnIndex(Audio.Media.SIZE));
            long duration = cur.getLong(cur.getColumnIndex(Audio.Media.DURATION));
            String durationStr = ModelUtil.toTimeString(duration / 1000);
            String album = cur.getString(cur.getColumnIndex(Audio.Media.ALBUM));
            String data = cur.getString(cur.getColumnIndexOrThrow(Audio.Media.DATA));
            String fileName = data.substring(data.lastIndexOf(File.separator));
            String ext = fileName.substring(fileName.lastIndexOf("."));
            data = data.replace(fileName, File.separator + id + ext);
            String url = serverURL + "audio" + data;
            Res res = new Res(mimeType, size, durationStr, null, url);
            MusicTrack musicTrack = new MusicTrack(id, VItem.AUDIO_ID, title, creator, album,
                    new PersonWithRole(creator), res);
            items.add(musicTrack);
        }

        return items;
    }

    public ArrayList<Item> getImageItems() {
        ArrayList<Item> items = new ArrayList<>();

        String[] imageColumns = {Images.Media._ID, Images.Media.TITLE, Images.Media.DATA,
                Images.Media.MIME_TYPE, Images.Media.SIZE};

        Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, null);

        if (cur == null) {
            return items;
        }

        while (cur.moveToNext()) {
            String id = String.valueOf(cur.getInt(cur.getColumnIndex(MediaStore.Images.Media._ID)));
            String title = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.TITLE));
            String creator = cur.getString(cur.getColumnIndexOrThrow(Images.Media.TITLE));
            String mimeType = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
            long size = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.SIZE));
            String data = cur.getString(cur.getColumnIndexOrThrow(Audio.Media.DATA));
            String fileName = data.substring(data.lastIndexOf(File.separator));
            String ext = fileName.substring(fileName.lastIndexOf("."));
            data = data.replace(fileName, File.separator + id + ext);
            String url = serverURL + "image" + data;
            Res res = new Res(new MimeType(mimeType.substring(0, mimeType.indexOf('/')),
                    mimeType.substring(mimeType.indexOf('/') + 1)), size, url);
            ImageItem imageItem = new ImageItem(id, VItem.IMAGE_ID, title, creator, res);
            items.add(imageItem);
        }

        return items;
    }

    public ArrayList<Item> getVideoItems() {
        ArrayList<Item> items = new ArrayList<>();

        String[] videoColumns = {Video.Media._ID, Video.Media.TITLE, Video.Media.DATA,
                Video.Media.ARTIST, Video.Media.MIME_TYPE, Video.Media.SIZE, Video.Media.DURATION,
                Video.Media.RESOLUTION};

        Cursor cur = cr.query(Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);

        if (cur == null) {
            return items;
        }

        while (cur.moveToNext()) {
            String id = String.valueOf(cur.getInt(cur.getColumnIndex(Video.Media._ID)));
            String title = cur.getString(cur.getColumnIndex(Video.Media.TITLE));
            String creator = cur.getString(cur.getColumnIndex(Video.Media.ARTIST));
            String mimeType = cur.getString(cur.getColumnIndex(Video.Media.MIME_TYPE));
            long size = cur.getLong(cur.getColumnIndex(Video.Media.SIZE));
            long duration = cur.getLong(cur.getColumnIndex(Video.Media.DURATION));
            String durationStr = ModelUtil.toTimeString(duration / 1000);
            String resolution = cur.getString(cur.getColumnIndex(Video.Media.RESOLUTION));
            String data = cur.getString(cur.getColumnIndexOrThrow(Audio.Media.DATA));
            String fileName = data.substring(data.lastIndexOf(File.separator));
            String ext = fileName.substring(fileName.lastIndexOf("."));
            data = data.replace(fileName, File.separator + id + ext);
            String url = serverURL + "video" + data;
            Res res = new Res(mimeType, size, durationStr, null, url);
            res.setDuration(ModelUtil.toTimeString(duration / 1000));
            res.setResolution(resolution);
            Movie movie = new Movie(id, VItem.VIDEO_ID, title, creator, res);
            items.add(movie);
        }
        return items;
    }
}
