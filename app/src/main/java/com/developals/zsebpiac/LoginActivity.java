package com.developals.zsebpiac;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    private AutoCompleteTextView mUserEmail;
    private AutoCompleteTextView mUserPassword;
    private TextView mCreateAccount;
    private TextView mForgotPassword;
    private Button mLoginButton;
    static  boolean mlogin = false;
    static String loginStatus;
    static String username;

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
            mUserEmail.setText(account.getEmail());
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
        }
    }

    private void getFacebookData(JSONObject object) {
        try{
            URL profilePicture = new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?type=large"); //width=250&height=250");
            //Picasso.with(this).load(profilePicture.toString()).into(imageView);
            mUserEmail.setText(object.getString("email"));
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

        mUserEmail = (AutoCompleteTextView) findViewById(R.id.user_email);
        mUserPassword = (AutoCompleteTextView) findViewById(R.id.user_password);
        mCreateAccount = (TextView) findViewById(R.id.create_account);
        mForgotPassword = (TextView) findViewById(R.id.forgot_password);
        mLoginButton = (Button) findViewById(R.id.user_login);
        mForgotPassword.setOnClickListener(this);
        mCreateAccount.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

        mUserEmail.setText("root");
        mUserPassword.setText("Root123");

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

        Button loginButton = (Button)findViewById(R.id.fb_login_button);
        loginButton.setOnClickListener(this);

        //LoginWithFacebook(AccessToken.getCurrentAccessToken());

        //GOOGLE PLUS LOGIN
        Button signInButton = (Button)findViewById(R.id.google_login_button);
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


    static void showTooltip(View v, int gravty)
    {
        Button btn = (Button)v;
        Tooltip tooltip = new Tooltip.Builder(btn)
                .setText(loginStatus)
                .setTextColor(Color.WHITE)
                .setGravity(gravty)
                .setCornerRadius(8f)
                .setDismissOnClick(true)
                .show();

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.user_login:

                username =  mUserEmail.getText().toString();
                String password =  mUserPassword.getText().toString();
                String type = "Login";
                BackgroundWorker backgroundWorkerr = new BackgroundWorker(this);
                backgroundWorkerr.execute(type,username,password);

                Handler handler=new Handler();
                Runnable r=new Runnable() {
                    public void run()
                    {
                        if(mlogin) {
                            Intent chooseIntent = new Intent(LoginActivity.this, ChooseActivity.class);
                            startActivity(chooseIntent);
                            showTooltip(v,Gravity.BOTTOM);
                        }
                        else
                        {
                            showTooltip(v,Gravity.BOTTOM);
                        }

                    }
                };
                handler.postDelayed(r, 1000);




                break;
            case R.id.fb_login_button:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email","user_birthday"));
                break;
            case R.id.google_login_button:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.create_account:
                Intent createAccountIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(createAccountIntent);
                break;
            case R.id.forgot_password:
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
                break;
        }
    }
}
