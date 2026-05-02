import java.util.*;

public class FlowGraph {

    private Map<String, Node> nodes; //node id -> node
    private Map<String, List<Edge>> outgoing; //node id -> outgoing edges
    private Map<String, String> entryNode; //method name -> entry node id
    private Map<String, Set<String>> returnNodes; //method name -> return node ids
    private Set<String> methods; //all know method names


    public FlowGraph() {
        nodes = new HashMap<>();
        outgoing = new HashMap<>();
        entryNode = new HashMap<>();
        returnNodes = new HashMap<>();
        methods = new HashSet<>();

    }

    public void addNode(Node node) {
        nodes.putIfAbsent(node.getId(), node);
        outgoing.putIfAbsent(node.getId(), new ArrayList<>());

        if (node.isEntry()) {
            entryNode.putIfAbsent(node.getMethod(), node.getId());
        } else if (node.isReturn()) {
            returnNodes.putIfAbsent(node.getMethod(), new HashSet<>());
            returnNodes.get(node.getMethod()).add(node.getId());

        }

        methods.add(node.getMethod());


    }

    public void addEdge(Edge edge) {
        outgoing.putIfAbsent(edge.getFrom(), new ArrayList<>());
        outgoing.get(edge.getFrom()).add(edge);

    }


    public void print() {
        System.out.println("Nodes:");
        for (Node node : nodes.values()) {
            System.out.println(
                    node.getId() + " method=" + node.getMethod() +
                            " entry=" + node.isEntry() +
                            " return=" + node.isReturn()
            );
        }

        System.out.println();

        System.out.println("Edges:");
        for (String from : outgoing.keySet()) {
            for (Edge edge : outgoing.get(from)) {
                System.out.println(edge.getFrom() + " --" + edge.getLabel() + "--> " + edge.getTo());
            }
        }

        System.out.println();

        System.out.println("Entry nodes:");
        for (String method : entryNode.keySet()) {
            System.out.println(method + " -> " + entryNode.get(method));
        }

        System.out.println();

        System.out.println("Return nodes:");
        for (String method : returnNodes.keySet()) {
            System.out.println(method + " -> " + returnNodes.get(method));
        }
    }

    public Map<String, Node> getNodes() {
        return this.nodes;
    }

    public Map<String, List<Edge>> getOutgoing() {
        return this.outgoing;
    }

    public Map<String, String> getEntryNode() {
        return this.entryNode;
    }

    public Map<String, Set<String>> getReturnNodes() {
        return this.returnNodes;
    }

    public Set<String> getMethods() {
        return this.methods;
    }

    public Set<Edge> getAllEdges(){
        Set<Edge> edges = new HashSet<>();

        for (List<Edge> edgeList : outgoing.values()){
            edges.addAll(edgeList);
        }

        return edges;
    }

    public Set<String> getEdgeLabels() {
        Set<String> labels = new HashSet<>();

        for (Edge edge : getAllEdges()) {
            labels.add(edge.getLabel());
        }

        return labels;
    }


}
