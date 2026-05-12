import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class MyApplication {


    public static void main(String[] args) throws Exception {

        String cfgFile = args.length > 0 ? args[0] : "src/test/java/testcases/Simple/simple.cfg";
        String specFile = args.length > 1 ? args[1] : "src/test/java/testcases/Simple/simple.spec";

        FlowGraph flowGraph = FlowGraphParser.parseFlowGraph(cfgFile);
        DFA dfa = DFAParser.parseDFA(specFile);

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

        ProductCFG productCFG = ProductConstructor.constructProductCFG(flowGraph, dfa);
        productCFG.print();
        
        //Chech if the product CFG is empty and print the counterexample if it is not
        List<String> counterExample = productCFG.getCounterexample();
        if (counterExample == null) {
            System.out.println("Product CFG language is empty.\n");
        } else {
            System.out.println("Product CFG is not empty. Counterexample:");
            System.out.println(String.join(" ", counterExample));
            System.out.println();
        }


    }


}
