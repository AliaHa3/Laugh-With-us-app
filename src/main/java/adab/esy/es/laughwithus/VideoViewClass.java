package adab.esy.es.laughwithus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewClass extends ActionBarActivity {

    public VideoView videoView;
    private ProgressDialog progress ;
    private Toolbar toolbar;
    private String link,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        link = getIntent().getStringExtra("link");
        name = getIntent().getStringExtra("name");
        videoView = (VideoView)findViewById(R.id.Video_view);

        toolbar = (Toolbar)findViewById(R.id.tool_bar3);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.icon);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Loading Video ..");
        progress.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoView);

            // Get the URL from String VideoURL
            Uri video = Uri.parse(link);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progress.dismiss();
                videoView.start();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about3) {
            Intent intent = new Intent(this,About.class);
            startActivity(intent);
        }
        if(id==R.id.share){
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareString =
                    "لقد وجدت فيديوهات ومقاطع مضحكة على هذا التطبيق أنصحك بتنزيله ومشاهدتها";

            String shareString2 =
                    "هذا هو الرابط";
            String shareString3 =
            "على تطبيق اضحك معنا";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, name +" "+ shareString3);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareString +"\n" +shareString2 );
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        return super.onOptionsItemSelected(item);
    }
}
