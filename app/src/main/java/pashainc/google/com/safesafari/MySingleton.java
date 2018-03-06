package pashainc.google.com.safesafari;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by black on 3/6/2018.
 */

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingleton(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return requestQueue;
    }

    public static synchronized MySingleton getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }

        return mInstance;
    }

    public void addToRequestque(Request<?> request) {
        request.setShouldCache(false);
        requestQueue.add(request);

    }
}
