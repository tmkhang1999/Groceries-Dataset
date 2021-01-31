package Project2;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrequencyData<type> {

    private final List<Set<type>> allItemSetList;
    private final Map<Set<type>, Integer> supportCountMap;
    private final double minimumSupport;
    private final int numberOfTransactions;

    FrequencyData(List<Set<type>> frequentItemsetList,
                  Map<Set<type>, Integer> supportCountMap,
                  double minimumSupport,
                  int transactionNumber) {
        this.allItemSetList = frequentItemsetList;
        this.supportCountMap = supportCountMap;
        this.minimumSupport = minimumSupport;
        this.numberOfTransactions = transactionNumber;
    }

    public List<Set<type>> getAllItemSetList() {
        return allItemSetList;
    }

    public Map<Set<type>, Integer> getSupportCountMap() {
        return supportCountMap;
    }

    public double getSupport(Set<type> itemset) {
        return (double) supportCountMap.get(itemset) / numberOfTransactions;
    }
}