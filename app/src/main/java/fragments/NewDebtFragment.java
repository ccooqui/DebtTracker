package fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import er.debttracker.GMailSender;
import er.debttracker.JSSEProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import er.debttracker.R;
import objects.Debts;

public class NewDebtFragment extends Fragment {

    // View & Context Setup
    private Context mContext;
    View v;

    //UI Elements
    private Button btnCreate, btnDueDate;
    private EditText etDebtorName, etPhone, etInitialBalance, etBalance;
    private TextView debtDueDate;
    String selectedDate;
    DatePickerDialog datePickerDialog;

    //Database Elements
    private FirebaseAuth fAuth;
    private DatabaseReference fDebtsDatabase;


    public static NewDebtFragment newInstance() {
        return new NewDebtFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_new_debt,
                container, false);
        mContext = v.getContext();

        // Setting up dropdown to allow a user to select a category for a coupon
        Spinner spinner = v.findViewById(R.id.categorySpinner);
        ArrayAdapter<String> categoriesArray = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.category_types));
        categoriesArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoriesArray);

        // Setting up buttons and views
        btnCreate = v.findViewById(R.id.btnCreate);
        btnDueDate = v.findViewById(R.id.btnDate);
        debtDueDate=v.findViewById(R.id.expiry_date);
        etDebtorName = v.findViewById(R.id.etDebtorName);
        etPhone = v.findViewById(R.id.etPhone);
        etBalance = v.findViewById(R.id.etBalance);
        etInitialBalance = v.findViewById(R.id.etInitialBalance);

        btnDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int year = Calendar.getInstance().get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int dpYear, int dpMonth , int dpDay) {
                        dpMonth = dpMonth + 1;
                        selectedDate = dpMonth + "/" + dpDay + "/" + dpYear;
                        debtDueDate.setText(selectedDate);
                        Log.d("NEW_DEBT_FRAG", " "+ selectedDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToList();
            }
        });

        fAuth=FirebaseAuth.getInstance();
        fDebtsDatabase = FirebaseDatabase.getInstance().getReference().child("Debts").child(fAuth.getCurrentUser().getUid());

    return v;
    }

    private void uploadToList(){
        // Gathers all of the text in each of the text views and spinner
        // to create a new Debt to add it to the database.
        etDebtorName = v.findViewById(R.id.etDebtorName);
        etPhone = v.findViewById(R.id.etPhone);
        etBalance = v.findViewById(R.id.etBalance);
        etInitialBalance = v.findViewById(R.id.etInitialBalance);
        debtDueDate = v.findViewById(R.id.expiry_date);
        Spinner categoryType = v.findViewById(R.id.categorySpinner);
        final String dDueDate = selectedDate;

        final String dName = etDebtorName.getText().toString();
        final String dPhone = etPhone.getText().toString();
        final String dBalance = etBalance.getText().toString();
        final String dInitialBalance = etInitialBalance.getText().toString();
        final String dCategory = categoryType.getSelectedItem().toString();
        Boolean categ;
        if(dCategory.equals("Debt") == true) categ = true;
        else categ = false;

        if(dName.trim().length() == 0 || dPhone.trim().length() == 0 || dBalance.trim().length() == 0 || dInitialBalance.trim().length() == 0 || selectedDate==null || dCategory.trim().length()==0){
            Log.d("NEW_DEBT_FRAG", "Not all fields are filled out");
            Toast.makeText(getContext(), "Fill out all required fields and choose a Due Date before adding the debt to the list", Toast.LENGTH_LONG).show();
        } else {

            Debts d = new Debts(dName, dPhone, dBalance, dInitialBalance, dDueDate, categ);
            createDebt(d);
            Toast.makeText(getContext(), "Debt added to your list", Toast.LENGTH_SHORT).show();
            clearEntries();

            final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        GMailSender sender = new GMailSender(
                                "recoupdebttracker@gmail.com",
                                "fillmore4020");
                        sender.sendMail("Debt Confirmation", "A new debt has been added to your account.\n\nDebtor Name: " + dName +
                                "\nPhone Number: " + dPhone + "\nCurrent Balance: " + dBalance + "\nInitial Balance: " + dInitialBalance +
                                        "\nDue Date: " + dDueDate + "\nCategory: " + dCategory, "recoupdebttracker@gmail.com", email);
                    } catch (Exception e) {
                        Toast.makeText(mContext.getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }
            }).start();
            Toast.makeText(getContext(), "Email confirmation sent", Toast.LENGTH_SHORT).show();
        }
    }

    private void createDebt(Debts debt) {
        if (fAuth.getCurrentUser() != null) {
            final DatabaseReference newDebtRef = fDebtsDatabase.push();

            final Map debtMap = new HashMap();
            debtMap.put("debt", debt);

            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newDebtRef.setValue(debtMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task){
                            if(task.isSuccessful()) {
                                Toast.makeText(getContext(), "Note added to db", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "ERROR:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            mainThread.start();

        } else {
            Toast.makeText(getContext(), "USER IS NOT SIGNED IN", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearEntries(){
        // Shows an alert dialog that asks the users if they want to clear all of the fields
        // They entered for a coupon. If they say yes, all the entries are reset. If they say no,
        // the dialog disappears and their entries remain the same.
        AlertDialog.Builder clearEntries = new AlertDialog.Builder(mContext);
        clearEntries.setMessage("Clear all entries?");
        clearEntries.setCancelable(true);

        clearEntries.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mContext, "Clearing Entries", Toast.LENGTH_SHORT).show();
                        etDebtorName = v.findViewById(R.id.etDebtorName);
                        etPhone = v.findViewById(R.id.etPhone);
                        etBalance = v.findViewById(R.id.etBalance);
                        etInitialBalance = v.findViewById(R.id.etInitialBalance);
                        debtDueDate = v.findViewById(R.id.expiry_date);
                        etDebtorName.setText(null);
                        etPhone.setText(null);
                        etBalance.setText(null);
                        etInitialBalance.setText(null);
                        debtDueDate.setText(null);
                    }
                });

        clearEntries.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = clearEntries.create();
        alertDialog.show();
    }

}
