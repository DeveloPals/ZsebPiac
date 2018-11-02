package com.developals.zsebpiac.Dao;

import android.content.Context;
import android.util.Log;

import com.developals.zsebpiac.AsyncTask.AsyncResponse;
import com.developals.zsebpiac.AsyncTask.ExceptionHandler;
import com.developals.zsebpiac.AsyncTask.PostResponseAsyncTask;
import com.developals.zsebpiac.LoginActivity;
import com.developals.zsebpiac.ProducersDataActivity;

import java.util.HashMap;

public class DBPhoneNumber {

    private static final String TAG = "DBPhoneNumber";
    private static final String url = DBMain.URL;

    public static void InsertInto(Context context, String email, String num){
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("number", num);

        PostResponseAsyncTask task = new PostResponseAsyncTask(context, data, new ClassAsyncResponse());
        task.setExceptionHandler(new ClassExceptionHandler());
        task.execute(url.concat("insertNumber.php"));
    }

    public static void Delete(Context context, String email, String num){
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("number", num);

        PostResponseAsyncTask task = new PostResponseAsyncTask(context, data, new ClassAsyncResponse());
        task.setExceptionHandler(new ClassExceptionHandler());
        task.execute(url.concat("deleteNumber.php"));
    }

    public static void Update(Context context, String email, String oldNum, String newNum){
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("old_number", oldNum);
        data.put("new_number", newNum);

        PostResponseAsyncTask task = new PostResponseAsyncTask(context, data, new ClassAsyncResponse());
        task.setExceptionHandler(new ClassExceptionHandler());
        task.execute(url.concat("updateNumber.php"));
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
