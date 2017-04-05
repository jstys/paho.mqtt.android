package com.geminiapps.mqttsubscriber.viewmodels;

import android.app.Dialog;
import android.widget.Toast;

import com.geminiapps.mqttsubscriber.models.MqttSubscriptionModel;
import com.geminiapps.mqttsubscriber.views.AddEditSubscriptionFragment;

public class AddEditSubscriptionViewModel {
    private Dialog dialog;
    private AddEditSubscriptionFragment mView;
    private AddEditSubscriptionFragment.ISubscriptionAddedListener subscriptionAddedListener;

    public AddEditSubscriptionViewModel(Dialog dialog, AddEditSubscriptionFragment.ISubscriptionAddedListener subscriptionAddedListener, AddEditSubscriptionFragment view)
    {
        this.dialog = dialog;
        this.subscriptionAddedListener = subscriptionAddedListener;
        mView = view;
    }

    public void saveSubscription(MqttSubscriptionModel model) {
        StringBuilder errorStringBuilder = new StringBuilder();
        if (model != null){
            if(validateMQTTTopic(model.getTopic(), errorStringBuilder)){
                model.setQos(mView.getSelectedQos());
                this.subscriptionAddedListener.onSubscriptionAdded(model);
                this.dialog.dismiss();
            }
            else{
                Toast.makeText(mView.getContext(), errorStringBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void cancelSubscription() {
        this.dialog.dismiss();
    }

    private boolean validateMQTTTopic(String topic, StringBuilder errorString){
        if(topic.isEmpty()){
            errorString.append("Topic cannot be empty");
            return false;
        }
        else if(topic.length() > 65535){
            errorString.append("Topic must be less than 65536 characters");
            return false;
        }

        int hashCount = 0;
        boolean isValid = true;
        String[] topicLevels = topic.split("/");

        for(String topicLevel : topicLevels){
            if(topicLevel.contains("+") && !topicLevel.equals("+")){
                errorString.append("Topic Level ").append(topicLevel).append(" contains an illegal '+'\n");
                isValid = false;
            }
            else if(topicLevel.contains("#") && !topicLevel.equals("#")){
                errorString.append("Topic Level ").append(topicLevel).append(" contains an illegal '#'\n");
                isValid = false;
            }
            else if(topicLevel.equals("#")){
                hashCount++;
                if(!topic.endsWith("#")){
                    errorString.append("Topic filter using '#' must end with '#'\n");
                    isValid = false;
                }
                if(hashCount == 2){
                    errorString.append("Topic filter can only contain 1 '#' at most\n");
                    isValid = false;
                }
            }
        }

        return isValid;
    }
}
