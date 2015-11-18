package cs251.vandy.imagegrabberservice;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.Date;

import cs251.vandy.imagegrabberservice.DownloadActivity.MyResponseReceiver;

public class ImageIntentService extends IntentService{

    private long mStartTime;
    private long mEndTime;

    public ImageIntentService(){
        super(ImageIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Date date = new Date();
        mStartTime = date.getTime();
        Log.i("ImageIntentService", "onHandleIntent() called");
        String url = intent.getStringExtra("url");
        Log.i("ImageIntentService", "url string: " + url);
        Uri uri = Uri.parse(url);
        //SystemClock.sleep(15000); //15 seconds

        Uri returned = DownloadUtils.downloadImage(this.getApplicationContext(), uri);

        Date date2 = new Date();
        mEndTime = date2.getTime();
        long calcTime = mEndTime - mStartTime;

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MyResponseReceiver.ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra("url", returned.toString());

        String time = String.valueOf(calcTime);
        broadcastIntent.putExtra("time", time);

        Log.i("ImageIntentService", "calcTime: " + time);
        sendBroadcast(broadcastIntent);

    }

}
