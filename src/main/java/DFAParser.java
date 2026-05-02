import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//=>(q0)-eps->(q0)
//(q0)-a->(q1)
//(q0)-main->[q2]
//(q1)-main->[q2]
//(q1)-a->[q2]
//[q2]-a->[q2]
//[q2]-main->[q2]
//[q2]-eps->[q2]
//(q1)-eps->(q1)

public class DFAParser {

    private static final Pattern TRANSITION_PATTERN =
            Pattern.compile("^(=>)?([\\(\\[][^\\)\\]]+[\\)\\]])-([^\\-]+)->([\\(\\[][^\\)\\]]+[\\)\\]])$");

    public static DFA parseDFA(String filename) throws IOException {
        DFA dfa = new DFA();
        FileInputStream inputStream = new FileInputStream(filename);
        System.setIn(inputStream);
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            parseTransitionLine(line, dfa);
        }


        return dfa;

    }

    private static void parseTransitionLine(String line, DFA dfa) {
        Matcher matcher = TRANSITION_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid DFA transition line: " + line);
        }
        boolean isStart = matcher.group(1) != null;

        String fromToken = matcher.group(2);
        String label = matcher.group(3);
        String toToken = matcher.group(4);

        String fromState = cleanState(fromToken);
        String toState = cleanState(toToken);

        boolean fromAccepting = isAcceptingToken(fromToken);
        boolean toAccepting = isAcceptingToken(toToken);

        dfa.addState(fromState);
        dfa.addState(toState);

        if (isStart) {
            dfa.setStartState(fromState);
        }

        if (fromAccepting) {
            dfa.addAcceptingState(fromState);
        }

        if (toAccepting) {
            dfa.addAcceptingState(toState);
        }

        dfa.addTransition(fromState, label, toState);


    }

    private static String cleanState(String token) {
        return token.substring(1, token.length() - 1);
    }

    private static boolean isAcceptingToken(String token) {
        return token.startsWith("(") && token.endsWith(")");
    }
}
