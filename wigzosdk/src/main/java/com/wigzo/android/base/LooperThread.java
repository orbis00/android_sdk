package com.wigzo.android.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class LooperThread extends Thread {
    public static Handler mHandler;

    public void run() {
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                Task task = (Task) msg.getData().getSerializable("task");
                task.takeAction();
            }
        };
        Looper.loop();
    }

    public static void sendMessage(Task task) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putSerializable("task", task);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
}