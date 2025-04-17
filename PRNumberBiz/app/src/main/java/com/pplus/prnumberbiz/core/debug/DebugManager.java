package com.pplus.prnumberbiz.core.debug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.pple.pplus.utils.part.logs.LogUtil;

/**
 * @value mService : Passed in Client Messenger
 */
public class DebugManager implements DebugConstant{

    private static final String TAG = DebugManager.class.getSimpleName();
    private static DebugManager instance;
    private static Context mContext;

    final String SERVICE_ACTION = "kr.co.j2n.pplusutil.DebugService";
    final String SERVICE_PACKAGE = "kr.co.j2n.pplusutil";

    private Messenger mService = null;
    private boolean mBound;
    private boolean stop;

    private static Messenger appMessenger;
    private Bundle mBundle;
    private int mType;

    public onServiceConnectInterface mOnServiceConnectInterface;

    public interface onServiceConnectInterface{
        void onServiceConnect();
    }

    public DebugManager() {
    }

    public static DebugManager with(Context context, onServiceConnectInterface serviceConnectInterface) {
        mContext = context;
        LogUtil.d(TAG, "instance = " + instance);
        if (instance == null) {
            instance = new DebugManager();
        }
        LogUtil.d(TAG, "appMessenger = " + appMessenger);
        if (appMessenger == null) {
            appMessenger = new Messenger(new DebugHandler(mContext, serviceConnectInterface));
        }
        return instance;
    }

    public static void init() {
        instance = null;
        appMessenger = null;
    }

    public DebugManager setBundleData(Bundle bundleData, int type){
        mType = type;
        mBundle = bundleData;
        return instance;
    }

    public DebugManager setServiceConnectInterface(onServiceConnectInterface serviceConnectInterface) {
        mOnServiceConnectInterface = serviceConnectInterface;
        return instance;
    }

    public void startBindService() {
        stop = false;

        Intent intent = new Intent();
        intent.setAction(SERVICE_ACTION);
        intent.setPackage(SERVICE_PACKAGE);
        boolean isBind = mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        LogUtil.d(TAG, "isBind = " + isBind);
    }


    public void stopBindService() {
        stop = true;
        if (mBound) {
            send(SEND_DISCONNET_SERVICE);
            appMessenger = null;
            try {
                mContext.unbindService(mConnection);
                //Toast.makeText(mContext,"Service is Unbind",Toast.LENGTH_SHORT).show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @return mService ==null(Client is Craceful Shutdown) or RemoteException (Client is Receive no state response)
     * @Params what : Request code sent to Client( define AppToAppConstant)
     * @Params bundle : data sent to client (object must implemented parcelable)
     */
    public boolean send(int what, Bundle bundle) {

        if (mBound && mService != null) {
            Message msg = Message.obtain(null, what, 0, 0);
            msg.setData(bundle);
            msg.replyTo = appMessenger;
            try {
                mService.send(msg);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

    }

    public boolean send(int what) {

        if (mBound && mService != null) {
            Message msg = Message.obtain(null, what, 0, 0);
            msg.replyTo = appMessenger;
            try {
                mService.send(msg);
                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }


    }

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            mBound = true;
            if(mBundle != null) {
                send(mType, mBundle);
                mBundle = null;
            }
            else {
                send(SEND_CONNECT_SERVICE);
            }

            //Toast.makeText(mContext,"Service is connect",Toast.LENGTH_SHORT).show();

            if(mOnServiceConnectInterface != null) {
                mOnServiceConnectInterface.onServiceConnect();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            if (!stop) {
                startBindService();
            }
            mService = null;
            mBound = false;


        }
    };


}
