package er.debttracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SignupScreen extends AppCompatActivity {

    private EditText fNameEntry, lNameEntry, usernameEntry, passwordEntry;
    private String fName, lName, username, password;
    private FirebaseAuth auth;
    private DatabaseReference fUsersDatabase;

    private ProgressDialog PD;
    private static List<Users> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        auth = FirebaseAuth.getInstance();
        fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        Button signUpBtn = this.findViewById(R.id.createAcctBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fNameEntry = (EditText) findViewById(R.id.firstNameEntry);
                lNameEntry = (EditText) findViewById(R.id.lastNameEntry);
                usernameEntry = (EditText) findViewById(R.id.usernameEntry);
                passwordEntry = (EditText) findViewById(R.id.passwordEntry);

                fName = fNameEntry.getText().toString();
                lName = lNameEntry.getText().toString();
                username = usernameEntry.getText().toString();
                password = passwordEntry.getText().toString();

                try {

                    if (validatePassword(password) && password.length() > 0 && username.length() > 0) {
                        PD.show();
                        fNameEntry.setError(null);
                        lNameEntry.setError(null);
                        usernameEntry.setError(null);
                        passwordEntry.setError(null);

                        auth.createUserWithEmailAndPassword(username, password)
                                .addOnCompleteListener(SignupScreen.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            usernameEntry.setError("Invalid Email");
                                            Log.w("error", task.getException());
                                            Toast.makeText(
                                                    SignupScreen.this,
                                                    "Authentication Failed",
                                                    Toast.LENGTH_LONG).show();
                                        } else {

                                            FirebaseUser user = auth.getCurrentUser();
                                            String id = user.getUid();
                                            Users newUser = new Users(id, fName, lName, username, password);
                                            userList.add(newUser);


                                            Intent intent = new Intent(SignupScreen.this, WelcomeScreen.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        PD.dismiss();
                                    }
                                });
                    } else {

                        if (!validateFirstName(fName)) {
                            fNameEntry.setError("Invalid First Name. Enter letters only.");
                        } else if (validateFirstName(fName)) fNameEntry.setError(null);

                        if (!validateLastName(lName)) {
                            lNameEntry.setError("Invalid Last Name. Enter letters only.");
                        } else if (validateLastName(lName)) lNameEntry.setError(null);

                        if (username.length() == 0) {
                            usernameEntry.setError("Empty Field");
                        } else if (username.length() != 0) usernameEntry.setError(null);

                        /*if (!checkUsernameDB(username)) {
                            Toast.makeText(
                                    SignupScreen.this,
                                    "Account already exists!",
                                    Toast.LENGTH_LONG).show();
                        }*/

                        if (!validatePassword(password)) {
                            passwordEntry.setError("Invalid Password. Password must be 8 characters or more and has to include at least one capital letter, one lower case letter and one digit.");
                        } else if (validatePassword(password)) passwordEntry.setError(null);


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }
// User Input validation functions

    //Checks if First Name is only letters and has two or more characters
    private boolean validateFirstName(String fName) {
        char c;
        int len, i, x = 0;
        len = fName.length();

        if (len >= 2) {
            for (i = 0; i < len; i++) {
                c = fName.charAt(i);
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) && c != ' ') {
                    x++;
                } else ;
            }
            if (x == len) return true;
            else return false;
        } else return false;
    }

    //Checks if Last Name is only letters and has two or more characters
    private boolean validateLastName(String lName) {
        char d;
        int length, x, y = 0;
        length = lName.length();

        if (length >= 2) {
            for (x = 0; x < length; x++) {
                d = lName.charAt(x);
                if (((d >= 'a' && d <= 'z') || (d >= 'A' && d <= 'Z')) && d != ' ') {
                    y++;
                } else ;
            }
            if (y == length) return true;
            else return false;
        } else return false;
    }

    // Checks if username has already been taken and returns true otherwise
   /* private boolean checkUsernameDB(String usernameDB) {

        List<String> usernames = new LinkedList<String>();
        Cursor cur = myUsersDataB.getAllUsers();

        if (cur.getCount() != 0) {
            while (cur.moveToNext()) {
                String usrName = cur.getString(cur.getColumnIndex(USER_KEY_USERNAME));
                usernames.add(usrName);
            }
            if (usernames.contains(usernameDB)) return false;
            else return true;
        } else return true;
    }*/

    // Checks if password meet the minimun requirements
    //At least 8 characters long, at least one upper case, one lower case and one digit
    private boolean validatePassword(String password) {

        char c;
        int b;
        int l = 0;
        int u = 0;
        int d = 0;
        int t = 0;
        int len = password.length();

        if (len >= 8) {
            for (b = 0; b < len; b++) {
                c = password.charAt(b);
                if (Character.isLowerCase(c)) l++;
                if (Character.isUpperCase(c)) u++;
                if (Character.isDigit(c)) d++;
                if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || Character.isDigit(c)) && c != ' ') {
                    t++;
                } else ;
            }

            if (t == len && (l + u + d) == len && l >= 1 && u >= 1 && d >= 1) return true;
            else return false;
        } else return false;
    }
}