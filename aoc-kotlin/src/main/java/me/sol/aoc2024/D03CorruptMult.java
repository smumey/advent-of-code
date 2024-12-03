package me.sol.aoc2024;

import me.sol.Utility;

import java.io.IOException;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public class D03CorruptMult {

    private final List<String> lines;

    D03CorruptMult(List<String> lines) {
        this.lines = lines;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new D03CorruptMult(Utility.readInput(D03CorruptMult.class, D03CorruptMult::parse)).sumProducts());
    }

    static List<String> parse(Stream<String> lines) {
        return lines.toList();
    }

    long sumProducts() {

        return lines.stream()
                .map(l -> l.chars()
                        .peek(c -> System.out.printf("char=%c%n", c))
                        .boxed()
                        .reduce(
                                ProductState.INITIAL_STATE,
                                ProductState::apply,
                                (s1, s2) -> {
                                    throw new IllegalStateException();
                                }
                        )
                )
                .mapToLong(ProductState::sum)
                .sum();
    }

    enum ParseStep {
        OPEN, M, U, L, M1_BEGIN, M1_DIGITS, M2_BEGIN, M2_DIGITS, M2_END
    }

    record ParseState(ParseStep step, String digits1, String digits2) {
        public static final ParseState OPEN_STATE = new ParseState(ParseStep.OPEN, "", "");
        public static final ParseState M_STATE = new ParseState(ParseStep.M, "", "");
        public static final ParseState U_STATE = new ParseState(ParseStep.U, "", "");
        public static final ParseState L_STATE = new ParseState(ParseStep.L, "", "");
        public static final ParseState M1_BEGIN_STATE = new ParseState(ParseStep.M1_BEGIN, "", "");

        ParseState transition(int c) {
            if (c == 'm') {
                return M_STATE;
            }
            return switch (this.step()) {
                case ParseStep.OPEN, M2_END -> OPEN_STATE;
                case M -> c == 'u' ? U_STATE : OPEN_STATE;
                case U -> c == 'l' ? L_STATE : OPEN_STATE;
                case L -> c == '(' ? M1_BEGIN_STATE : OPEN_STATE;
                case M1_BEGIN ->
                        Character.isDigit(c) ? new ParseState(ParseStep.M1_DIGITS, String.valueOf((char) c), "") : OPEN_STATE;
                case M1_DIGITS -> {
                    if (Character.isDigit(c) && digits1().length() < 3) {
                        yield new ParseState(ParseStep.M1_DIGITS, digits1 + (char) c, "");
                    } else {
                        yield c == ',' ? new ParseState(ParseStep.M2_BEGIN, digits1, digits2) : OPEN_STATE;
                    }
                }
                case M2_BEGIN ->
                        Character.isDigit(c) ? new ParseState(ParseStep.M2_DIGITS, digits1, String.valueOf((char) c)) : OPEN_STATE;
                case M2_DIGITS -> {
                    if (Character.isDigit(c) && digits2().length() < 3) {
                        yield new ParseState(ParseStep.M2_DIGITS, digits1, digits2 + (char) c);
                    } else {
                        yield c == ')' ? new ParseState(ParseStep.M2_END, digits1, digits2) : OPEN_STATE;
                    }
                }
            };
        }

        long product() {
            return Long.parseLong(digits1) * Long.parseLong(digits2);
        }
    }

    record ProductState(long sum, ParseState parseState) implements IntFunction<ProductState> {
        static final ProductState INITIAL_STATE = new ProductState(0L, ParseState.OPEN_STATE);

        @Override
        public ProductState apply(int c) {
            var newParseState = parseState.transition(c);
            System.out.printf("state=%s c=%c newParseState=%s%n", this, c, newParseState);
            if (newParseState.step == ParseStep.M2_END) {
                return new ProductState(sum + newParseState.product(), newParseState);
            } else {
                return new ProductState(sum, newParseState);
            }
        }
    }
}
