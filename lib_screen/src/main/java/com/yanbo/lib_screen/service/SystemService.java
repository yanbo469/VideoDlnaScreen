package com.yanbo.lib_screen.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.yanbo.lib_screen.VIntents;
import com.yanbo.lib_screen.manager.ControlManager;
import com.yanbo.lib_screen.service.upnp.JettyResourceServer;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportVariable;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.lastchange.EventedValueString;
import org.fourthline.cling.support.lastchange.LastChange;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.TransportState;
import org.fourthline.cling.support.model.item.Item;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lzan13 on 2018/3/22.
 */
public class SystemService extends Service {
    private static final String TAG = SystemService.class.getSimpleName();

    private Binder binder = new LocalBinder();
    private Device mSelectedDevice;
    private int mDeviceVolume;
    private AVTransportSubscriptionCallback mAVTransportSubscriptionCallback;

    //Jetty DMS Server
    private ExecutorService mThreadPool = Executors.newCachedThreadPool();
    private JettyResourceServer mJettyResourceServer;

    @Override
    public void onCreate() {
        super.onCreate();
        //Start Local Server
        mJettyResourceServer = new JettyResourceServer();
        mThreadPool.execute(mJettyResourceServer);
    }

    @Override
    public void onDestroy() {
        //End all subscriptions
        if (mAVTransportSubscriptionCallback != null)
            mAVTransportSubscriptionCallback.end();

        //Stop Jetty
        mJettyResourceServer.stopIfRunning();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public SystemService getService() {
            return SystemService.this;
        }
    }

    public Device getSelectedDevice() {
        return mSelectedDevice;
    }

    public void setSelectedDevice(Device selectedDevice, ControlPoint controlPoint) {
        if (selectedDevice == mSelectedDevice) return;

        Log.i(TAG, "Change selected device.");
        mSelectedDevice = selectedDevice;
        //End last device's subscriptions
        if (mAVTransportSubscriptionCallback != null) {
            mAVTransportSubscriptionCallback.end();
        }
        UDAServiceType type = new UDAServiceType(ControlManager.AV_TRANSPORT);
        //Init Subscriptions
        mAVTransportSubscriptionCallback = new AVTransportSubscriptionCallback(mSelectedDevice.findService(type));
        controlPoint.execute(mAVTransportSubscriptionCallback);

        Intent intent = new Intent(VIntents.ACTION_CHANGE_DEVICE);
        sendBroadcast(intent);
    }

    public int getDeviceVolume() {
        return mDeviceVolume;
    }

    public void setDeviceVolume(int currentVolume) {
        mDeviceVolume = currentVolume;
    }

    private class AVTransportSubscriptionCallback extends SubscriptionCallback {

        protected AVTransportSubscriptionCallback(org.fourthline.cling.model.meta.Service service) {
            super(service);
        }

        @Override
        protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
            Log.e(TAG, "AVTransportSubscriptionCallback failed.");
        }

        @Override
        protected void established(GENASubscription subscription) {
        }

        @Override
        protected void ended(GENASubscription subscription, CancelReason reason, UpnpResponse responseStatus) {
            Log.i(TAG, "AVTransportSubscriptionCallback ended.");
        }

        @Override
        protected void eventReceived(GENASubscription subscription) {
            Map<String, StateVariableValue> values = subscription.getCurrentValues();
            if (values != null && values.containsKey("LastChange")) {
                String lastChangeValue = values.get("LastChange").toString();
                Log.i(TAG, "LastChange:" + lastChangeValue);
                LastChange lastChange;
                try {
                    lastChange = new LastChange(new AVTransportLastChangeParser(), lastChangeValue);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                //Parse TransportState value.
                AVTransportVariable.TransportState transportState = lastChange.getEventedValue(0, AVTransportVariable.TransportState.class);
                if (transportState != null) {
                    TransportState ts = transportState.getValue();
                    if (ts == TransportState.PLAYING) {
                        Intent intent = new Intent(VIntents.ACTION_PLAYING);
                        sendBroadcast(intent);
                    } else if (ts == TransportState.PAUSED_PLAYBACK) {
                        Intent intent = new Intent(VIntents.ACTION_PAUSED_PLAYBACK);
                        sendBroadcast(intent);
                    } else if (ts == TransportState.STOPPED) {
                        Intent intent = new Intent(VIntents.ACTION_STOPPED);
                        sendBroadcast(intent);
                    }
                }

                //Parse CurrentTrackMetaData value.
                EventedValueString currentTrackMetaData = lastChange.getEventedValue(0, AVTransportVariable.CurrentTrackMetaData.class);
                if (currentTrackMetaData != null && currentTrackMetaData.getValue() != null) {
                    DIDLParser didlParser = new DIDLParser();
                    Intent lastChangeIntent;
                    try {
                        DIDLContent content = didlParser.parse(currentTrackMetaData.getValue());
                        Item item = content.getItems().get(0);
                        String creator = item.getCreator();
                        String title = item.getTitle();

                        lastChangeIntent = new Intent(VIntents.ACTION_UPDATE_LAST_CHANGE);
                        lastChangeIntent.putExtra("creator", creator);
                        lastChangeIntent.putExtra("title", title);
                    } catch (Exception e) {
                        Log.e(TAG, "Parse CurrentTrackMetaData error.");
                        lastChangeIntent = null;
                    }

                    if (lastChangeIntent != null)
                        sendBroadcast(lastChangeIntent);
                }
            }
        }

        @Override
        protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {
        }
    }
}
