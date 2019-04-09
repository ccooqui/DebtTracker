package er.debttracker;

import java.util.Date;
import java.util.List;

public class Debts {

    private long debtID;
    private String debtorName;
    private String phone;
    private String balance;
    private String credit;
    private String initialBalance;
    private String dueDate;
    private String finalDueDate;
    private String dateEntered;
    private String isCreditorOrDebtor;

    public Debts(){}

    public Debts(long DebtID, String DebtorName, String Phone, String Balance, String Credit, String InitialBalance, String DueDate, String FinalDueDate, String DateEntered, String IsCreditorOrDebtor) {
        this.debtID = DebtID;
        this.debtorName = DebtorName;
        this.phone = Phone;
        this.balance = Balance;
        this.credit = Credit;
        this.initialBalance = InitialBalance;
        this.dueDate = DueDate;
        this.finalDueDate = FinalDueDate;
        this.dateEntered = DateEntered;
        this.isCreditorOrDebtor = IsCreditorOrDebtor;
    }

    //Getter Functions

    public long getDebtID(){
        return debtID;
    }

    public String getDebtorName(){
        return debtorName;
    }

    public String getPhone(){
        return phone;
    }

    public String getBalance() {
        return balance;
    }

    public String getCredit(){ return credit; }

    public String getInitialBalance(){
        return initialBalance;
    }

    public String getDueDate(){ return dueDate; }

    public String getFinalDueDate(){ return finalDueDate; }

    public String getDateEntered(){ return dateEntered; }

    public String getIsCreditorOrDebtor(){ return isCreditorOrDebtor; }


    //Setter Functions

    public void setDebtID(long DebtID) {
        DebtID = debtID;
    }

    public void setDebtorName(String DebtorName) {
        DebtorName = debtorName;
    }

    public void setPhone(String Phone) {
        Phone = phone;
    }

    public void setBalance(String Balance) { Balance = balance;   }

    public void setCredit(String Credit) { Credit = credit; }

    public void setInitialBalance(String InitialBalance) {
        InitialBalance = initialBalance;
    }

    public void setDueDate(String DueDate) {
        DueDate = dueDate;
    }

    public void setFinalDueDate(String FinalDueDate) {
        FinalDueDate = finalDueDate;
    }

    public void setDateEntered(String DateEntered) {
        DateEntered = dateEntered;
    }

    public void setIsCreditorOrDebtor(String IsCreditorOrDebtor) { IsCreditorOrDebtor = isCreditorOrDebtor;    }

    public String toString(){
        return "" + debtorName + "";
    }

}
