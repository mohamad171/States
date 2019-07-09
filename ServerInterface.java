package ir.moderndata.states;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class ServerInterface {
    Context context;
    RequestQueue queue;
    String baseUrl="http://193.176.243.39:600/";
    responseListeneer responselisteneer;
    ErrorListeneer errorListeneer;

    public ServerInterface(Context context ) {
        this.context = context;
        queue = Volley.newRequestQueue(context);


    }

    public void Get(String resource,responseListeneer responseListeneer){
        Log.wtf("dataaa",baseUrl+resource);
        responselisteneer = responseListeneer;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, baseUrl+resource,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responselisteneer.OnResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("dataaa",error.toString());
                Toast.makeText(context, "خطایی در ارتباط با سرور رخ داده است", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void Post(String resource, final Map<String,String> params,responseListeneer responseListeneer,final ErrorListeneer errorListeneer){
        responselisteneer = responseListeneer;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+resource,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responselisteneer.OnResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListeneer.OnError(error);

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }
    public void Post(String resource, final Map<String,String> params,final Map<String,String> headers,responseListeneer responseListeneer,final ErrorListeneer errorListeneer){
        responselisteneer = responseListeneer;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+resource,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responselisteneer.OnResponse(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorListeneer.OnError(error);

            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

    }

    public interface responseListeneer{
        public void OnResponse(String content);
    }
    public interface ErrorListeneer{
        public void OnError(VolleyError error);
    }


}
