package com.developals.zsebpiac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developals.zsebpiac.AsyncTask.AsyncResponse;
import com.developals.zsebpiac.AsyncTask.ExceptionHandler;
import com.developals.zsebpiac.AsyncTask.PostResponseAsyncTask;
import com.developals.zsebpiac.Dao.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.tooltip.Tooltip;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;
    private final String TAG = "LoginActivity";

    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    private AutoCompleteTextView etEmail;
    private AutoCompleteTextView etPassword;
    private TextView tvCreateAccount;
    private TextView tvForgotPassword;
    private Button btnLoginButton;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            LoginWithGoogle(account);
        } catch (ApiException e) {
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            LoginWithGoogle(null);
        }
    }

    private void LoginWithGoogle(GoogleSignInAccount account) {
        if(account != null){
            etEmail.setText(account.getEmail());
            Intent chooseIntent = new Intent(LoginActivity.this, ChooseActivity.class);
            startActivity(chooseIntent);

        }
    }

    private void LoginWithFacebook(AccessToken accessToken){
        if(accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d("response", response.toString());
                    getFacebookData(object);
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email");
            request.setParameters(parameters);
            request.executeAsync();

            Intent chooseIntent = new Intent(LoginActivity.this, ChooseActivity.class);
            startActivity(chooseIntent);
        }
    }

    private void getFacebookData(JSONObject object) {
        try{
            URL profilePicture = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?type=large"); //width=250&height=250");
            //Picasso.with(this).load(profilePicture.toString()).into(imageView);
            //etEmail.setText(object.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //LoginWithGoogle(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //printKeyHash();

        etEmail = (AutoCompleteTextView) findViewById(R.id.text_login_email);
        etPassword = (AutoCompleteTextView) findViewById(R.id.text_login_password);
        tvCreateAccount = (TextView) findViewById(R.id.text_create_account);
        tvForgotPassword = (TextView) findViewById(R.id.text_forgot_passw);
        btnLoginButton = (Button) findViewById(R.id.button_login);
        tvForgotPassword.setOnClickListener(this);
        tvCreateAccount.setOnClickListener(this);
        btnLoginButton.setOnClickListener(this);

        etEmail.setText("root");
        etPassword.setText("root");

        //FACEBOOK LOGIN
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        LoginWithFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        Button loginButton = (Button)findViewById(R.id.button_facebook);
        loginButton.setOnClickListener(this);

        //LoginWithFacebook(AccessToken.getCurrentAccessToken());

        //GOOGLE PLUS LOGIN
        Button signInButton = (Button)findViewById(R.id.button_google);
        signInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void printKeyHash(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.developals.zsebpiac", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.button_login:
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();



                HashMap<String, String> loginData = new HashMap<>();
                loginData.put("email", email);
                loginData.put("password", password);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,
                        loginData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(TAG, s);
                        if(!s.contains("Error")){
                            SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();
                            Intent in = new Intent(getApplicationContext(), ChooseActivity.class);
                            startActivity(in);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong. Cannot login.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                loginTask.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public void handleException(Exception e) {
                        if(e != null && e.getMessage() != null){
                          Log.d(TAG, e.getMessage());
                        }
                    }
                });
                loginTask.execute("http://pte-ttk.wscdev.hu/team6/zsebpiac/login.php");
                break;
            case R.id.button_facebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email","user_birthday"));
                break;
            case R.id.button_google:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.text_create_account:
                Intent createAccountIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(createAccountIntent);
                break;
            case R.id.text_forgot_passw:
                //forgotMailSend=etEmail.getText().toString();
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
                break;
        }
    }
}
