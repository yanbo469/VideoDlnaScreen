package com.yanbo.lib_screen.utils;



import com.yanbo.lib_screen.entity.RemoteItem;

import org.fourthline.cling.support.model.ProtocolInfo;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.VideoItem;
import org.seamless.util.MimeType;

/**
 * Created by lzan13 on 2018/3/27.
 * Cling 相关工具类
 */
public class ClingUtil {

    public static String DIDL_LITE_HEADER = "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:sec=\"http://www.sec.co.kr/\">";
    public static String DIDL_LITE_FOOTER = "</DIDL-Lite>";

    /**
     * 获取 Item 资源的 metadata
     */
    public static String getItemMetadata(RemoteItem remoteItem) {
        Res itemRes = new Res(new MimeType(ProtocolInfo.WILDCARD, ProtocolInfo.WILDCARD), remoteItem
                .getSize(), remoteItem.getUrl());
        itemRes.setDuration(remoteItem.getDuration());
        itemRes.setResolution(remoteItem.getResolution());
        VideoItem item = new VideoItem(remoteItem.getId(), "0", remoteItem.getTitle(), remoteItem.getCreator(), itemRes);

        StringBuilder metadata = new StringBuilder();
        metadata.append(DIDL_LITE_HEADER);

        metadata.append(String.format("<tem id=\"%s\" parentID=\"%s\" restricted=\"%s\">", item.getId(), item
                .getParentID(), item.isRestricted() ? "1" : "0"));

        metadata.append(String.format("<dc:title>%s</dc:title>", item.getTitle()));
        String creator = item.getCreator();
        if (creator != null) {
            creator = creator.replaceAll("<", "_");
            creator = creator.replaceAll(">", "_");
        }
        metadata.append(String.format("<upnp:class>%s</upnp:class>", item.getClazz().getValue()));
        metadata.append(String.format("<upnp:artist>%s</upnp:artist>", creator));

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        Date now = new Date();
//        String time = sdf.format(now);
//        metadata.append(String.format("<dc:date>%s</dc:date>", time));

        // metadata.append(String.format("<upnp:album>%s</upnp:album>",
        // localItem.get);

        // <res protocolInfo="http-get:*:audio/mpeg:*"
        // resolution="640x478">http://192.168.1.104:8088/Music/07.我醒著做夢.mp3</res>

        Res res = item.getFirstResource();
        if (res != null) {
            // protocol info
            String protocolInfo = "";
            ProtocolInfo pi = res.getProtocolInfo();
            if (pi != null) {
                protocolInfo = String.format("protocolInfo=\"%s:%s:%s:%s\"", pi.getProtocol(), pi.getNetwork(), pi
                        .getContentFormatMimeType(), pi.getAdditionalInfo());
            }
            LogUtils.i("protocolInfo: " ,protocolInfo);

            // resolution, extra info, not adding yet
            String resolution = "";
            if (res.getResolution() != null && res.getResolution().length() > 0) {
                resolution = String.format("resolution=\"%s\"", res.getResolution());
            }

            // duration
            String duration = "";
            if (res.getDuration() != null && res.getDuration().length() > 0) {
                duration = String.format("duration=\"%s\"", res.getDuration());
            }

            // res begin
            // metadata.append(String.format("<res %s>", protocolInfo)); // no resolution & duration yet
            metadata.append(String.format("<res %s %s %s>", protocolInfo, resolution, duration));

            // url
            String url = res.getValue();
            metadata.append(url);

            // res end
            metadata.append("</res>");
        }
        metadata.append("</item>");
        metadata.append(DIDL_LITE_FOOTER);
        return metadata.toString();
    }

}
