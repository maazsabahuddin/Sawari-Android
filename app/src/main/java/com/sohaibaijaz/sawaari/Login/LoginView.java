package com.sohaibaijaz.sawaari.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.sohaibaijaz.sawaari.R;

public class LoginView extends AppCompatActivity implements View.OnClickListener {

    TextView textViewGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 007;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);
        getSupportActionBar().hide();

        textViewGoogle= findViewById(R.id.google_tv);

        TextView phone_number_tv = findViewById(R.id.phone_number_tv);
        phone_number_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginView.this, LoginPhone.class);
                LoginView.this.startActivity(intent);
            }
        });


        textViewGoogle.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }

    @Override
    public void onClick(View v) {

        signIn();
       // Toast.makeText(LoginView.this, "Password field cannot be empty", Toast.LENGTH_LONG).show();

    }




    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            String displayName = account.getDisplayName();
            // String dipalayFamilyName = account.getFamilyName();
            String displayEmail = account.getEmail();
            //    int phone= account.
           // user.setText(displayName);
            //passw.setText(displayEmail);

            Toast.makeText(LoginView.this, displayName+"----- "+displayEmail,Toast.LENGTH_LONG).show();
            //updateUI(true);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Main2Login", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginView.this,e.toString(),Toast.LENGTH_LONG).show();


        }
    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            //  GoogleSignInResult  result  = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // Toast.makeText(MainActivity.this,"GHLT RHY HO",Toast.LENGTH_LONG).show();

            //  handleResult(result);
        }

    }
}
