package er.debttracker;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class NewDebtActivity extends AppCompatActivity {

    private Button btnCreate;
    private TextInputEditText etDebtorName;
    private EditText etPhone, etBalance, etDate;

    private FirebaseAuth fAuth;
    private DatabaseReference fDebtsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_debt);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        etDebtorName = (TextInputEditText) findViewById(R.id.etDebtorName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etBalance = (EditText) findViewById(R.id.etBalance);
        etDate = (EditText) findViewById(R.id.etDate);

        fAuth = FirebaseAuth.getInstance();
        fDebtsDatabase = FirebaseDatabase.getInstance().getReference().child("Debts").child(fAuth.getCurrentUser().getUid());
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etDebtorName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String balance = etBalance.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                if(!TextUtils.isEmpty(name)){
                    createDebt(name, phone, balance, date);
                } else {
                    Snackbar.make(view, "Fill in empty fields", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createDebt(String name, String phone, String balance, String date) {
        if (fAuth.getCurrentUser() != null) {
            final DatabaseReference newDebtRef = fDebtsDatabase.push();

            final Map debtMap = new HashMap();
            debtMap.put("name", name);
            debtMap.put("phone", phone);
            debtMap.put("balance", balance);
            debtMap.put("date", date);
            debtMap.put("timestamp", ServerValue.TIMESTAMP);
            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newDebtRef.setValue(debtMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task){
                            if(task.isSuccessful()) {
                                Toast.makeText(NewDebtActivity.this, "Note added to db", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NewDebtActivity.this, "ERROR:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            mainThread.start();

        } else {
            Toast.makeText(this, "USER IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }
    }
}
