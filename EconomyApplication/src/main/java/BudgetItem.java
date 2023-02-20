public class BudgetItem{
    short amount;
    String description;
    ExpenditureCategory category;

    public BudgetItem(short amount, String description, ExpenditureCategory category){
        this.amount = amount;
        this.description = description;
        this.category = category;
        //TODO: add if
    }
}