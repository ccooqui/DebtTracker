package objects;

import java.util.List;

public class Users {

    private String userID;
    private String fName;
    private String lName;
    private String username;
    private String password;
    private List<Debts> dList;

    public Users(){}

    public Users(String usrID, String name, String lastName, String userName, String pass, List<Debts> debtsList) {
        this.userID = usrID;
        this.fName = name;
        this.lName = lastName;
        this.username = userName;
        this.password = pass;
        this.dList = debtsList;
    }

    public String getUserID(){
        return userID;
    }

    public String getfName(){
        return fName;
    }

    public String getlName(){
        return lName;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public List<Debts> getdList(){return dList;}


    //Setter Functions

    public void setUserID(String UserID){userID = UserID;}
    public void setFName(String FName){fName = FName;}
    public void setLName(String LName){lName = LName;}
    public void setUsername(String Username){username= Username;}
    public void setPassword(String Password){password = Password;}
    public void setdList(List<Debts> DebtsList){dList = DebtsList;}


    public String toString(){
        return "" + fName + " " + lName + "";
    }

}
