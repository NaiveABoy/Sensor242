package com.jingge.sensorcollect;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SensorService extends Service {
    public SensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}