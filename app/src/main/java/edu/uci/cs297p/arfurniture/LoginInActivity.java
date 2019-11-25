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

public class LoginInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signInButton;
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView signUpLink;
    // authentication object of firebase
    private FirebaseAuth fbAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        //authenticate
        checkAuth();

        signInButton = (Button) findViewById(R.id.SignIn);
        inputEmail = (EditText) findViewById(R.id.LoginEmail);
        inputPassword =(EditText) findViewById(R.id.LoginPassword);
        signUpLink = (TextView) findViewById(R.id.SignUpLink);
        signInButton.setOnClickListener(this);
        signUpLink.setOnClickListener(this);

    }

    private void checkAuth() {
        fbAuth = FirebaseAuth.getInstance();
        if (fbAuth.getCurrentUser() != null)  {
            fbAuth.getInstance().signOut();
            //finish();
            //startActivity(new Intent(this, HomeActivity.class));
        }
    }

    private void signIn() {
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
            fbAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast tst = Toast.makeText(LoginInActivity.this,"Login successfully!",Toast.LENGTH_SHORT);
                        tst.setGravity(Gravity.CENTER,0,0);
                        tst.show();
                        startActivity(new Intent(LoginInActivity.this, HomeActivity.class));
                    }
                    else {
                        Toast tst = Toast.makeText(LoginInActivity.this,"Login failed!",Toast.LENGTH_SHORT);
                        tst.setGravity(Gravity.CENTER,0,0);
                        tst.show();
                    }
                }
            });
        }
    }

    private void goSignUpPage() {
        finish();
        startActivity(new Intent(this, SignUpActivity.class));

    }


    @Override
    public void onClick(View view) {
        if (view ==signInButton) {
            signIn();
        }

        else if (view == signUpLink) {
            goSignUpPage();

        }
    }
}
