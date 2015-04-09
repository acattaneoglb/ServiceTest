package com.globant.cattaneo.ariel.servicetest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by ariel.cattaneo on 08/04/2015.
 */
public class ServiceManager {
    private Context mContext;

    private LocalService mBoundService;
    private boolean mIsBound = false;

    DataServiceAvailableListener mListener = null;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doUnbindService();
        }
    };

    public ServiceManager(Context context) {
        this.mContext = context;

        mContext.startService(new Intent(mContext, LocalService.class));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((LocalService.LocalBinder)service).getService();

            // Tell the user about this for our demo.
            Toast.makeText(mContext, R.string.local_service_connected, Toast.LENGTH_SHORT).show();

            // We can unbind now. Free the service to make it able to self destruct.
            // And see what happens...
            //doUnbindService();

            if (mListener != null) {
                mListener.serviceAvailable(mBoundService);

                mListener = null;

                doUnbindService();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
            Toast.makeText(mContext, R.string.local_service_disconnected, Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        mContext.startService(new Intent(mContext, LocalService.class));

        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        mContext.bindService(new Intent(mContext, LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsBound = false;
        }
    }

    private void startUnbindCountdown() {
        handler.postDelayed(runnable, 10000);
    }

    public LocalService getDataService() {
        mContext.startService(new Intent(mContext, LocalService.class));

        return mBoundService;
    }

    public void requestDataService(DataServiceAvailableListener listener) {
        mListener = listener;

        doBindService();

        //startUnbindCountdown();
    }
}
