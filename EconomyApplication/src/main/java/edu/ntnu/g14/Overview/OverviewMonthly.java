package edu.ntnu.g14.Overview;

import java.sql.Date;
import edu.ntnu.g14.*;

public class OverviewMonthly implements Overview {
    int totalIncome;
    int totalExpenses;
    Date month;
    int totalSavings;

    public OverviewMonthly(Date month){
        this.month = month;
        this.totalIncome =; //add
        this.totalExpenses = ; //add
        this.totalSavings = this.totalIncome - this.totalExpenses;
    }

    public Transaction allTransactions(){
        //return user.get(transactions(month))
    }
}
