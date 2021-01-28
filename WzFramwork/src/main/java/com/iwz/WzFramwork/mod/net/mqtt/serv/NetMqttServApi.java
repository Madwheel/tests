package com.iwz.WzFramwork.mod.net.mqtt.serv;


import com.alibaba.fastjson.JSONObject;
import com.iwz.WzFramwork.base.CommonRes;
import com.iwz.WzFramwork.base.api.ServApi;
import com.iwz.WzFramwork.mod.net.mqtt.NetMqttMain;
import com.iwz.WzFramwork.mod.net.mqtt.model.IMqttResCallback;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class NetMqttServApi extends ServApi {
    private static NetMqttServApi instance = null;
    private NetMqttMain mMain;

    protected NetMqttServApi(NetMqttMain main) {
        super(main);
        mMain = main;
    }

    public static NetMqttServApi getInstance(NetMqttMain main) {
        if (instance == null) {
            instance = new NetMqttServApi(main);
        }
        return instance;
    }

    private MqttClient mClient;
    private MqttFact mMqtt;
    private boolean mWorkingFlag;

    public <T> void startWork(String host, int port, final String token, String[] topics, String clientId, final IMqttResCallback<T> callback) {
        if (mWorkingFlag) {
            return;
        }
        if (mMqtt != null) {
            return;
        }
        mMqtt = new MqttFact(host, port, token, topics, clientId);
        mClient = mMqtt.createClient();
        if (mClient == null) {
            callback.onFinish(new CommonRes<T>(false));
            return;
        }
        mClient.setCallback(new MqttCallbackExtended() {
            public void connectComplete(boolean reconnect, String serverURI) {//连接成功
                try {
                    JSONObject reqObj = new JSONObject();
                    reqObj.put("token", token);//设置 Token 内容
                    reqObj.put("type", "R");//设置 Token 类型,共有 RW，R，W 三种类型
                    MqttMessage message = new MqttMessage(reqObj.toString().getBytes());
                    message.setQos(1);
                    MqttTopic pubTopic = NetMqttServApi.this.mClient.getTopic("$SYS/uploadToken");
                    MqttDeliveryToken mqttDeliveryToken = pubTopic.publish(message);
                    mqttDeliveryToken.waitForCompletion();//同步等待上传结果
                    //Token 上传成功，在开始正常的业务消息收发
                    int qos[] = {1, 1};
                    NetMqttServApi.this.mClient.subscribe(NetMqttServApi.this.mMqtt.getTopics(), qos);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            public void connectionLost(Throwable throwable) {
                throwable.printStackTrace();
            }

            public void messageArrived(String topic, MqttMessage mqttMessage) {//接收到的数据
                if (mqttMessage != null) {
                    callback.onArrive(topic, mqttMessage.toString());
                }
            }

            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                //(数据传送)如果与mqtt建立链接成功并发送token成功，则回调该接口 iMqttDeliveryToken.getMessageId()为上传的id
            }
        });
        try {
            mClient.connect(mMqtt.pConnOpts);
        } catch (MqttException e) {
            terminateWork();
            callback.onFinish(new CommonRes<T>(false));
        }
        mWorkingFlag = true;
    }

    public void terminateWork() {
        if (mClient != null) {
            try {
                mClient.close();
            } catch (MqttException e) {
                d(e.toString());
            }
        }
        mClient = null;
        mMqtt = null;
        mWorkingFlag = false;
    }
}

class MqttFact {
    private String mHost;
    private int mPort;
    private String mToken;
    private String[] mTopics;
    private String mClientId;

    private MqttClient mClient;
    public MqttConnectOptions pConnOpts;

    public String[] getTopics() {
        return mTopics;
    }

    public MqttFact(String host, int port, String token, String[] topics, String clientId) {
        mHost = host;
        mPort = port;
        mToken = token;
        mTopics = topics;
        mClientId = clientId;
    }

    public MqttClient createClient() {
        String url = "tcp://" + mHost + ":" + mPort;
        final int[] qos = {1, 1};
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            mClient = new MqttClient(url, mClientId, persistence);
        } catch (MqttException e) {
            return null;
        }
        pConnOpts = new MqttConnectOptions();
        pConnOpts.setServerURIs(new String[]{url});
        pConnOpts.setCleanSession(false);//服务器端清除客户端session
        pConnOpts.setKeepAliveInterval(90);// 心跳包发送间隔，单位：秒
        pConnOpts.setAutomaticReconnect(true);//设置自动重连

        return mClient;
    }
}
