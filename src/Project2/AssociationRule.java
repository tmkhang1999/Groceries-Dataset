package Project2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AssociationRule<type> {
    private final Set<type> antecedent = new HashSet<>();
    private final Set<type> consequent = new HashSet<>();
    private final double confidence;

    public AssociationRule(Set<type> antecedent, Set<type> consequent, double confidence) {
        this.antecedent.addAll(antecedent);
        this.consequent.addAll(consequent);
        this.confidence = confidence;
    }

    public Set<type> getAntecedent() {
        return Collections.unmodifiableSet(antecedent);
    }

    public Set<type> getConsequent() {
        return Collections.unmodifiableSet(consequent);
    }

    public double getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        return Arrays.toString(antecedent.toArray()) + " -> " + Arrays.toString(consequent.toArray()) + ": " +
                confidence;
    }
}

