import java.util.HashSet;
import java.util.Set;

public class MyApplication {


    public static void main(String[] args) throws Exception {


        FlowGraph flowGraph = FlowGraphParser.parseFlowGraph("/Users/coreyporter/Desktop/KTH/Automata/AutomataLab2/src/test/java/testcases/Simple/simple.cfg");
        DFA dfa = DFAParser.parseDFA("/Users/coreyporter/Desktop/KTH/Automata/AutomataLab2/src/test/java/testcases/Simple/simple.spec");

        //Populate the alphabet
        Set<String> fullAlphabet = new HashSet<>();
        fullAlphabet.addAll(dfa.getAlphabet());
        fullAlphabet.addAll(flowGraph.getMethods());
        fullAlphabet.addAll(flowGraph.getEdgeLabels());
        fullAlphabet.add("eps");
        dfa.complete(fullAlphabet);
        dfa.complement();

        flowGraph.print();
        dfa.print();


    }


}
