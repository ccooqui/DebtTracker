package er.debttracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class NewDebtFragment extends Fragment {

    // View & Context Setup
    private Context mContext;
    View v;

    //UI Elements
    private Button btnCreate, btnDueDate;
    private EditText etDebtorName, etPhone, etInitialBalance, etBalance;
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

        

    return v;
    }


}
