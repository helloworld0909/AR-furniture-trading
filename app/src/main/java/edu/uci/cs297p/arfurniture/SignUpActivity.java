package edu.uci.cs297p.arfurniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signUpButton;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView signInLink;
    // authentication object of firebase
    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        checkAuth();

        signUpButton = (Button) findViewById(R.id.SignUp);
        inputEmail = (EditText) findViewById(R.id.SignUpEmail);
        inputPassword =(EditText) findViewById(R.id.SignUpPassword);
        signInLink = (TextView) findViewById(R.id.SignInLink);
        signUpButton.setOnClickListener(this);
        signInLink.setOnClickListener(this);
    }

    private void checkAuth() {
        fbAuth = FirebaseAuth.getInstance();
        if (fbAuth.getCurrentUser() != null)  {
            fbAuth.getInstance().signOut();
        }
    }
    private void signUp() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (email.isEmpty()) {
            Toast tst = Toast.makeText(this, "Empty email account!", Toast.LENGTH_SHORT);
            tst.setGravity(Gravity.CENTER,0,0);
            tst.show();
        }
        else if (password.isEmpty()) {
            Toast tst = Toast.makeText(this, "Empty password!", Toast.LENGTH_SHORT);
            tst.setGravity(Gravity.CENTER, 0, 0);
            tst.show();
        }
        else {
            fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast tst = Toast.makeText(SignUpActivity.this,"SignUp successfully!",Toast.LENGTH_SHORT);
                        tst.setGravity(Gravity.CENTER,0,0);
                        tst.show();
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    }
                    else {
                        Toast tst = Toast.makeText(SignUpActivity.this,"SignUp failed!",Toast.LENGTH_SHORT);
                        tst.setGravity(Gravity.CENTER,0,0);
                        tst.show();
                    }
                }
            });
        }
    }

    private void goSignInPage() {
        finish();
        startActivity(new Intent(this, LoginInActivity.class));
    }

    @Override
    public void onClick(View view) {
        if(view == signUpButton ) {
            signUp();
        }
        else if (view == signInLink ) {
            goSignInPage();
        }
    }
}
