package com.yanbo.lib_screen.service.upnp;


import com.yanbo.lib_screen.VApplication;
import com.yanbo.lib_screen.database.MediaContentDao;
import com.yanbo.lib_screen.entity.VItem;

import org.fourthline.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryException;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.util.List;

/**
 * Created by lzan13 on 2018/3/18.
 * 本地设备内容目录服务，主要用来读取本地音频和视频文件
 */
public class ClingContentDirectoryService extends AbstractContentDirectoryService {

    @Override
    public BrowseResult browse(String objectID, BrowseFlag browseFlag, String filter, long firstResult, long maxResults, SortCriterion[] orderBy) throws ContentDirectoryException {
        Container resultBean = ContainerFactory.createContainer(objectID);
        DIDLContent content = new DIDLContent();
        for (Container c : resultBean.getContainers()) {
            content.addContainer(c);
        }
        for (Item item : resultBean.getItems()) {
            content.addItem(item);
        }
        int count = resultBean.getChildCount();
        String contentModel = "";
        try {
            contentModel = new DIDLParser().generate(content);
        } catch (Exception e) {
            throw new ContentDirectoryException(ContentDirectoryErrorCode.CANNOT_PROCESS, e.toString());
        }
        return new BrowseResult(contentModel, count, count);
    }

    static class ContainerFactory {
        static Container createContainer(String containerId) {
            Container result = new Container();
            result.setChildCount(0);

            if (VItem.ROOT_ID.equals(containerId)) {
                // 定义音频资源
                Container audioContainer = new Container();
                audioContainer.setId(VItem.AUDIO_ID);
                audioContainer.setParentID(VItem.ROOT_ID);
                audioContainer.setClazz(VItem.AUDIO_CLASS);
                audioContainer.setTitle("Audios");

                result.addContainer(audioContainer);
                result.setChildCount(result.getChildCount() + 1);

                // 定义图片资源
                Container imageContainer = new Container();
                imageContainer.setId(VItem.IMAGE_ID);
                imageContainer.setParentID(VItem.ROOT_ID);
                imageContainer.setClazz(VItem.IMAGE_CLASS);
                imageContainer.setTitle("Images");

                result.addContainer(imageContainer);
                result.setChildCount(result.getChildCount() + 1);

                // 定义视频资源
                Container videoContainer = new Container();
                videoContainer.setId(VItem.VIDEO_ID);
                videoContainer.setParentID(VItem.ROOT_ID);
                videoContainer.setClazz(VItem.VIDEO_CLASS);
                videoContainer.setTitle("Videos");

                result.addContainer(videoContainer);
                result.setChildCount(result.getChildCount() + 1);
            } else if (VItem.AUDIO_ID.equals(containerId)) {
                MediaContentDao contentDao = new MediaContentDao(VApplication.getContext());
                //Get audio items
                List<Item> items = contentDao.getAudioItems();
                for (Item item : items) {
                    result.addItem(item);
                    result.setChildCount(result.getChildCount() + 1);
                }
            } else if (VItem.IMAGE_ID.equals(containerId)) {
                MediaContentDao contentDao = new MediaContentDao(VApplication.getContext());
                //Get image items
                List<Item> items = contentDao.getImageItems();
                for (Item item : items) {
                    result.addItem(item);
                    result.setChildCount(result.getChildCount() + 1);
                }
            } else if (VItem.VIDEO_ID.equals(containerId)) {
                MediaContentDao contentDao = new MediaContentDao(VApplication.getContext());
                //Get video items
                List<Item> items = contentDao.getVideoItems();
                for (Item item : items) {
                    result.addItem(item);
                    result.setChildCount(result.getChildCount() + 1);
                }
            }
            return result;
        }
    }
}
