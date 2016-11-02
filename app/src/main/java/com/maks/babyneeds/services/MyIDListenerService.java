package com.maks.babyneeds.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyIDListenerService extends InstanceIDListenerService {
    public MyIDListenerService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
