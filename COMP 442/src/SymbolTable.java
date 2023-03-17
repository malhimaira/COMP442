import java.util.*;

public class SymbolTable {

    private ArrayList<Identifier> identifiers;
    ArrayList<SymbolTable> childrenTables;

    public SymbolTable() {
        identifiers = new ArrayList<>();
    }

    public void addIdentifier(Identifier iden) {
        identifiers.add(iden);
    }

    public String toString(){
        return "formatted table";
    }

}
