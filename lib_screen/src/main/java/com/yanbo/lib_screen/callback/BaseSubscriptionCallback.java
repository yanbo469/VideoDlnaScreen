package com.yanbo.lib_screen.callback;


import com.yanbo.lib_screen.utils.LogUtils;

import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.state.StateVariableValue;
import org.fourthline.cling.support.lastchange.Event;
import org.fourthline.cling.support.lastchange.EventedValue;
import org.fourthline.cling.support.lastchange.InstanceID;
import org.fourthline.cling.support.lastchange.LastChangeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lzan13 on 2018/3/9.
 */
public abstract class BaseSubscriptionCallback extends SubscriptionCallback {

    // 订阅持续时间 秒，这里设置3小时
    private static final int SUB_DURATION = 60 * 60 * 3;

    protected BaseSubscriptionCallback(Service service) {
        this(service, SUB_DURATION);
    }

    protected BaseSubscriptionCallback(Service service, int requestedDurationSeconds) {
        super(service, requestedDurationSeconds);
    }

    @Override
    protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
        LogUtils.d("","SubscriptionCallback failed");
    }

    @Override
    protected void ended(GENASubscription subscription, CancelReason reason, UpnpResponse responseStatus) {
        LogUtils.d("","SubscriptionCallback ended");
    }

    @Override
    protected void established(GENASubscription subscription) {}

    @Override
    protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {}

    @Override
    protected void eventReceived(GENASubscription subscription) {
        Map<String, StateVariableValue> values = subscription.getCurrentValues();
        if (values != null && values.containsKey("LastChange")) {
            LastChangeParser parser = getLastChangeParser();
            String lastChangeValue = values.get("LastChange").toString();
            LogUtils.d("Last change value: %s", lastChangeValue);
            try {
                Event event = parser.parse(lastChangeValue);
                List<InstanceID> ids = event.getInstanceIDs();
                List<EventedValue> eventValues = new ArrayList<>();
                for (InstanceID id : ids) {
                    eventValues.addAll(id.getValues());
                }
                onReceived(eventValues);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    protected abstract LastChangeParser getLastChangeParser();

    protected abstract void onReceived(List<EventedValue> values);

}
