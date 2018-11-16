package com.developals.zsebpiac;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

//import com.developals.zsebpiac.Dao.Customers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;


public class Listing  extends AsyncTask<String,Void,String> {

    static List<String> list;
    Context context;
    AlertDialog alertDialog;
    Listing(Context ctx)
    {
        context =ctx;
    }

    public static String[] LINE11;
    //km
    public static String[] LINE22;
    //category
    public static String[] LINE33;


    @Override
    protected String doInBackground(String... params) {

        String type = params[0];

        String login_url =  "http://pte-ttk.wscdev.hu/team6/zsebpiac/listFirstProducers.php";


        if (type.equals("getList"))
        {
            try {
               // String user_email = params[1];
                //String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8")+"&"+
//                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
//                bufferedWriter.write(post_data);
//                bufferedWriter.flush();
//                bufferedWriter.close();
//                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine())!=null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return  result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    protected void onPreExecute(String result)
    {
        //  alertDialog = new AlertDialog.Builder(context).create();
        // alertDialog.setTitle("Login Status");

    }
    @Override
    protected void onPostExecute(String result) {
        list = Arrays.asList(result.split("\\s*,\\s*"));
        int j = 0;

      //  ProducersListActivity.lineItemCounter = LINE11.length;
        LINE33 = new String[30];
        LINE11 = new String[30];
        LINE22 = new String[30];
        for (int i = 0; i<list.size();i=i+3)
        {
                LINE11[j] = list.get(i) + " " + list.get(i + 1);
                LINE22[j]= "5km";
                LINE33[j] = list.get(i + 2);
                ProducersListActivity.LINE1[j]= LINE11[j];
                ProducersListActivity.LINE2[j]= LINE22[j];
                ProducersListActivity.LINE3[j]= LINE33[j];
            j++;
        }





    }




    //@Override
    protected void onProgessUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

}



