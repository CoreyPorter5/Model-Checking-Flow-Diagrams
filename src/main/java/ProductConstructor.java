import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Collections;


public class ProductConstructor {


	public static ProductCFG constructProductCFG(FlowGraph flowGraph, DFA dfa) {
        
		ProductCFG productCFG = new ProductCFG();

		// Find a starting node in the flow graph
		String startNode = findStartNode(flowGraph);
		String startState = dfa.getStartState();
		if (startNode == null || startState == null || startState.isEmpty()) {
			return productCFG;
		}

		// S -> [q0 v_start q_accept] for every state in F
		for (String acceptingState : dfa.getAcceptingStates()) {
			productCFG.addProduction(
					"S",
					singletonProductVariable(startState, startNode, acceptingState)
			);
		}

		Set<String> dfaStates = dfa.getStates();

		// Add [q a r] -> a
		for (Map.Entry<String, Map<String, String>> fromEntry : dfa.getTransitions().entrySet()) {
			String fromState = fromEntry.getKey();
			for (Map.Entry<String, String> trans : fromEntry.getValue().entrySet()) {
				String label = trans.getKey();
				String toState = trans.getValue();
				productCFG.addProduction(productVariable(fromState, label, toState), Collections.singletonList(label));
			}
		}

		// Walk over all flow graph edges and add matching productions
		for (Edge edge : flowGraph.getAllEdges()) {
			if (edge.isEpsilon()) {

				// Internal / epsilon edges rewrite the variable to the successor node variable
				for (String leftState : dfaStates) {
					for (String rightState : dfaStates) {
						productCFG.addProduction(
								productVariable(leftState, edge.getFrom(), rightState),
								singletonProductVariable(leftState, edge.getTo(), rightState)
						);
					}
				}
				continue;
			}

			// For labeled edges (calls etc.) we make the DFA to find
			// the next state from a given leftState on the edge label
			String label = edge.getLabel();
			boolean isDefinedMethod = flowGraph.getMethods().contains(label);

			for (String leftState : dfaStates) {
				String midState = dfa.getTransition(leftState, label);
				if (midState == null) {
					continue;
				}

				for (String rightState : dfaStates) {
					if (isDefinedMethod) {

						// [q_a v_i q_d] -> [q_a m q_b] [q_b v_k q_c] [q_c v_j q_d]
						String entryNode = flowGraph.getEntryNode().get(label);


						if (entryNode == null) {
							// fallback to atomic if we don't have an entry
							List<String> rhs = new ArrayList<>();
							rhs.add(productVariable(leftState, label, midState));
							rhs.add(productVariable(midState, edge.getTo(), rightState));
							productCFG.addProduction(productVariable(leftState, edge.getFrom(), rightState), rhs);
							continue;
						}

						for (String qc : dfaStates) {
							List<String> rhs = new ArrayList<>();
							rhs.add(productVariable(leftState, label, midState));
							rhs.add(productVariable(midState, entryNode, qc));
							rhs.add(productVariable(qc, edge.getTo(), rightState));
							productCFG.addProduction(productVariable(leftState, edge.getFrom(), rightState), rhs);
						}
					} else {
						// External action, link via atomic variable [q m q'] and it already
						// has a production to the terminal label
                        //  So,
						// [q_a v_i q_d] -> [q_a m q_b] [q_b v_j q_d]
						List<String> rhs = new ArrayList<>();
						rhs.add(productVariable(leftState, label, midState));
						rhs.add(productVariable(midState, edge.getTo(), rightState));
						productCFG.addProduction(productVariable(leftState, edge.getFrom(), rightState), rhs);
					}
				}
			}
		}

		// Return nodes correspond to empty productions as in `[q v q] -> eps`.
		for (Node node : flowGraph.getNodes().values()) {
			if (!node.isReturn()) {
				continue;
			}

			for (String state : dfaStates) {
				productCFG.addProduction(
						productVariable(state, node.getId(), state),
						new ArrayList<>() // empty right hand side -> eps
				);
			}
		}

		return productCFG;
	}

	private static String findStartNode(FlowGraph flowGraph) {
		String mainEntry = flowGraph.getEntryNode().get("main");
		if (mainEntry != null) {
			return mainEntry;
		}

		if (!flowGraph.getEntryNode().isEmpty()) {
			return flowGraph.getEntryNode().values().iterator().next();
		}

		if (!flowGraph.getNodes().isEmpty()) {
			return flowGraph.getNodes().keySet().iterator().next();
		}

		return null;
	}

	private static String productVariable(String leftState, String nodeId, String rightState) {
		return "[" + leftState + " " + nodeId + " " + rightState + "]";
	}

	private static List<String> singletonProductVariable(String leftState, String nodeId, String rightState) {
		List<String> rightSide = new ArrayList<>();
		rightSide.add(productVariable(leftState, nodeId, rightState));
		return rightSide;
	}
}
