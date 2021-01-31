package Project2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrequencyCalculator<type> {

    public FrequencyData<type> generate(List<Set<type>> transactionList, double minimumSupport) {
        Map<Set<type>, Integer> supportCountMap = new HashMap<>();
        List<Set<type>> singleItemsFrequency = getSingleItemsFrequency(transactionList, supportCountMap, minimumSupport);
        Map<Integer, List<Set<type>>> map = new HashMap<>();
        map.put(1, singleItemsFrequency);

        int k = 1;
        do {
            ++k;
            List<Set<type>> candidateList1 = findCandidates(map.get(k - 1));
            for (Set<type> transaction : transactionList) {
                List<Set<type>> candidateList2 = subset(candidateList1, transaction);
                for (Set<type> itemset : candidateList2) {
                    supportCountMap.put(itemset, supportCountMap.getOrDefault(itemset, 0) + 1);
                }
            }
            map.put(k, getNextItemSets(candidateList1, supportCountMap, minimumSupport,
                    transactionList.size()));
        } while (!map.get(k).isEmpty());

        return new FrequencyData<>(getAllItemSets(map), supportCountMap, minimumSupport, transactionList.size());
    }

    private List<Set<type>> getAllItemSets(Map<Integer, List<Set<type>>> map) {
        List<Set<type>> ret = new ArrayList<>();

        for (List<Set<type>> itemsetList : map.values()) {
            ret.addAll(itemsetList);
        }
        return ret;
    }

    private List<Set<type>> getNextItemSets(List<Set<type>> candidateList,
                                            Map<Set<type>, Integer> supportCountMap,
                                            double minimumSupport,
                                            int numberOfTransactions) {
        List<Set<type>> ret = new ArrayList<>(candidateList.size());

        for (Set<type> itemSet : candidateList) {
            if (supportCountMap.containsKey(itemSet)) {
                int supportCount = supportCountMap.get(itemSet);
                double support = 1.0 * supportCount / numberOfTransactions;

                if (support >= minimumSupport) {
                    ret.add(itemSet);
                }
            }
        }
        return ret;
    }

    private List<Set<type>> subset(List<Set<type>> candidateList, Set<type> transaction) {
        List<Set<type>> ret = new ArrayList<>(candidateList.size());

        for (Set<type> candidate : candidateList) {
            if (transaction.containsAll(candidate)) {
                ret.add(candidate);
            }
        }
        return ret;
    }

    private List<Set<type>> findCandidates(List<Set<type>> itemSetList) {
        List<List<type>> list = new ArrayList<>(itemSetList.size());

        for (Set<type> itemset : itemSetList) {
            List<type> l = new ArrayList<>(itemset);
            l.sort(ITEM_COMPARATOR);
            list.add(l);
        }

        int listSize = list.size();
        List<Set<type>> ret = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; ++i) {
            for (int j = i + 1; j < listSize; ++j) {
                Set<type> candidate = getMergeItemSets(list.get(i), list.get(j));

                if (candidate != null) {
                    ret.add(candidate);
                }
            }
        }
        return ret;
    }

    private static final Comparator ITEM_COMPARATOR = (o1, o2) -> ((Comparable) o1).compareTo(o2);

    private Set<type> getMergeItemSets(List<type> itemSet1, List<type> itemSet2) {
        int length = itemSet1.size();

        for (int i = 0; i < length - 1; ++i) {
            if (!itemSet1.get(i).equals(itemSet2.get(i))) {
                return null;
            }
        }

        if (itemSet1.get(length - 1).equals(itemSet2.get(length - 1))) {
            return null;
        }

        Set<type> ret = new HashSet<>(length + 1);
        ret.addAll(itemSet1);
        ret.add(itemSet2.get(length - 1));
        return ret;
    }

    private List<Set<type>> getSingleItemsFrequency(List<Set<type>> itemSetList,
                                                    Map<Set<type>, Integer> supportCountMap,
                                                    double minimumSupport) {
        Map<type, Integer> map = new HashMap<>();
        List<Set<type>> frequentItemSetList = new ArrayList<>();

        for (Set<type> itemSet : itemSetList) {
            for (type item : itemSet) {
                Set<type> tmp = new HashSet<>(1);
                tmp.add(item);
                supportCountMap.put(tmp, supportCountMap.getOrDefault(tmp, 0) + 1);
                map.put(item, map.getOrDefault(item, 0) + 1);
            }
        }

        for (Map.Entry<type, Integer> entry : map.entrySet()) {
            if (1.0 * entry.getValue() / itemSetList.size() >= minimumSupport) {
                Set<type> itemSet = new HashSet<>(1);
                itemSet.add(entry.getKey());
                frequentItemSetList.add(itemSet);
            }
        }
        return frequentItemSetList;
    }
}