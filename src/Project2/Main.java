package Project2;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        process();
    }

    private static void process() {
        List<Transaction> receipts = getDataFromCSV("Groceries_dataset.csv");
        QuickSort qs = new QuickSort();
        qs.quickSort(receipts);

        // Covert the Groceries dataset into a transaction dataset
        List<Transaction> transactions = generateTransaction(receipts);
        List<Set<String>> itemSetList = new ArrayList<>();
        for(Transaction transaction : transactions){
            itemSetList.add(transaction.getItems());
        }

        //Frequent ItemSets
        double minimumSupport = 0.001;
        FrequencyData<String> data = new FrequencyCalculator<String>().generate(itemSetList, minimumSupport);
        createFrequencyFile(data, minimumSupport);

        //Association Rule
        double minimumConfidence = 0.0001;
        List<AssociationRule<String>> associationRuleList = new AssociationRuleCalculator<String>().generate(data, minimumConfidence);
        createAssociationRuleFile(associationRuleList, minimumConfidence);
    }

    private static List<Transaction> generateTransaction(List<Transaction> receipts) {
        List<Transaction> transactions = new ArrayList<>();
        int i = 0;
        while(i < receipts.size()){
            List<Transaction> group = new ArrayList<>();
            group.add(receipts.get(i));
            for(int j = i+1; j < receipts.size(); j++){
                if(receipts.get(i).getID().equals(receipts.get(j).getID())){
                    group.add(receipts.get(j));
                }
            }

            Map<String, Set<String>> extractGroup = new HashMap<>();
            for(Transaction ok : group){
                if(!extractGroup.containsKey(ok.getDay())){
                    extractGroup.put(ok.getDay(),ok.getItems());
                } else{
                    extractGroup.replace(ok.getDay(), mergeSet(extractGroup.get(ok.getDay()), ok.getItems()));
                }
            }

            for(Map.Entry<String, Set<String>> itemSet : extractGroup.entrySet()){
                transactions.add(new Transaction(receipts.get(i).getID(), itemSet.getKey(), itemSet.getValue()));
            }
            i = i + group.size();
        }
        return transactions;
    }

    private static Set<String> mergeSet(Set<String> set1, Set<String>set2) {
        return new HashSet<>() {
            {
                addAll(set1);
                addAll(set2);
            } };
    }

    private static void createFrequencyFile(FrequencyData<String> data, double minimumSupport){
        PrintWriter out = null;
        try {
            out = new PrintWriter("support.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert out != null;
        out.println("--- Frequent itemSets ---");
        out.println("--- Minimum Support: " + minimumSupport + " ---");
        int i = 1;
        for (Set<String> itemset : data.getAllItemSetList()) {
            out.println(i++ + ". " + itemset + ", support: " + data.getSupport(itemset));
        }
        out.close();
    }

    private static void createAssociationRuleFile(List<AssociationRule<String>> associationRuleList, double minimumConfidence) {
        PrintWriter out = null;
        try {
            out = new PrintWriter("associationRule.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert out != null;
        out.println("--- Association rules ---");
        out.println("--- Minimum Confidence: " + minimumConfidence + " ---");
        int i = 1;
        for (AssociationRule<String> rule : associationRuleList) {
            out.println(i++ + ". " + rule.toString());
        }
        out.close();
    }

    private static List<Transaction> getDataFromCSV(String fileName){
        List<Transaction> transactions = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            reader.readLine(); // Skip the first line (Title)
            String line = reader.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Transaction newTransaction = createTransaction(attributes);
                transactions.add(newTransaction);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private static Transaction createTransaction(String[] attributes) {
        String id = attributes[0];
        String dateOfPurchase = attributes[1];
        Set<String> items = new HashSet<>();
        items.add(attributes[2]);
        return new Transaction(id, dateOfPurchase, items);
    }

}
