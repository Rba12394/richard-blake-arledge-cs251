package cs251.vandy.imagegrabberservice;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

public class MainActivity extends Activity {
    // @@ https://google.github.io/styleguide/javaguide.html#s4.6.1-vertical-whitespace
    private EditText mEdit;
    private String mUrl;
    static final String MY_URL = "url";

    // @@ Why make this static?
    public static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit = (EditText) findViewById(R.id.urlTextEdit);
    }

    public void onDownloadServiceClicked(View view) {
        // @@ Why start an Activity and then decide which download method to use?
        // @@ Don't start a new Activity for this
        Intent i = createIntent("service");
        startActivity(i);
    }

    public void onDownloadThreadClicked(View view) {
        // @@ Why start an Activity and then decide which download method to use?
        // @@ Don't start a new Activity for this
        Intent i = createIntent("thread");
        defineHandler();
        startActivity(i);
    }

    private Intent createIntent(String method){
        getUrl(mEdit);
        Intent intent = new Intent(this, DownloadActivity.class);
        intent.putExtra(MY_URL, mUrl);
        // @@ Use a named constant
        intent.putExtra("DownloadMethod", method);
        return intent;
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




}