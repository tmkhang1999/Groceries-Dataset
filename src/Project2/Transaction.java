package Project2;

import java.util.Set;

public class Transaction {
    private final String ID;
    private final String dateOfPurchase;
    private final Set<String> items;

    public String getID() {return ID;}

    public String getDay() {return dateOfPurchase;}

    public Set<String> getItems() {return items;}

    public String toString() {
        return "Transaction [ID: " + ID + ", Day: " + dateOfPurchase + ", items: " + items + "]";
    }

    public Transaction(String ID, String dateOfPurchase, Set<String> items){
        this.ID = ID;
        this.dateOfPurchase = dateOfPurchase;
        this.items = items;
    }
}
