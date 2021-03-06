package fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import er.debttracker.R;
import objects.Debts;
import objects.Users;

import static java.lang.Float.parseFloat;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder>{
    // View & Context
    private View v;
    private Context mContext;

    // Data
    private List<Debts> mData;
    private FirebaseUser loggedInUserFB;
    private DatabaseReference fDebtsDatabase;

    // Dialogs
    private Dialog debtDialog;
    private Dialog editDialog;
    private Dialog paymentDialog;

    // Accounts
    private Users loggedInUser;

    public RecycleViewAdapter() { }

    public RecycleViewAdapter(Context mContext, List<Debts> mData, int spinner) {
        this.mContext = mContext;
        this.mData = mData;

        if (spinner == 0) {
            // do nothing
        }
        else if (spinner == 1) { // sort by Name
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

                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    String dateString1 = example1.getFinalDueDate();
                    String dateString2 = example2.getFinalDueDate();

                    try {
                        Date date1 = formatter.parse(dateString1);
                        Date date2 = formatter.parse(dateString2);

                        return (date1).compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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

                final Button paymentButton = debtDialog.findViewById(R.id.call_dialog_payment);
                paymentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        debtDialog.hide();
                        paymentDialog = new Dialog(mContext);
                        paymentDialog.setContentView(R.layout.payment_dialog);
                        paymentDialog.show();

                        Button button = (Button) paymentDialog.findViewById(R.id.btnPayment);

                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                EditText payment = (EditText) paymentDialog.findViewById(R.id.etPayment);

                                String Uid = loggedInUserFB.getUid();
                                Debts current_debt;
                                current_debt = mData.get(viewHolder.getAdapterPosition());
                                fDebtsDatabase = FirebaseDatabase.getInstance().getReference().child("Debts").child(Uid).child(current_debt.getDebtID()).child("debt");
                                Float newBalance = parseFloat(current_debt.getBalance()) + parseFloat(payment.getText().toString());
                                fDebtsDatabase.child("balance").setValue(newBalance.toString());
                                paymentDialog.dismiss();
                                Toast.makeText(mContext, "Payment Made", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                Button editButton = debtDialog.findViewById(R.id.edit_btn);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        debtDialog.hide();
                        editDialog = new Dialog(mContext);
                        editDialog.setContentView(R.layout.edit_dialog);
                        editDialog.show();

                        Button button = (Button) editDialog.findViewById(R.id.btnEdit);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                EditText etName = (EditText) editDialog.findViewById(R.id.etEditName);
                                EditText etBalance = (EditText) editDialog.findViewById(R.id.etEditBalance);
                                EditText etDate = (EditText) editDialog.findViewById(R.id.etEditDate);
                                EditText etNote = (EditText) editDialog.findViewById(R.id.etEditNotes);

                                String Uid = loggedInUserFB.getUid();
                                Debts current_debt;
                                current_debt = mData.get(viewHolder.getAdapterPosition());
                                fDebtsDatabase = FirebaseDatabase.getInstance().getReference().child("Debts").child(Uid).child(current_debt.getDebtID()).child("debt");

                                String sName = etName.getText().toString();
                                String sBalance = etBalance.getText().toString();
                                String sDate = etDate.getText().toString();
                                String sNote = etNote.getText().toString();

                                if (!TextUtils.isEmpty(sName)) {
                                    fDebtsDatabase.child("debtorName").setValue(sName);
                                }
                                if (!TextUtils.isEmpty(sBalance)) {
                                    fDebtsDatabase.child("initialBalance").setValue(sBalance);
                                }
                                if (!TextUtils.isEmpty(sDate)) {
                                    fDebtsDatabase.child("finalDueDate").setValue(sDate);
                                }
                                if (!TextUtils.isEmpty(sNote)) {
                                    fDebtsDatabase.child("debtNotes").setValue(sNote);
                                }
                                editDialog.dismiss();
                                Toast.makeText(mContext, "Debt Edited", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                Button deleteButton = debtDialog.findViewById(R.id.delete_btn);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Uid = loggedInUserFB.getUid();
                        Debts current_debt;
                        current_debt = mData.get(viewHolder.getAdapterPosition());
                        fDebtsDatabase = FirebaseDatabase.getInstance().getReference().child("Debts").child(Uid).child(current_debt.getDebtID());
                        fDebtsDatabase.removeValue();
                        mData.remove(viewHolder.getAdapterPosition());
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

