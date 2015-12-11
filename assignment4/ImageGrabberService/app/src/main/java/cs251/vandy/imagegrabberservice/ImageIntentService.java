package cs251.vandy.imagegrabberservice;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import cs251.vandy.imagegrabberservice.MainActivity.MyResponseReceiver;

public class ImageIntentService extends IntentService{

    private long mStartTime;
    private long mEndTime;

    public ImageIntentService(){
        super(ImageIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent){
        Log.i("ImageIntentService", "onHandleIntent() called");
        String url = intent.getStringExtra("url");
        Log.i("ImageIntentService", "url string: " + url);
        Uri uri = Uri.parse(url);
        //SystemClock.sleep(15000); //15 seconds

        // @@ Start the time here
        // @@ consider using this method:
        // @@ http://docs.oracle.com/javase/7/docs/api/java/lang/System.html#currentTimeMillis()

        mStartTime = System.currentTimeMillis();

        Uri returned = DownloadUtils.downloadImage(this.getApplicationContext(), uri);

        mEndTime = System.currentTimeMillis();
        long calcTime = mEndTime - mStartTime;

        // @@ You can use a broadcast receiver for communication, but the
        // @@ receiver should probably be in MainActivity instead of DownloadActivity.
        // @@ You don't need the DownloadActivity class here.

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
