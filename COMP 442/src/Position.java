public class Position {

    private int row;

    public Position(int row){
        this.row = row;
    }

    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public String toString(){
        return ""+row;
    }
}
