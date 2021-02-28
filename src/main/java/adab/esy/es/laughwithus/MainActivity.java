package adab.esy.es.laughwithus;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    public static RequestQueue queue;
    public static ArrayList<Type>typeList;
    public static final String getAllType ="http://adab.esy.es/getAllType.php";
    private ProgressDialog progress ;
    private Toolbar toolbar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.list1);
        toolbar = (Toolbar)findViewById(R.id.tool_bar1);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.icon);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Loading..");

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        getMethod(getAllType);
    }


    public void getMethod(final String url){
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
                        typeList = parseTypeJSON(response);
                        if (typeList != null)
                            initlizeLIST(typeList);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Log.d("Error.Response", error.toString());
                        if(error.getClass().equals(NoConnectionError.class)){


                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("Connection Error");

                            // Setting Dialog Message
                            alertDialog.setMessage("You need to switch on WIFI.");

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User pressed YES button. Write Logic Here

                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton("Setting", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User pressed No button. Write Logic Here
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                }
                            });

                            // Setting Netural "Cancel" Button
                            alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User pressed Cancel button. Write Logic Here
                                   finish();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }
                    }
                }
        );

        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // add it to the RequestQueue
        queue.add(getRequest);
    }

    public ArrayList<Type> parseTypeJSON(JSONObject jsonObj){

        // Types JSONArray
        JSONArray types = null;

        // for ListView
        ArrayList<Type> typeList = new ArrayList<>();

        // Getting JSON Array node
        try {
             types = jsonObj.getJSONArray("types");

            // looping through All Types
            for (int i = 0; i < types.length(); i++) {

                JSONObject c = types.getJSONObject(i);

                String name = c.getString(Type.TAG_NAME);
                int id = c.getInt(Type.TAG_ID);

                // adding type to types list
                typeList.add(new Type(id,name));
            }
            return typeList;
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initlizeLIST(ArrayList<Type> data){
        ArrayAdapter<Type> adapter = new ArrayAdapter<>(this,R.layout.type_item,R.id.name_type,
                                                        data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about1) {
            Intent intent = new Intent(this,About.class);
            startActivity(intent);
        }
        if(id == R.id.refresh1)
            getMethod(getAllType);

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Type type = (Type)listView.getItemAtPosition(i);
        Log.d("Response", "click on item");

        Intent intent = new Intent(this,video_list.class);
        intent.putExtra("idType",type.id +"");
        startActivity(intent);
    }
}
