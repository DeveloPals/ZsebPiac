package com.developals.zsebpiac;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.developals.zsebpiac.AsyncTask.AsyncResponse;
import com.developals.zsebpiac.AsyncTask.PostResponseAsyncTask;
import com.tooltip.Tooltip;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "RegisterActivity";

    private AutoCompleteTextView etEmail;
    private AutoCompleteTextView etPassword;
    private AutoCompleteTextView etConfirmPassword;
    private CheckBox cbAccept;
    private Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etEmail = (AutoCompleteTextView) findViewById(R.id.text_regemail);
        etPassword = (AutoCompleteTextView) findViewById(R.id.text_regpassw);
        etConfirmPassword = (AutoCompleteTextView) findViewById(R.id.text_regconfirm);
        cbAccept = (CheckBox) findViewById(R.id.checkbox_accept);
        btnReg = (Button) findViewById(R.id.button_reg);
        btnReg.setOnClickListener(this);
    }

    private boolean passwordValidation(String pw)
    {
        int minLenght=6;
        int MaxLenght=16;
        boolean hasUppercase = !pw.equals(pw.toLowerCase());
        boolean containsNumber = pw.matches(".*\\d+.*");
        int curretLenght=pw.length();
        boolean pwIsValid= false;

        if(curretLenght>=minLenght && curretLenght<MaxLenght && hasUppercase && containsNumber)
        {
            pwIsValid = true;
        }
        return pwIsValid;

    }

    @Override
    public void onClick(View v) {
        if(cbAccept.isChecked()){
            if(!emptyValidate()){
                if(passwordValidate()){
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    HashMap<String, String> postData = new HashMap<>();
                    postData.put("email", email);
                    postData.put("password", password);

                    PostResponseAsyncTask task1 = new PostResponseAsyncTask(this,
                            postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Log.d(TAG, s);
                            if(s.contains("ErrorInsert")){
                                Toast.makeText(RegistrationActivity.this,
                                        "Something went wrong. Data was not inserted.",
                                        Toast.LENGTH_LONG).show();
                            }else {
                                Intent in = new Intent(getApplicationContext(),
                                        LoginActivity.class);
                                startActivity(in);
                            }
                        }
                    });
                    task1.execute("http://pte-ttk.wscdev.hu/team6/zsebpiac/register.php");
                }
                else{  // not equals
                    Toast.makeText(getApplicationContext(),
                            "Make sure your password is the same to confirm password",
                            Toast.LENGTH_LONG).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Fill out all the fields",
                        Toast.LENGTH_LONG).show();
            }
        }
        else
        {

        }
    }

    private boolean emptyValidate(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirm = etConfirmPassword.getText().toString();
        return (email.isEmpty() && password.isEmpty() && confirm.isEmpty());
    }

    private boolean passwordValidate(){
        String password = etPassword.getText().toString();
        String confirm = etConfirmPassword.getText().toString();
        return (password.equals(confirm));
    }
}

