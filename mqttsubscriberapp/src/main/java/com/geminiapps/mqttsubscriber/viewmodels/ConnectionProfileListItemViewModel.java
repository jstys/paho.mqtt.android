package com.geminiapps.mqttsubscriber.viewmodels;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.geminiapps.mqttsubscriber.broadcast.MqttServiceSender;
import com.geminiapps.mqttsubscriber.models.MqttConnectionProfileModel;

/**
 * Created by jim.stys on 10/1/16.
 */

public class ConnectionProfileListItemViewModel {
    private MqttServiceSender serviceSender;

    public ConnectionProfileListItemViewModel(Context context){
        this.serviceSender = new MqttServiceSender(context);
    }

    public boolean profileLongClicked(MqttConnectionProfileModel model)
    {
        return true;
    }

    public void profileClicked(MqttConnectionProfileModel model)
    {
        this.serviceSender.connectToBroker(model);
    }
}
