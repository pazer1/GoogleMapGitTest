package com.example.googlemapgittest.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.googlemapgittest.R;
import com.example.googlemapgittest.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import static android.text.TextUtils.isEmpty;
import static com.example.googlemapgittest.util.Check.doStringsMatch;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";

    private EditText mEmail,mPassword,mConfirmPassword;
    private ProgressBar mProgressBar;

    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mConfirmPassword = findViewById(R.id.input_confirm_password);
        mProgressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btn_register).setOnClickListener(this);

        mDb = FirebaseFirestore.getInstance();

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_register:{
                Log.d(TAG,"onClick: attempt to register.");

                if(!isEmpty(mEmail.getText().toString())
                    && !isEmpty(mPassword.getText().toString())
                    && !isEmpty(mConfirmPassword.getText().toString())){
                    if(doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())){
                        registerNewEmail(mEmail.getText().toString(),mPassword.getText().toString());
                    }
                }
            }
        }
    }

    public void registerNewEmail(final String email, String password){
        showDialog();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"createUserWithEmail:onComplete:"+task.isSuccessful());

                        if(task.isSuccessful()){
                            Log.d(TAG,"onComplete: AuthState:" + FirebaseAuth.getInstance().getCurrentUser().getUid());

                            User user = new User();
                            user.setEmail(email);
                            user.setUsername(email.substring(0,email.indexOf("@")));
                            user.setUser_id(FirebaseAuth.getInstance().getUid());

                            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().build();
                            mDb.setFirestoreSettings(settings);

                            DocumentReference newUserRef = mDb.collection(getString(R.string.collection_users))
                                    .document(FirebaseAuth.getInstance().getUid());

                        }
                    }
                });
    }

    private void showDialog(){
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideDialog(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


}
