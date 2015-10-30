package cs251.vandy.imagegrabber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.Calendar;

public class DownloadActivity extends Activity {
private double calcTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        Intent mIntent = getIntent();
        String sentUrl = mIntent.getStringExtra("url");
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
            return null;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            System.out.println("FINISHED WITH DOWNLOAD");
            Intent intent = new Intent(DownloadActivity.this, MainActivity.class);
            intent.putExtra("time",calcTime);
            startActivity(intent);
        }

    }

}


