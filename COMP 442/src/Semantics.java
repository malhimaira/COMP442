public class Semantics {
    private String value;
    private boolean isLeaf;

    @Override
    public String toString() {
        if(isLeaf){
            return value.toUpperCase();
        }
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public Semantics(String value, boolean isLeaf) {
        this.value = value;
        this.isLeaf = isLeaf;
    }
}