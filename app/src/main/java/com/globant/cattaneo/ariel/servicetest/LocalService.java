package com.globant.cattaneo.ariel.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by ariel.cattaneo on 07/04/2015.
 */
public class LocalService extends Service {

    private final static int TIMEOUT_SECONDS = 30;

    private DatabaseWrapper mWrapper;

    Handler mHandler = new Handler();
    Runnable mRunnable;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    private void startSelfDestruct() {
        mHandler.postDelayed(mRunnable, 1000 * TIMEOUT_SECONDS);
        Log.d("Test app", "Self-destruction countdown started");
    }

    private void stopSelfDestruct() {
        mHandler.removeCallbacks(mRunnable);
        Log.d("Test app", "Self-destruction countdown stopped");
    }

    private void restartSelfDestruct() {
        stopSelfDestruct();
        startSelfDestruct();
    }

    protected DatabaseWrapper useWrapper() {
        return mWrapper;
    }

    @Override
    public void onCreate() {
        Log.d("Test app", "Service created");

        // TODO: Init the stuff which manages the database?
        mWrapper = new ParseWrapper();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("Test app", "Self-destructioning");
                stopSelf();
            }
        };

        // TODO: Do needed stuff
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        stopSelfDestruct();
        startSelfDestruct();

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Log.d("Test app", "The service has been destroyed. :(");
    }

    @Override
    public IBinder onBind(Intent intent) {
        stopSelfDestruct();
        Log.d("Test app", "Binded");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean result = super.onUnbind(intent);

        Log.d("Test app", "Unbinded");

        startSelfDestruct();

        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        stopSelfDestruct();

        Log.d("Test app", "Rebinded");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.d("Test app", "Task removed");
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    //---------------------------------------------------------------------------------
    // Methods for the client

    public Event getEvent() {
        return (Event) useWrapper().getObjectFromTable("event");
    }
}