package edu.ntnu.g14.model;

public enum BudgetCategory {
  FOOD_AND_DRINK("e"),
  CLOTHES_AND_SHOES("e"),
  PERSONAL_CARE("e"),
  LEISURE("e"),
  TRAVEL("e"),
  ALCOHOL_AND_TOBACCO("e"),
  OTHER("e"),
  SALARY("r"),
  PAYMENT("r"),
  INCOME("r"),
  BUSINESS("r");

  private final String type;

  BudgetCategory(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
