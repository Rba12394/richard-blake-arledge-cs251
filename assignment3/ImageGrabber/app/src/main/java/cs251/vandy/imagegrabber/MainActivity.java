package cs251.vandy.imagegrabber;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
    private EditText mEdit;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permissionCheck = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            this.requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        mEdit = (EditText)findViewById(R.id.urlTextEdit);
    }

    public void onDownloadClicked(View view){
        url = mEdit.getText().toString();
        Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
        intent.putExtra("url", url);
        mEdit.setText("");
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("ON START");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("ON RESTART");
        TableRow newRow = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        newRow.setLayoutParams(lp);
        LinearLayout newL = new LinearLayout(this);
        TextView imageName = new TextView(this);
        TextView downloadTime = new TextView(this);
        Button showBtn = new Button(this);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(galleryIntent);
            }
        });
        imageName.setWidth(360);
        downloadTime.setWidth(360);
        showBtn.setText("SHOW");
        View v = findViewById(R.id.urlTextEdit);
        Intent mIntent = getIntent();
        String timer = mIntent.getStringExtra("time");
        downloadTime.setText(timer);
        URI uri1 = null;
        try {
            uri1 = new URI(url);
            String[] segments = uri1.getPath().split("/");
            String idStr = segments[segments.length-1];
            imageName.setText(idStr);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //(mEdit.getText().toString());
        newL.addView(imageName);
        newL.addView(downloadTime);
        newL.addView(showBtn);
        newL.addView(newRow);
        TableLayout ll = (TableLayout) findViewById(R.id.tablelayout1);
        ll.addView(newL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ON RESUME");
    }

    @Override
    protected void onPause(){
        super.onPause();
        System.out.println("ON PAUSE");
    }

    @Override
    protected void onStop(){
        super.onStop();
        System.out.println("ON STOP");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
