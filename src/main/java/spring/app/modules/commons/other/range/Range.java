package spring.app.modules.commons.other.range;


public abstract class Range<T> {

    protected final TypeComparison type;
    protected T lower;
    protected T upper;

    public enum TypeComparison {
        STRICT, NON_STRICT
    }

    protected Range(TypeComparison type, T lower, T upper) {
        if (type == null) {
            type = TypeComparison.NON_STRICT;
        }
        this.type = type;
        this.lower = lower;
        this.upper = upper;
    }

    protected Range(TypeComparison type) {
        if (type == null) {
            type = TypeComparison.NON_STRICT;
        }
        this.type = type;
    }

    public abstract boolean isInBounds(T value);

    public abstract T[] inBetween();
}
