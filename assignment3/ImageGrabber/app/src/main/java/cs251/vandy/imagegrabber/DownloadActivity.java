package cs251.vandy.imagegrabber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.Calendar;

public class DownloadActivity extends Activity {
    // @@ Prefix member variables with "m"
private double calcTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        Intent mIntent = getIntent();
        // @@ Use a named constant
        String sentUrl = mIntent.getStringExtra("url");

        // @@ Measure the time in your AsyncTask, not here
        Calendar c = Calendar.getInstance();
        double seconds = c.get(Calendar.SECOND);
        MyTask myTask = new MyTask(this);
        myTask.execute(Uri.parse(sentUrl));
        Calendar c2 = Calendar.getInstance();
        double seconds2 = c2.get(Calendar.SECOND);
        calcTime = seconds2-seconds;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }


    class MyTask extends AsyncTask<Uri, Void, Void>{
        private Context mContext;

        public MyTask(Context context){
            mContext = context;
        }

        @Override
        protected Void doInBackground(Uri...params){
            DownloadUtils.downloadImage(mContext, params[0]);
            // @@ No! Return the Uri returned by the call to downloadImage
            return null;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        // @@ This parameter should be a Uri, not Void
        @Override
        protected void onPostExecute(Void result) {
            // @@ Don't need to call the super implementation
            super.onPostExecute(result);
            // @@ Use logging instead
            System.out.println("FINISHED WITH DOWNLOAD");
            // @@ How do you know MainActivity called you? Consider an alternate constructor
            Intent intent = new Intent(DownloadActivity.this, MainActivity.class);
            intent.putExtra("time",calcTime);
            // @@ No; pass the data back to MainActivity through an Intent
            // @@ using setResult().
            // @@ Refactor this based on the class lecture on 10-29-15
            startActivity(intent);
        }

    }

}


