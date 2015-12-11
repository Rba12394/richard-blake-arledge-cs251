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
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class MainActivity extends Activity {
    // @@ https://google.github.io/styleguide/javaguide.html#s4.6.1-vertical-whitespace
    private EditText mEdit;
    private String mUrl;
    static final String MY_URL = "url";
    public static final String DOWNLOADMETHOD = "DownloadMethod";
    private final String URL = "url";
    private Bundle mResponseBundle;
    private String mSentUrl;
    private MyResponseReceiver receiver;
    myThread myThread;

    // Looper/thread https://www.youtube.com/watch?v=wYDJH6tDyNg

    // @@ Why make this static?
    public static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit = (EditText) findViewById(R.id.urlTextEdit);

        IntentFilter filter = new IntentFilter(MyResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MyResponseReceiver();
        registerReceiver(receiver, filter);


    }

    public void onDownloadServiceClicked(View view) {
        // @@ Why start an Activity and then decide which download method to use?
        // @@ Don't start a new Activity for this

        mSentUrl = mUrl;

        Intent msgIntent = new Intent(this, ImageIntentService.class);
        msgIntent.putExtra(URL, mSentUrl);
        Log.i("MainActivity", "call made to ImageIntentService");

        int permissionCheck = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            this.requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        startService(msgIntent);

    }

    public void onDownloadThreadClicked(View view) {
        // @@ Why start an Activity and then decide which download method to use?
        // @@ Don't start a new Activity for this

        myThread = new myThread();
        myThread.start();

        sendMessage();

        defineHandler();
    }


    public void sendMessage(){
        mSentUrl = mUrl;
        final DownloadImageHandler handlerObj = new DownloadImageHandler();
        myThread.handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("MainActivity", "new thread launched/inside runnable");
                Message msg = handlerObj.obtainMessage();
                //entered URI is added to the message
                msg.obj = Uri.parse(mSentUrl);
                Log.i("MainActivity/run", msg.obj.toString());
                final Messenger messenger = new Messenger(MainActivity.mHandler);
                msg.replyTo = messenger;
                loadExtras(msg);
                handlerObj.sendMessage(msg);
            }
        });
    }


    private void loadExtras(Message msg){
        msg.setData(mResponseBundle);
    }


    private void defineHandler(){
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message inputMessage){
                Log.i("MainActivity", "handleMessage() called");
                Intent updateIntent = new Intent();
                Bundle bundle = inputMessage.getData();
                String time = bundle.getString("time");
                String url = bundle.getString(MY_URL);
                updateIntent.putExtra("time", time);
                updateIntent.putExtra(MY_URL, url);
                display(updateIntent);
            }
        };
    }

    public void display(Intent data){
        final String returnedUrl = data.getStringExtra(MY_URL);
        String elapsed = data.getStringExtra("time");
        LinearLayout newL = new LinearLayout(this);
        TableRow newRow = new TableRow(this);
        Log.i("MainActivity", elapsed + "sec");

        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        newRow.setLayoutParams(lp);
        TextView imageName = new TextView(this);
        TextView downloadTime = new TextView(this);
        Button showBtn = new Button(this);
        showBtn.setOnClickListener(new View.OnClickListener() {

            final String pathtofile = "file://" + returnedUrl;

            @Override
            public void onClick(View v) {
                Log.i("MainActivity-showbtn", pathtofile);
                Intent galleryIntent = new Intent(Intent.ACTION_VIEW);
                galleryIntent.setDataAndType(Uri.parse(pathtofile), "image/*");
                startActivity(galleryIntent);
            }
        });

        imageName.setWidth(360);
        downloadTime.setWidth(360);
        showBtn.setText("SHOW");
        downloadTime.setText(Long.parseLong(elapsed) / 1000.0 + " sec");

        try {
            URI uri1 = new URI(returnedUrl);
            String[] segments = uri1.getPath().split("/");
            String idStr = segments[segments.length - 1];
            imageName.setText(idStr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        newL.addView(imageName);
        newL.addView(downloadTime);
        newL.addView(showBtn);
        newL.addView(newRow);
        TableLayout l1 = (TableLayout) findViewById(R.id.tablelayout1);
        l1.addView(newL);
    }


    public String getUrl(EditText v) {
        mUrl = v.getText().toString();
        return mUrl;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity", "onStart() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity", "onRestart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity", "onResume() called");

        Intent i = getIntent();
        Boolean ret = i.getBooleanExtra("returned",false);

        if(ret){
            Log.i("MainActivity", "ret = true");
            display(i);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        Log.i("MainActivity", "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity", "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


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

    public class MyResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "cs251.vandy.imagegrabberservice.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent){
            Log.i("MainActivity", "onReceive() called");
            //pulls data from bundle
            Bundle bundle = intent.getExtras();
            String elapsed = bundle.getString("time");
            String address = bundle.getString("url");

            //sends data in a new intent to mainactivity for UI to be updated
            Intent mainIntent = new Intent(MainActivity.this, ImageIntentService.class);
            mainIntent.putExtra("time", elapsed);
            mainIntent.putExtra("returned", true);
            mainIntent.putExtra(URL, address);

            // @@ This is a very roundabout way of doing this
            // @@ Why are you starting MainActivity here? Just receive the broadcast there
            startActivity(mainIntent);
            finish();
        }
    }

    class myThread extends Thread{
        Handler handler;

        public myThread(){

        }

        @Override
        public void run(){
            Looper.prepare();
            handler = new DownloadImageHandler();
            Looper.loop();
        }
    }

}


