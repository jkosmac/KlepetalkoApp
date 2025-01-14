package com.example.klepetalkoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

public class AddUserActivity extends AppCompatActivity {

    private TextView status;
    private EditText firstname;
    private EditText lastname;
    private EditText username;
    private EditText password;
    private EditText email;

    private RequestQueue requestQueue;
    private String url = "https://klepetalko.azurewebsites.net/api/v1/user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        firstname = (EditText) findViewById(R.id.teName);
        lastname = (EditText) findViewById(R.id.teSurname);
        username = (EditText) findViewById(R.id.teUsername);
        email = (EditText) findViewById(R.id.teEmail);
        password = (EditText) findViewById(R.id.tePassword);
        status = (TextView) findViewById(R.id.status);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

    }

    public void addUser(View view){
        this.status.setText("Posting to " + url);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("firstName", firstname.getText().toString());
            jsonBody.put("lastName", lastname.getText().toString());
            jsonBody.put("userName", username.getText().toString());
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("passwordHash", password.getText().toString());


            final String mRequestBody = jsonBody.toString();

            status.setText(mRequestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }
            ) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        status.setText(responseString);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }

            };

            requestQueue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}