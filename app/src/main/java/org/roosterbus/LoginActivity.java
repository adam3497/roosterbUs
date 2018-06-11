package org.roosterbus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    //constant fields
    private static final int RC_SIGN_IN = 101;
    private static final String NAME_FACEBOOK_PROFILE = "name";
    private static final String EMAIL_FACEBOOK_PROFILE = "email";
    private static final String PICTURE_FACEBOOK_PROFILE = "picture";
    private static final String TAG = "LoginActivity";


    private Button btnLogin, btnCreateAccount;
    private LoginButton btnFacebookLogin;
    private SignInButton btnGoogleSignIn;
    private EditText etxUsername, etxPassword;

    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        //initialize callback manager
        callbackManager = CallbackManager.Factory.create();

        setViews();

    }
    

    @Override
    protected void onStart() {
        super.onStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(isLoggedIn){
            loggedFacebook();
        }
        else{
            // Check for existing Google Sign In account, if the user is already signed in
            // the GoogleSignInAccount will be non-null.
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            googleSignInManager(account);
        }
    }

    /**
     * This method initialize all the views and put the listener to corresponding
     * Also, manage the listener to every view
     */
    private void setViews() {
        //find views
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCreateAccount = (Button) findViewById(R.id.btn_login_create_account);
        etxUsername = (EditText) findViewById(R.id.etx_login_username);
        etxPassword = (EditText) findViewById(R.id.etx_login_password);
        btnFacebookLogin = (LoginButton) findViewById(R.id.btn_facebook_login);
        btnGoogleSignIn = (SignInButton) findViewById(R.id.btn_google_login);

        //action for Google login button
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //action for Facebook login button
        btnFacebookLogin.setReadPermissions(Arrays.asList("public_profile", "email"));
        //callback registration
        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Snackbar.make(btnFacebookLogin, "Inicio éxitoso", Snackbar.LENGTH_SHORT).show();

                //obtain the needed information
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try{
                            //get profile information
                            String name = "";
                            String email = "";
                            String uriPicture = "";

                            if(object.getString(NAME_FACEBOOK_PROFILE) != null){
                                name = object.getString(NAME_FACEBOOK_PROFILE);
                            }
                            if(object.getString(EMAIL_FACEBOOK_PROFILE) != null){
                                email = object.getString(EMAIL_FACEBOOK_PROFILE);
                            }
                            if(object.getString(PICTURE_FACEBOOK_PROFILE) != null){
                                JSONObject imagen = new JSONObject(object.getString(PICTURE_FACEBOOK_PROFILE));
                                JSONObject imagen2 = new JSONObject(imagen.getString("data"));
                                uriPicture = imagen2.getString("url");
                            }

                            // save profile information to preferences
                            SharedPreferences prefs = getSharedPreferences("com.altarosprojects.seriesanimes", Context.MODE_PRIVATE);
                            prefs.edit().putString("facebookName", name).apply();
                            prefs.edit().putString("facebookEmail", email).apply();
                            prefs.edit().putString("facebookUriPicture", uriPicture).apply();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,picture");
                request.setParameters(parameters);
                request.executeAsync();

                loggedFacebook();
            }

            @Override
            public void onCancel() {
                Snackbar.make(btnFacebookLogin, "Inicio cancelado", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Snackbar.make(btnFacebookLogin, "Se produjo un error al iniciar con Facebook", Snackbar.LENGTH_SHORT).show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.putExtra("accountSignedGoogle", false);
                mainIntent.putExtra("accountSignedFacebook", false);
                startActivity(mainIntent);
                finish();
            }
        });

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * This method is called when a facebook account is logged in
     */
    private void loggedFacebook() {
        Toast.makeText(getApplicationContext(), "Inicio de sesión éxito con la cuenta de Facebook", Toast.LENGTH_SHORT).show();
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.putExtra("accountSignedFacebook", true);
        mainIntent.putExtra("accountSignedGoogle", false);
        startActivity(mainIntent);
        finish();
    }

    /**
     * This method manage the result of a Google sing in account
     * @param task the result of the sign in operation
     */
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            googleSignInManager(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            googleSignInManager(null);
        }
    }

    /**
     * This method is for manage a sign in with Google, if the account parameter != null then proceed with the next activity
     * @param account if != null is because a account is signed in
     */
    private void googleSignInManager(GoogleSignInAccount account) {
        if(account != null){
            Toast.makeText(getApplicationContext(), "Inicio de sesión éxito con la cuenta de Google", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            mainIntent.putExtra("accountSignedGoogle", true);
            mainIntent.putExtra("accountSignedFacebook", false);
            startActivity(mainIntent);
            finish();
        }
    }

}
