package er.debttracker;

import java.util.Date;
import java.util.List;

public class Debts {

    private String debtID;
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

    public Debts(String DebtID, String DebtorName, String Phone, String Balance, String Credit, String InitialBalance, String DueDate, String FinalDueDate, String DateEntered, String IsCreditorOrDebtor) {
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

    public String getDebtID(){
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

    public void setDebtID(String DebtID) {
        debtID = DebtID;
    }

    public void setDebtorName(String DebtorName) {
        debtorName = DebtorName;
    }

    public void setPhone(String Phone) {
        phone = Phone;
    }

    public void setBalance(String Balance) { balance = Balance;   }

    public void setCredit(String Credit) { credit = Credit; }

    public void setInitialBalance(String InitialBalance) {
        initialBalance = InitialBalance;
    }

    public void setDueDate(String DueDate) {
        dueDate = DueDate;
    }

    public void setFinalDueDate(String FinalDueDate) {
        finalDueDate = FinalDueDate;
    }

    public void setDateEntered(String DateEntered) {
        dateEntered = DateEntered;
    }

    public void setIsCreditorOrDebtor(String IsCreditorOrDebtor) { isCreditorOrDebtor = IsCreditorOrDebtor;    }

    public String toString(){
        return "" + debtorName + "";
    }

}
