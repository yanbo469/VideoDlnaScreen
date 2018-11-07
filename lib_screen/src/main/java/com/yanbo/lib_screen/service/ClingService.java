package com.yanbo.lib_screen.service;

import android.content.Intent;
import android.os.IBinder;

import com.yanbo.lib_screen.service.upnp.AndroidJettyServletContainer;
import com.yanbo.lib_screen.service.upnp.ClingContentDirectoryService;
import com.yanbo.lib_screen.utils.LogUtils;
import com.yanbo.lib_screen.utils.VMNetwork;

import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.transport.impl.AsyncServletStreamServerConfigurationImpl;
import org.fourthline.cling.transport.impl.AsyncServletStreamServerImpl;
import org.fourthline.cling.transport.spi.NetworkAddressFactory;
import org.fourthline.cling.transport.spi.StreamServer;

import java.util.UUID;

/**
 * Created by lzan13 on 2018/3/1.
 * Cling service，获取 UPnP 相关对象
 */
public class ClingService extends AndroidUpnpServiceImpl {

    private final String TAG = this.getClass().getSimpleName();

    private LocalDevice localDevice = null;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new LocalBinder();
        initLocalDevice();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    private void initLocalDevice() {
        //Create LocalDevice
        LocalService localService = new AnnotationLocalServiceBinder().read(
                ClingContentDirectoryService.class);
        localService.setManager(
                new DefaultServiceManager<>(localService, ClingContentDirectoryService.class));

        String macAddress = VMNetwork.getMacAddress();
        //Generate UUID by MAC address
        UDN udn = UDN.valueOf(UUID.nameUUIDFromBytes(macAddress.getBytes()).toString());

        UDADeviceType type = new UDADeviceType("MediaServer");
        // DeviceDetails details = new DeviceDetails(localDeviceName);
        DeviceDetails details = new DeviceDetails("VAndroidMediaServer");
        try {
            localDevice = new LocalDevice(new DeviceIdentity(udn), type, details,
                    new LocalService[]{localService});
            upnpService.getRegistry().addDevice(localDevice);
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "MediaServer device created! name:%s, manufacturer:%s, model:%s"+"      "+
                details.getFriendlyName()+"      "+ details.getManufacturerDetails().getManufacturer()+"      "+
                details.getModelDetails().getModelName());
    }

    public LocalDevice getLocalDevice() {
        return localDevice;
    }

    /**
     * 获取控制点
     */
    public ControlPoint getControlPoint() {
        return upnpService.getControlPoint();
    }

    /**
     * 获取 UPnP 核心注册组件
     */
    public Registry getRegistry() {
        return upnpService.getRegistry();
    }

    @Override
    protected UpnpServiceConfiguration createConfiguration() {
        return new FixedAndroidUpnpServiceConfiguration();
    }

    public UpnpServiceConfiguration getConfiguration() {
        return upnpService.getConfiguration();
    }

    class FixedAndroidUpnpServiceConfiguration extends AndroidUpnpServiceConfiguration {
        @Override
        public StreamServer createStreamServer(NetworkAddressFactory networkAddressFactory) {
            // Use Jetty, start/stop a new shared instance of JettyServletContainer
            return new AsyncServletStreamServerImpl(new AsyncServletStreamServerConfigurationImpl(
                    AndroidJettyServletContainer.INSTANCE,
                    networkAddressFactory.getStreamListenPort()));
        }
    }

    public class LocalBinder extends Binder {
        public ClingService getService() {
            return ClingService.this;
        }
    }
}
