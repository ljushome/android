package com.ljus.android;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class DeviceControl extends AppCompatActivity {
    String url;
    String deviceName;
    ImageView more;
    RequestQueue queue;
    View halo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);
        queue = Volley.newRequestQueue(this);
        halo = findViewById(R.id.halo);
        more = findViewById(R.id.more);
        SharedPreferences sharedpreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        FloatingActionButton onoff = findViewById(R.id.onoff);
        url = "http://192.168.1.135/state";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("on")) {
                            onoff.setTag("Aus");
                            onoff.setColorFilter(Color.parseColor("#007BFF"));
                            halo.setVisibility(View.VISIBLE);
                        } else if (response.equals("off")) {
                            onoff.setColorFilter(Color.LTGRAY);
                            onoff.setTag("Ein");
                            halo.setVisibility(View.GONE);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                queue.add(stringRequest);
                handler.postDelayed(this, 500);
            }
        };

        handler.postDelayed(runnable, 500);


        deviceName = sharedpreferences.getString("deviceName", null);
        onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onoff.getTag().toString().equals("Ein")) {

                    url = "http://192.168.1.135/on";
                } else {

                    url = "http://192.168.1.135/off";
                }
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                // Don't even ask, it doesn't work that well without it
                for (int i = 0; i < 2; i++) {
                    queue.add(stringRequest);
                }
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(DeviceControl.this, more);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.device_options, popup.getMenu());
                String eraseurl = "http://192.168.1.135/erase";
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Zurücksetzen")) {
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, eraseurl,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the first 500 characters of the response string.
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                            queue.add(stringRequest);
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
    }
}