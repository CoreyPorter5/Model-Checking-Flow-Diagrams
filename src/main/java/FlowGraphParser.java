import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class FlowGraphParser {

    public static FlowGraph parseFlowGraph(String filename) throws IOException {
        FlowGraph flowGraph = new FlowGraph();
        FileInputStream inputStream = new FileInputStream(filename);
        System.setIn(inputStream);
        Scanner sc = new Scanner(System.in);


        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith("node")) {
                parseNodeLine(line, flowGraph);
            } else if (line.startsWith("edge")) {
                parseEdgeLine(line, flowGraph);
            }


        }


        return flowGraph;


    }

    public static void parseEdgeLine(String line, FlowGraph flowGraph) {
        //edge v0 v1 eps
        String[] splitLine = line.split("\\s+");
        String from = splitLine[1];
        String to = splitLine[2];
        String label = splitLine[3];
        Edge edge = new Edge(from, to, label);
        flowGraph.addEdge(edge);


    }

    public static void parseNodeLine(String line, FlowGraph flowGraph) {
        //node v0 meth(main) entry
        String[] splitLine = line.split("\\s+");

        String nodeId = splitLine[1];
        String methodName = extractMethodName(splitLine[2]);

        boolean isEntry = false;
        boolean isReturn = false;
        if (splitLine.length >= 4) {
            String flag = splitLine[3].trim().toLowerCase();
            if (flag.equals("entry")) {
                isEntry = true;
            } else if (flag.equals("ret") || flag.equals("return")) {
                isReturn = true;
            }
        }

        Node node = new Node(nodeId, methodName, isEntry, isReturn);
        flowGraph.addNode(node);

    }


    private static String extractMethodName(String methodToken){
        int start = methodToken.indexOf("(");
        int end = methodToken.indexOf(")");

        if (start == -1 || end == -1 || end <= start) {
            throw new IllegalArgumentException("Invalid method token: " + methodToken);
        }

        return methodToken.substring(start + 1, end);
    }
}
