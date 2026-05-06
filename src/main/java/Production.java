import java.util.*;

public class Production {
    private String left;
    private List<String> right;

    public Production(String left, List<String> right){
        this.left = left;
        this.right = right;
    }

    public String getLeft(){
        return left;
    }

    public List<String> getRight(){
        return right;
    }

    @Override
    public String toString(){
        if(right.isEmpty()){
            return left + "-> eps";
        }
        return left + " -> " + String.join(" ", right);
    }
}
