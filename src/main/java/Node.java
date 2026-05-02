public class Node {

    private String id; //v0 etc
    private String method; //main etc
    private boolean isEntry;
    private boolean isReturn;

    public Node(String id, String method, boolean isEntry, boolean isReturn){
        this.id = id;
        this.method = method;
        this.isEntry = isEntry;
        this.isReturn = isReturn;

    }

    public String getId(){
        return this.id;
    }

    public String getMethod(){
        return this.method;
    }

    public boolean isEntry() {
        return this.isEntry;
    }

    public boolean isReturn() {
        return this.isReturn;
    }
}
