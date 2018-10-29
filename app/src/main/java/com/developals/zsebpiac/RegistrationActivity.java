package com.developals.zsebpiac;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

import com.tooltip.Tooltip;

public class RegistrationActivity extends AppCompatActivity {

    private AutoCompleteTextView mUserEmail;
    private AutoCompleteTextView mUserPassword;
    private AutoCompleteTextView mUserPasswordComfirm;
    private CheckBox mUserDataCheck;
    public static boolean emailValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        mUserEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        mUserPassword = (AutoCompleteTextView) findViewById(R.id.userPassword);
        mUserPasswordComfirm = (AutoCompleteTextView) findViewById(R.id.userComfirm);
        mUserDataCheck = (CheckBox) findViewById(R.id.radioButton);

    }

    static void showTooltip(View v, int gravty)
    {
        Button btn = (Button)v;
        Tooltip tooltip = new Tooltip.Builder(btn)
                .setText(LoginActivity.loginStatus)
                .setTextColor(Color.WHITE)
                .setGravity(gravty)
                .setCornerRadius(8f)
                .setDismissOnClick(true)
                .show();

    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){

        }
    }
//    public void emailCheck (RegistrationActivity view)
//    {
//        String type = "emailCheck";
//        String username =  mUserEmail.getText().toString();
//        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//        backgroundWorker.execute(type,username);
//    }



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



    public void OnReg(View view)
    {
        String username =  mUserEmail.getText().toString();
        String password =  mUserPassword.getText().toString();
        String passwordConfirm =  mUserPasswordComfirm.getText().toString();
        emailValid = true;
       // emailCheck(this);

        if(mUserDataCheck.isChecked())
        {
            if (password.equals(passwordConfirm))
            {
                if(passwordValidation(password))
                {
                    String typee = "register";
                    BackgroundWorker loginControllerr = new BackgroundWorker(this);
                    loginControllerr.execute(typee, username, password, passwordConfirm);

                    if(emailValid)
                    {
                        String status = "Sikeres regisztráció!";
                        LoginActivity.loginStatus = status;
                        showTooltip(view, Gravity.TOP);
                        emailValid = true;
                    }
                    else
                    {
                        String status = "Ez az email már létezik!";
                        LoginActivity.loginStatus=status;
                        showTooltip(view,Gravity.TOP);
                    }

                }
                else
                {
                    String status = "A jelszó minimum 6 karakterbõl állhat és maximum 16-ból(a-z)!" +
                            "Tartalmaznia kell egy számot is (0-9)"+
                            "Illetve egy nagybetüt is (A-Z)";
                    LoginActivity.loginStatus = status;
                    showTooltip(view, Gravity.TOP);
                }





            }
            else
            {
                String status = "A két jelszó nem egyezik!";
                LoginActivity.loginStatus=status;
                showTooltip(view,Gravity.TOP);
            }
        }


        else
        {
            String status = "Kérem fogadja el az adatvédelmi nyilatkozatot!";
            LoginActivity.loginStatus=status;
            showTooltip(view,Gravity.TOP);

        }


    }
}

