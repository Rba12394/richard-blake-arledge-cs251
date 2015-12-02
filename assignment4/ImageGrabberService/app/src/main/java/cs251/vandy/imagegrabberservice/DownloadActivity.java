package cs251.vandy.imagegrabberservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.util.Date;

public class DownloadActivity extends Activity {
    private final String URL = "url";
    private String mMethod;
    private MyResponseReceiver receiver;
    private Bundle mResponseBundle;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Intent mIntent = this.getIntent();
        mMethod = mIntent.getStringExtra("DownloadMethod");
        String sentUrl = mIntent.getStringExtra(URL);

        // @@ Why not have the BroadcastReceiver in MainActivity instead?
        IntentFilter filter = new IntentFilter(MyResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyResponseReceiver();
        registerReceiver(receiver, filter);

        Log.i("DownloadActivity", mMethod);

        // @@ See previous comments in MainActivity. Basically, you can
        // @@ remove the DownloadActivity class and decide which downloading method
        // @@ to use based on which button was clicked rather than passing a string parameter
        sendToDownloader(mMethod, sentUrl);
    }

    private void sendToDownloader(String str, final String sentUrl){
        //this means that the "DOWNLOAD SERVICE" button was pressed
        if(str.equals("service")){
            Intent msgIntent = new Intent(this, ImageIntentService.class);
            msgIntent.putExtra(URL, sentUrl);
            Log.i("DownloadActivity", "call made to ImageIntentService");

            int permissionCheck = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_DENIED)
                this.requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            startService(msgIntent);
        }

        //this means that the "DOWNLOAD THREAD" button was pressed
        if(str.equals("thread")){
            final DownloadImageHandler handlerObj = new DownloadImageHandler();

            // @@ This is also a very roundabout way to do things
            // @@ Instead of this, try creating one background thread (in MainActivity) that has
            // @@ a Looper, associate an instance of DownloadImageHandler with that Looper, and then
            // @@ send messages to that instance of DownloadImageHandler when you want to download
            // @@ using a Thread.
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i("DownloadActivity", "new thread launched/inside runnable");
                    Message msg = handlerObj.obtainMessage();
                    //entered URI is added to the message
                    msg.obj = Uri.parse(sentUrl);
                    Log.i("DownloadActivity/run", msg.obj.toString());
                    final Messenger messenger = new Messenger(MainActivity.mHandler);
                    msg.replyTo = messenger;
                    loadExtras(msg);
                    handlerObj.sendMessage(msg);
                }
            })).start();

        }
       // else{
       //     throw new InvalidParameterException("no identifier of which method to use");
       // }
    }

    private void loadExtras(Message msg){
        msg.setData(mResponseBundle);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("DownloadActivity", "onStart() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("DownloadActivity", "onRestart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("DownloadActivity", "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("DownloadActivity", "onPause() called");
        //used for ImageIntentService
        if(mMethod.equals("service")){
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("DownloadActivity", "onStop() called");
    }

    //this deals with the broadcast sent by the intent service and pulls the appropriate data
    public class MyResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "cs251.vandy.imagegrabberservice.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent){
            Log.i("DownloadActivity", "onReceive() called");
            //pulls data from bundle
            Bundle bundle = intent.getExtras();
            String elapsed = bundle.getString("time");
            String address = bundle.getString("url");

            //sends data in a new intent to mainactivity for UI to be updated
            Intent mainIntent = new Intent(DownloadActivity.this,MainActivity.class);
            mainIntent.putExtra("time", elapsed);
            mainIntent.putExtra("returned", true);
            mainIntent.putExtra(URL, address);

            // @@ This is a very roundabout way of doing this
            // @@ Why are you starting MainActivity here? Just receive the broadcast there
            startActivity(mainIntent);
            finish();
        }
    }


    //DownloadImageHandler class, custom Handler object that performs certain operations on
    class DownloadImageHandler extends Handler {
        private String mUrl = URL;
        private long mStartTime,mEndTime;
        public Handler handler;

        public DownloadImageHandler(){
            handler = new Handler();
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i("DownloadImageHandler", "handleMessage() called");
            mUrl = msg.obj.toString();
            Date date = new Date();
            mStartTime = date.getTime();
            Log.i("DownloadImgHandler:url", mUrl);
            Uri returned = DownloadUtils.downloadImage(getApplicationContext(), Uri.parse(mUrl));
            Log.i("DownloadImgHandler:uri", returned.toString());
            Date date2 = new Date();
            mEndTime = date2.getTime();
            long calcTime = mEndTime - mStartTime;
            mResponseBundle = new Bundle();
            String time = String.valueOf(calcTime);
            mResponseBundle.putString("time", time);
            mResponseBundle.putString(URL , returned.toString());
        }
    }
}



