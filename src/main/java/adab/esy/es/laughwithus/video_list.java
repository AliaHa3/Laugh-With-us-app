package adab.esy.es.laughwithus;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class video_list extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public static ArrayList<Video> videoList;
    public static final String getAllVideo = "http://adab.esy.es/getAllVideo.php?id=%1$s";
    public String idType;
    private ProgressDialog progress ;
    private ListView listView;
    private Toolbar toolbar;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        idType =  getIntent().getStringExtra("idType") ;
        listView = (ListView)findViewById(R.id.list2);
        toolbar = (Toolbar)findViewById(R.id.tool_bar2);


        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.icon);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Loading..");


        url = String.format(getAllVideo,idType);
        getMethod(url);
    }


    public void getMethod(String url){

        progress.show();
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        progress.dismiss();
                        // display response
                        Log.d("Response", response.toString());
                        videoList = parseVideoJSON(response);
                        if (videoList != null)
                            initlizeLIST(videoList);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // add it to the RequestQueue
       MainActivity.queue.add(getRequest);
    }

    public ArrayList<Video> parseVideoJSON(JSONObject jsonObj){

        // Videos JSONArray
        JSONArray videos = null;

        // for ListView
        ArrayList<Video> videoList = new ArrayList<>();

        // Getting JSON Array node
        try {
            videos = jsonObj.getJSONArray("videos");

            // looping through All videos
            for (int i = 0; i < videos.length(); i++) {

                JSONObject c = videos.getJSONObject(i);

                String name = c.getString(Video.TAG_NAME);
                String link =  c.getString(Video.TAG_LINK);
                // adding video to video list
                videoList.add(new Video(name,link));
            }
            return videoList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initlizeLIST(ArrayList<Video> data){
        ArrayAdapter<Video> adapter = new ArrayAdapter<>(this,R.layout.video_item,R.id.video_name,
                data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about2) {
            Intent intent = new Intent(this,About.class);
            startActivity(intent);
        }
        if(id == R.id.refresh2)
            getMethod(url);

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Video video = (Video)listView.getItemAtPosition(i);

        Intent intent = new Intent(this,VideoViewClass.class);
        intent.putExtra("link",video.link);
        intent.putExtra("name",video.name);
        startActivity(intent);
    }
}
