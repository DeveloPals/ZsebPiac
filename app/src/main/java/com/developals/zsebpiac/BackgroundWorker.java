package com.developals.zsebpiac;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.developals.zsebpiac.Dao.Customers;

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

public class BackgroundWorker extends AsyncTask<String,Void,String>{

    Context context;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx)
    {
        context =ctx;
    }
    String test = "-1";
    static public Integer asd = 1;
    static Boolean testt = false;
    static List<String> items;
    static Customers customers;

    @Override
    protected String doInBackground(String... params)
    {
        String type = params[0];
        String login_url =  "http://pte-ttk.wscdev.hu/team6/login.php";
        String register_url =  "http://pte-ttk.wscdev.hu/team6/register.php";
        String getCustomerData_url =  "http://pte-ttk.wscdev.hu/team6/getCustomerData.php";

        if (type.equals("Login"))
        {
            try {
                String user_email = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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

      else  if(type.equals("register"))
        {
            try {
                String user_email = params[1];
                String password = params[2];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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
      else   if(type.equals("getCustomer"))
        {
            try {
                String user_email = params[1];
                URL url = new URL(getCustomerData_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_email,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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

        String str1 = "Sikeres_Bejelentkezes!";
        String str2 = "Sikertelen bejelentkezés!:  A megadott email cím nem létezik vagy a jelszó helytelen!";
        String emailExist = "FailedInsert";

        if(str1.equals(result))
        {
            LoginActivity.mlogin = true;
            LoginActivity.loginStatus=result;
        }
        else
        {
            LoginActivity.mlogin=false;
            LoginActivity.loginStatus=str2;
        }
        if(emailExist.equals(result))
        {
            RegistrationActivity.emailValid = false;
        }

        if(result.length()>150)
        {
             items = Arrays.asList(result.split("\\s*,,,\\s*"));
             //customers = new Customers(3,items.get(1), items.get(2),items.get(3),items.get(4),items.get(5),items.get(6));
            // uj Peéldaány
            customers = new Customers();
            // adatok hozzáadása
            customers.setId(Integer.parseInt(items.get(0)));
            customers.setEmail(items.get(1));
            customers.setPassword(items.get(2));
            customers.setFirstName(items.get(3));
            customers.setLastName(items.get(4));
            customers.setCoordinate(items.get(5));
            customers.setPicture(items.get(6));
            LoginActivity.loginStatus=customers.getEmail();

        }








        // alertDialog.setMessage(result);
         // alertDialog.show();




    }




    //@Override
    protected void onProgessUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

}
