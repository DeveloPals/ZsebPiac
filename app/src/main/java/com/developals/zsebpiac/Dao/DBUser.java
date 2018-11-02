package com.developals.zsebpiac.Dao;

import android.content.Context;
import android.util.Log;

import com.developals.zsebpiac.AsyncTask.AsyncResponse;
import com.developals.zsebpiac.AsyncTask.PostResponseAsyncTask;

import java.util.HashMap;

public class DBUser {

    private static final String TAG = "DBProducer";
    private static final String url = DBMain.URL;

    public static void Update(Context context, String email, String firstName, String lastName,
                              String producerID) {
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("first_name", firstName);
        data.put("last_name", lastName);
        data.put("producer_id", producerID);

        PostResponseAsyncTask task = new PostResponseAsyncTask(context, data, new ClassAsyncResponse());
        task.setExceptionHandler(new ClassExceptionHandler());
        task.execute(url.concat("updateUserData.php"));
    }

    private static class ClassAsyncResponse implements AsyncResponse {

        @Override
        public void processFinish(String output) {
            Log.d(TAG, output);
        }
    }

    private static class ClassExceptionHandler implements com.developals.zsebpiac.AsyncTask.ExceptionHandler {

        @Override
        public void handleException(Exception e) {
            if(e != null && e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }
        }
    }
}
