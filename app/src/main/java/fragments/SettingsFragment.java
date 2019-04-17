package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import er.debttracker.ForgetAndChangePasswordActivity;
import er.debttracker.R;
import er.debttracker.WelcomeScreen;


public class SettingsFragment extends Fragment {


    // View & Context Setup
    private Context mContext;
    View v;

    //Firebase DB setup
    FirebaseAuth auth;
    private DatabaseReference mDatabase;

    private Button btnLogout, btnDelete, btnChangePassword, btnChangeEmail;
    private ProgressDialog PD;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings,
                container, false);
        mContext = v.getContext();

        PD = new ProgressDialog(mContext);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        btnDelete = v.findViewById(R.id.deleteAccount);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = auth.getCurrentUser();
                String Uid = user.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Debts").child(Uid);


                Toast.makeText(mContext, "Delete Account", Toast.LENGTH_SHORT).show();
                PD.show();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mDatabase.removeValue();
                                    Toast.makeText(mContext, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(mContext, WelcomeScreen.class));
                                } else {
                                    Toast.makeText(mContext, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                }
                                PD.dismiss();
                            }
                        });
            }
        });

        btnChangeEmail = v.findViewById(R.id.changeEmail);
        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ForgetAndChangePasswordActivity.class).putExtra("Mode", 2));
            }
        });

        btnChangePassword = v.findViewById(R.id.changePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ForgetAndChangePasswordActivity.class).putExtra("Mode", 1));
            }
        });

        btnLogout = v.findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(mContext, WelcomeScreen.class));

            }
        });

        return v;
    }

}
