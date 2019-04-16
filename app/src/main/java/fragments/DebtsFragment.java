package fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import er.debttracker.R;
import objects.Debts;


public class DebtsFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static List<Debts> debtsList = new ArrayList<>();
    private RecycleViewAdapter recycleViewAdapter;
    View v;

    // Database retrieval
    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;

    public DebtsFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             Bundle savedInstanceState) {

        debtsList.clear();

        if(v == null) {
            v = inflater.inflate(R.layout.fragment_debts, container, false);

            setHasOptionsMenu(true);

            fAuth=FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Debts").child(fAuth.getCurrentUser().getUid());
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Retrieving debts from Firebase to display in RecycleView
                    for(DataSnapshot debtsSnapshot : dataSnapshot.getChildren()){
                        Debts debt = debtsSnapshot.getValue(Debts.class);
                        debtsList.add(debt);
                    }


                    // Display nothing
                    if(debtsList.size() == 0){
                        Toast.makeText(getContext(), "No debts in the list. Go to the add debts tab to add some debts to your list!", Toast.LENGTH_LONG).show();
                    } else {
                        getRecycleView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }


        return v;
    }

    public void getRecycleView(){
        // Creating a view of the fragment_debts .xml layout.
        // Generates the icons and information taken from the debts list and displays each item
        // in 2 columns and then returns the view
        RecyclerView myRecyclerView = v.findViewById(R.id.debts_recycler_view);
        recycleViewAdapter = new RecycleViewAdapter(getContext(), debtsList);
        myRecyclerView.setAdapter(recycleViewAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        myRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}