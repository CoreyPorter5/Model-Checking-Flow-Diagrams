import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFA {

    private Set<String> states;
    private Set<String> acceptingStates;
    private String startState;
    private Map<String, Map<String, String>> transitions;
    private Set<String> alphabet;

    public DFA(){
        states = new HashSet<>();
        acceptingStates = new HashSet<>();
        transitions = new HashMap<>();
        alphabet = new HashSet<>();
        startState = "";
    }

    public void addState(String state){
        states.add(state);

    }

    public void addAcceptingState(String state){
        acceptingStates.add(state);
        states.add(state);

    }

    public void setStartState(String startState){
        this.startState = startState;

    }

    public void addTransition(String fromState, String label, String toState){
        transitions.putIfAbsent(fromState, new HashMap<>());
        transitions.get(fromState).putIfAbsent(label, toState);
        alphabet.add(label);

    }

    public void complete(Set<String> fullAlphabet){
        //If a transition is missing, go to SINK.
        //SINK loops to itself on every label.
        //SINK is non-accepting unless later complement changes it.
        String sink = "SINK";
        boolean needsSink = false;

        for(String state : new HashSet<>(states)){
            transitions.putIfAbsent(state, new HashMap<>());
            for(String label : fullAlphabet){
                if (!transitions.get(state).containsKey(label)){
                    transitions.get(state).put(label, sink);
                    needsSink = true;
                }
            }
        }

        if(needsSink){
            states.add(sink);
            transitions.putIfAbsent(sink, new HashMap<>());
            for(String label : fullAlphabet){
                transitions.get(sink).put(label, sink);
            }
        }
        alphabet.addAll(fullAlphabet);
    }

    public void complement(){

        //Flip accepting and non-accepting states so that we accept violating method-call traces as we need to check L(flow graph)∩L(Complemented DFA)=∅
        Set<String> newAcceptingStates = new HashSet<>();
        for (String state : states){
            if (!acceptingStates.contains(state)){
                newAcceptingStates.add(state);
            }
        }
        acceptingStates = newAcceptingStates;
    }



    public Set<String> getAlphabet(){
        return this.alphabet;
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<String> getAcceptingStates() {
        return acceptingStates;
    }

    public String getStartState() {
        return startState;
    }

    public Map<String, Map<String, String>> getTransitions() {
        return transitions;
    }

    public String getTransition(String fromState, String label) {
        Map<String, String> outgoing = transitions.get(fromState);
        if (outgoing == null) {
            return null;
        }
        return outgoing.get(label);
    }





    public void print() {
        System.out.println("-----------DFA-----------");
        System.out.println("Start state:");
        System.out.println(startState);

        System.out.println();

        System.out.println("States:");
        System.out.println(states);

        System.out.println();

        System.out.println("Accepting states:");
        System.out.println(acceptingStates);

        System.out.println();

        System.out.println("Alphabet:");
        System.out.println(alphabet);

        System.out.println();

        System.out.println("Transitions:");
        for (String fromState : transitions.keySet()) {
            for (String label : transitions.get(fromState).keySet()) {
                String toState = transitions.get(fromState).get(label);
                System.out.println(fromState + " --" + label + "--> " + toState);
            }
        }
    }

}
