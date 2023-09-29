package spring.app.modules.commons.other.range;

import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class IntRange extends Range<Integer> {

    private IntRange(int lower, int upper) {
        super(null, lower, upper);
    }

    public IntRange(int lower, int upper, @Nullable TypeComparison type) {
        super(type, lower, upper);
    }

    private IntRange(int value, boolean isLower) {
        super(null);
        if (isLower) {
            this.lower = value;
            this.upper = Integer.MAX_VALUE;
        } else {
            this.upper = value;
            this.lower = Integer.MIN_VALUE;
        }
    }

    @Override
    public boolean isInBounds(Integer value) {
        return switch (type) {
            case STRICT -> value > lower && value < upper;
            case NON_STRICT -> value >= lower && value <= upper;
        };
    }

    @Override
    public Integer[] inBetween() {
        Integer[] res = switch (type) {
            case STRICT -> new Integer[upper - lower - 1];
            case NON_STRICT -> new Integer[upper - lower + 1];
        };
        for (int i = 0; i < res.length; i++) {
            res[i] = lower + i;
        }
        return res;
    }

    public static IntRange of(int lower, int upper) {
        return new IntRange(lower, upper);
    }

    public static IntRange of(int value, boolean isLower) {
        return new IntRange(value, isLower);
    }

    public static IntRange of(int lower, int upper, @Nullable TypeComparison type) {
        return new IntRange(lower, upper, type);
    }
}
