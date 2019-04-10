package er.debttracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private String email, password;
    private FirebaseAuth auth;
    private Button btnLogin, forgetPassword;
    private ProgressDialog PD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        auth = FirebaseAuth.getInstance();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);


        btnLogin = (Button) findViewById(R.id.checkEntriesLoginBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {

                inputEmail = (EditText) findViewById(R.id.usernameText);
                inputPassword = (EditText) findViewById(R.id.passwordText);
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();

                try {

                    if (password.length()>0 && email.length()>0) {
                        PD.show();
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("error", task.getException());
                                            Toast.makeText(
                                                    LoginScreen.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();

                                        } else {
                                            inputEmail.setError(null);
                                            inputPassword.setError(null);

                                            Intent intent = new Intent(LoginScreen.this, DashboardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {

                        if (password.length()==0) {
                            inputPassword.setError("Empty Field");
                        } else if (password.length()>0) inputPassword.setError(null);

                        if (email.length()==0) {
                            inputEmail.setError("Empty Field");
                        } else if (email.length()>0) inputEmail.setError(null);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        forgetPassword = findViewById(R.id.forget_password_button);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 0));
            }
        });
    }


}
