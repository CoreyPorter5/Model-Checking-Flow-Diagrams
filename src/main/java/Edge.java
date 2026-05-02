public class Edge {
    private String from; //e.g v0
    private String to; //e.g: v1
    private String label; //eps or method name like a

    public Edge(String from, String to, String label){
        this.from = from;
        this.to = to;
        this.label = label;
    }


    public boolean isEpsilon(){
        return label.equals("eps");
    }

    public boolean isCall(){
        return !label.equals("eps");
    }

    public String getFrom(){
        return this.from;
    }

    public String getTo(){
        return this.to;
    }

    public String getLabel(){
        return this.label;
    }
}
