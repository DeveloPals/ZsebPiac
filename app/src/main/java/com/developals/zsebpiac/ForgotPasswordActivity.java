

package com.developals.zsebpiac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPasswordActivity extends AppCompatActivity {


    private AutoCompleteTextView fUserEmail;
    private Button forgotPw;
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    private String email;
    private String newPw;
    private String subject;
    private String massage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        /*context = this;

        fUserEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        fUserEmail.setText(LoginActivity.forgotMailSend);*/
    }

    private  String passwordGenerator(int length)
    {
        char[] chars = "1234567890-=qwertyuiop[]asdfghjkl;zxcvbnm,./()_+QWERTYUIOP{}ASDFGHJKL:|~ZXCVBNM<>?()".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i<length;i++)
        {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return  stringBuilder.toString();
    }


    public void onForgotPass(View view)
    {
        email = fUserEmail.getText().toString();
        newPw = passwordGenerator(15);
        subject = "Új jelszó";
        massage = "<h1>Kedves Ügyfelük itt az új jelszava: "+newPw+"<br></h1>" +
                "Ezúton szeretnénk figyelmeztetni, hogy rögvest állítsa át a<br>" +
                "beállítások menüpontban a jelszavát az Ön érdekében!<br>" +
                "------------------------------------------------------------<br>" +
                "<<Kérem erre az email-re ne válaszoljon!>><br>" +
                "Üdv:<br>" +
                "DevoPals";


        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("developalsnoreply@gmail.com", "Root123456");
            }
        });

        pdialog = ProgressDialog.show(context, "", "Emal küldése...", true);

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();

        String typee = "changePw";
        /*BackgroundWorker loginControllerr = new BackgroundWorker(this);
        loginControllerr.execute(typee, email, newPw);*/


    }


    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("developalsnoreply@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject(subject);
                message.setContent(massage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            fUserEmail.setText("");
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
