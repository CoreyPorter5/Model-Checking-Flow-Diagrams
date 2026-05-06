import java.util.*;

public class ProductCFG {
    private String startSymbol;
    private Set<String> variables;
    private Set<String> terminals;
    private List<Production> productions;

    public ProductCFG() {
        this.startSymbol = "S";
        this.variables = new HashSet<>();
        this.terminals = new HashSet<>();
        this.productions = new ArrayList<>();
        this.variables.add(startSymbol);
    }

    public void addProduction(String left, List<String> right) {
        variables.add(left);
        productions.add(new Production(left, right));

        for (String symbol : right) {
            if (isVariable(symbol)) {
                variables.add(symbol);
            } else if (!symbol.equals("eps")) {
                terminals.add(symbol);
            }
        }
    }

    private boolean isVariable(String symbol) {
        return symbol.equals("S") || symbol.startsWith("[");
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public Set<String> getVariables() {
        return variables;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void print() {
        System.out.println("-----------PRODUCT CFG-----------");

        for (Production p : productions) {
            System.out.println(p);
        }
    }
}
