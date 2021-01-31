package Project2;

import java.util.*;

public class AssociationRuleCalculator<type> {

    public List<AssociationRule<type>> generate(FrequencyData<type> data, double minimumConfidence) {
        Set<AssociationRule<type>> resultSet = new HashSet<>();

        for (Set<type> itemset : data.getAllItemSetList()) {
            if (itemset.size() < 2) {
                continue;
            }
            Set<AssociationRule<type>> basicAssociationRuleSet = generateAllBasicAssociationRules(itemset, data);
            generateAssociationRules(itemset, basicAssociationRuleSet, data, minimumConfidence, resultSet);
        }

        List<AssociationRule<type>> ret = new ArrayList<>(resultSet);
        ret.sort((a1, a2) -> Double.compare(a2.getConfidence(), a1.getConfidence()));
        return ret;
    }

    private void generateAssociationRules(Set<type> itemset,
                                          Set<AssociationRule<type>> ruleSet,
                                          FrequencyData<type> data,
                                          double minimumConfidence,
                                          Set<AssociationRule<type>> collector) {
        if (ruleSet.isEmpty()) {
            return;
        }

        int k = itemset.size();
        int m = ruleSet.iterator().next().getConsequent().size();
        if (k > m + 1) {
            Set<AssociationRule<type>> nextRules = moveOneItemToConsequents(itemset, ruleSet, data);
            Iterator<AssociationRule<type>> iterator = nextRules.iterator();

            while (iterator.hasNext()) {
                AssociationRule<type> rule = iterator.next();
                if (rule.getConfidence() >= minimumConfidence) {
                    collector.add(rule);
                } else {
                    iterator.remove();
                }
            }
            generateAssociationRules(itemset, nextRules, data, minimumConfidence, collector);
        }
    }

    private Set<AssociationRule<type>> moveOneItemToConsequents(Set<type> itemset,
                             Set<AssociationRule<type>> ruleSet,
                             FrequencyData<type> data) {
        Set<AssociationRule<type>> output = new HashSet<>();
        Set<type> antecedent = new HashSet<>();
        Set<type> consequent = new HashSet<>();
        double itemsetSupportCount = data.getSupportCountMap().get(itemset);

        for (AssociationRule<type> rule : ruleSet) {
            antecedent.clear();
            consequent.clear();
            antecedent.addAll(rule.getAntecedent());
            consequent.addAll(rule.getConsequent());

            for (type item : rule.getAntecedent()) {
                antecedent.remove(item);
                consequent.add(item);
                int antecedentSupportCount = data.getSupportCountMap().get(antecedent);
                AssociationRule<type> newRule = new AssociationRule<>(antecedent, consequent,
                                itemsetSupportCount / antecedentSupportCount);

                output.add(newRule);
                antecedent.add(item);
                consequent.remove(item);
            }
        }
        return output;
    }

    private Set<AssociationRule<type>>generateAllBasicAssociationRules(Set<type> itemset, FrequencyData<type> data){
        Set<AssociationRule<type>> basicAssociationRuleSet = new HashSet<>(itemset.size());
        Set<type> antecedent = new HashSet<>(itemset);
        Set<type> consequent = new HashSet<>(1);

        for (type item : itemset) {
            antecedent.remove(item);
            consequent.add(item);

            int itemsetSupportCount = data.getSupportCountMap().get(itemset);
            int antecedentSupportCount = data.getSupportCountMap().get(antecedent);

            double confidence = 1.0 * itemsetSupportCount / antecedentSupportCount;
            basicAssociationRuleSet.add(new AssociationRule(antecedent, consequent, confidence));
            antecedent.add(item);
            consequent.remove(item);
        }
        return basicAssociationRuleSet;
    }
}