package fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import er.debttracker.R;
import objects.Debts;
import objects.Users;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder>{
    // View & Context
    private View v;
    private Context mContext;

    // Data
    private List<Debts> mData;
    private FirebaseUser loggedInUserFB;

    // Dialogs
    private Dialog debtDialog;

    // Accounts
    private Users loggedInUser;


    public RecycleViewAdapter() { }

    public RecycleViewAdapter(Context mContext, List<Debts> mData, int spinner) {
        this.mContext = mContext;
        this.mData = mData;

        if (spinner == 0) {
            // do nothing
        }
        else if (spinner == 1) { // sort by name
            Collections.sort(mData, new Comparator<Debts>() {
                @Override
                public int compare(Debts example1, Debts example2) {

                    //For Ascending Order
                    return example1.getDebtorName().compareTo(example2.getDebtorName());
                }
            });
        }
        else if (spinner == 2) { // sort by Due Date

            Collections.sort(mData, new Comparator<Debts>() {
                @Override
                public int compare(Debts example1, Debts example2) {

                    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                    String dateString1 = example1.getFinalDueDate();
                    String dateString2 = example2.getFinalDueDate();

                    try {
                        Date date1 = formatter.parse(dateString1);
                        Date date2 = formatter.parse(dateString2);

                        return (date1).compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //For Ascending Order
                    //return (date1).compareTo(date2);

                    return example1.getFinalDueDate().compareTo(example2.getFinalDueDate());
                }
            });
        }
        else if (spinner == 3) { // sort by Amount Owed
            Collections.sort(mData, new Comparator<Debts>() {
                @Override
                public int compare(Debts example1, Debts example2) {

                    //For Ascending Order
                    return (int) (example1.calculateBalance() - example2.calculateBalance());
                }
            });
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // For mini dialog that is displayed when a debt card item is clicked

        // Setting up user account
        FirebaseAuth auth = FirebaseAuth.getInstance();
        loggedInUserFB = FirebaseAuth.getInstance().getCurrentUser();
        assert loggedInUserFB != null;


        v = LayoutInflater.from(mContext).inflate(R.layout.debt_cardview, viewGroup,false);
        final myViewHolder viewHolder = new myViewHolder(v);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                //Initialize the dialog
                debtDialog = new Dialog(mContext);
                debtDialog.setContentView(R.layout.debt_dialog);

                debtDialog.show();

                Button callButton = debtDialog.findViewById(R.id.call_dialog_btn);
                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = mData.get(viewHolder.getAdapterPosition()).getPhone();

                        if(phone == null){
                            Toast.makeText(mContext, "Phone number not available", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Make a call", Toast.LENGTH_SHORT).show();
                            openCall(phone);
                        }
                    }
                });

                Button editButton = debtDialog.findViewById(R.id.edit_btn);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, final int i) {

            myViewHolder.tv_name.setText(mData.get(i).getDebtorName());
            myViewHolder.tv_balance.setText(mData.get(i).getBalance());
            myViewHolder.tv_initialBalance.setText(mData.get(i).getInitialBalance());
            myViewHolder.tv_dueDate.setText(mData.get(i).getFinalDueDate());
            myViewHolder.tv_dateCreated.setText(mData.get(i).getDateEntered());
            myViewHolder.tv_debtNotes.setText(mData.get(i).getDebtNotes());

            myViewHolder.debt_progress.setProgress(mData.get(i).calculateBalancePercent());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{

        // Gets the id names of all components to the cardView items
        private TextView tv_name;
        private TextView tv_balance;
        private TextView tv_initialBalance;
        private TextView tv_dueDate;
        private TextView tv_dateCreated;
        private TextView tv_debtNotes;
        private DonutProgress debt_progress;

        private CardView cardView;

        myViewHolder(View v){
            super(v);

            tv_name = itemView.findViewById(R.id.tvDebtorName);
            tv_balance = itemView.findViewById(R.id.tvBalance);
            tv_initialBalance = itemView.findViewById(R.id.tvInitialBalance);
            tv_dueDate =  itemView.findViewById(R.id.tvDueDate);
            tv_dateCreated =  itemView.findViewById(R.id.tvDateCreated);
            tv_debtNotes =  itemView.findViewById(R.id.tvDebtNotes);
            debt_progress = itemView.findViewById(R.id.debt_progress);
            cardView = itemView.findViewById(R.id.id_Card);
        }
    }

    private  void openCall(final String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        mContext.startActivity(intent);
    }


}

