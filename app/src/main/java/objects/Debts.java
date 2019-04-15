package objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Debts {

    private String debtID;
    private String debtorName;
    private String phone;
    private String balance;
    private String initialBalance;
    private String dueDate;
    private String finalDueDate;
    private String dateEntered;
    private Boolean isCreditorOrDebtor;

    public Debts(){}

    public Debts(String DebtorName, String Phone, String Balance, String InitialBalance, String FinalDueDate, Boolean isCreditorOrDebtor) {
        this.debtorName = DebtorName;
        this.phone = Phone;
        this.balance = Balance;
        this.initialBalance = InitialBalance;
        this.finalDueDate = FinalDueDate;
        this.isCreditorOrDebtor = isCreditorOrDebtor;

        //generate the date of when the debt was created
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        this.dateEntered = date;
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

    public String getInitialBalance(){
        return initialBalance;
    }

    public String getDueDate(){ return dueDate; }

    public String getFinalDueDate(){ return finalDueDate; }

    public String getDateEntered(){ return dateEntered; }

    public Boolean getIsCreditorOrDebtor(){ return isCreditorOrDebtor; }


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

    public void setIsCreditorOrDebtor(Boolean IsCreditorOrDebtor) { isCreditorOrDebtor = IsCreditorOrDebtor;    }

    public String toString(){
        return "" + debtorName + "";
    }

    public Float calculateBalance() {
        return Float.parseFloat(this.initialBalance) - Float.parseFloat(this.balance);
    }

}
