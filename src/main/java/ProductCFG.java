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

    public List<String> getCounterexample() {
        Map<String, Production> witness = new HashMap<>();
        Set<String> generating = new HashSet<>();

        boolean changed = true;
        while (changed) {
            changed = false;
            for (Production p : productions) {
                String left = p.getLeft();
                if (generating.contains(left)) continue;

                boolean allGood = true;
                for (String sym : p.getRight()) {
                    if (isVariable(sym)) {
                        if (!generating.contains(sym)) {
                            allGood = false;
                            break;
                        }
                    } // terminals and "eps" are fine
                }

                if (allGood) {
                    generating.add(left);
                    witness.put(left, p);
                    changed = true;
                }
            }
        }

        if (!generating.contains(startSymbol)) {
            return null; // empty language
        }

        return buildFromVariable(startSymbol, witness);
    }

    private List<String> buildFromVariable(String var, Map<String, Production> witness) {
        Production p = witness.get(var);
        if (p == null) return new ArrayList<>();
        List<String> out = new ArrayList<>();
        for (String sym : p.getRight()) {
            if (isVariable(sym)) {
                out.addAll(buildFromVariable(sym, witness));
            } else {
                if (!sym.equals("eps")) out.add(sym);
            }
        }
        return out;
    }
}
