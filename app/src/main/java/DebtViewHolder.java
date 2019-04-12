import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import er.debttracker.R;

public class DebtViewHolder extends RecyclerView.ViewHolder {

    View mView;
    TextView tvDebtorName, tvBalance;
    public DebtViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        tvDebtorName = mView.findViewById(R.id.tvDebtorName);
        tvBalance = mView.findViewById(R.id.tvBalance);
    }

    public void setDebtorName(String debtorName) {
        tvDebtorName.setText(debtorName);
    }
    public void setBalance(String balance) {
        tvBalance.setText(balance);
    }
    /*public void setDateEntered(String dateEntered);
    public void setFinalDueDate(String finalDueDate);
    public void setInitialBalance(String initialBalance);
    public void setPhone(String phone);*/
}
