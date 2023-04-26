package edu.ntnu.g14.Overview;

import edu.ntnu.g14.Transaction;
import java.sql.Date;

public class OverviewMonthly implements Overview {

  int totalIncome;
  int totalExpenses;
  Date month;
  int totalSavings;

  public OverviewMonthly(Date month) {
    this.month = month;
    this.totalIncome = 0; //add
    this.totalExpenses = 0; //add
    this.totalSavings = this.totalIncome - this.totalExpenses;
  }

  public Transaction allTransactions() {
    //return user.get(transactions(month))
    return null;
  }
}
