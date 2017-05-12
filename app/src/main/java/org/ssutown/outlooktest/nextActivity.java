package org.ssutown.outlooktest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.ssutown.outlooktest.MainActivity.authResult;


/**
 * Created by Jiyeon on 2017-05-10.
 */

public class nextActivity extends Activity {

    String resultId;
    String calendarInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Intent intent = getIntent();
        final String userID = intent.getStringExtra("userID");

        TextView textview = (TextView) findViewById(R.id.userid);

        textview.setText(userID);
        Toast.makeText(this, userID, Toast.LENGTH_SHORT).show();


        final TextView result = (TextView) findViewById(R.id.parsingtext);

        findViewById(R.id.parsingstart).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            JSONObject jsonObject = new JSONObject(userID.toString());
                            resultId = jsonObject.getString("id");
                            result.setText(resultId);
                        } catch (JSONException e) {
                        }
                    }
                }
        );

        findViewById(R.id.showcalendar).setOnClickListener(             //버튼
                new Button.OnClickListener() {
                    public void onClick(View v) {
                         callGraphAPI();

                        Log.i("clickshowcalendar", "click");
                    }
                }
        );
    }
        private void updateGraphUI(JSONObject graphResponse) {
            Log.i("updategraphui","7");//로그인 성공 후 object를 text에 써줌
            TextView calendarresponse = (TextView)findViewById(R.id.calenderinfo);
            calendarresponse.setText(graphResponse.toString());




        }

    private void callGraphAPI() {
        Log.d("call graph api", "Starting volley request to graph");

    /* Make sure we have a token to send to graph */
       // if (authResult.getAccessToken() == null) {return;}

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d("error", "Failed to put parameters: " + e.toString());
        }



        String MY_URL = "https://outlook.office.com/api/v2.0/me/calendars";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MY_URL,
                parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            /* Successfully called graph, process data and send to UI */
                Log.d("inresponse", "Response: " + response.toString());

                updateGraphUI(response);//로그인 성공 후 textview에 뿌려줌

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onerrorresponse", "Error: " + error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
                return headers;
            }
        };

        Log.d("end response", "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }




    }


