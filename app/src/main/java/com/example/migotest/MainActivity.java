package com.example.migotest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.migotest.databinding.ActivityMainBinding;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private Context mContext;

    private static String TAG = "[MIGO][TEST]";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private int mNetworkType;

    private String PUBLIC_URL = "https://code-test.migoinc-dev.com/status";
    private String PRIVATE_URL = "https://192.168.2.2/status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        mContext = this;

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAndRequestResponse();
            }
        });

        mNetworkType = getNetworkType();
        String type;

        switch (mNetworkType) {
            case ConnectivityManager.TYPE_MOBILE :
                type = "Mobile";
                break;
            case ConnectivityManager.TYPE_WIFI :
                type = "Wifi";
                break;
            default :
                type = "Others";
        }

        Toast.makeText(getApplicationContext(),"Network status : " + type, Toast.LENGTH_LONG).show();
    }

    private int getNetworkType() {
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connManager.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                if (netInfo.isConnected()) {
                    return ConnectivityManager.TYPE_WIFI;
                }
            }
            else if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (netInfo.isConnected()) {
                    return ConnectivityManager.TYPE_MOBILE;
                }
            }
        }

        return -1;
    }

    private void sendAndRequestResponse() {
        String url = (mNetworkType == ConnectivityManager.TYPE_WIFI) ? PRIVATE_URL : PUBLIC_URL;

        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override @UiThread
            public void onResponse(String response) {
                JSONObject obj = null;
                String responseCode;

                try {
                    obj = new JSONObject(response);
                    responseCode = obj.getString("status");
                    Toast.makeText(getApplicationContext(),"Http status : " + responseCode, Toast.LENGTH_LONG).show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override @UiThread
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
                Toast.makeText(getApplicationContext(),"Http status : " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}