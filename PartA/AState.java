package algorithms.search;

import java.util.Objects;

/**
 * this abstract class will represent a place in a problem (state)
 * stateName - the name of the state
 * cost - how much it cost to get to this state from the start state of the problem
 * parent - from which state this state reveled
 */
public abstract class AState {
    private String stateName;
    private double cost;
    private AState parent;

    public AState(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AState aState = (AState) o;
        return stateName.equals(aState.stateName);
    }


    @Override
    public int hashCode() {
        return Objects.hash(stateName);
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setParent(AState parent) {
        this.parent = parent;
    }

    public double getCost() {
        return cost;
    }

    public AState getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return stateName;
    }
}
