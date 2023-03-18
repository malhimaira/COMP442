package SymbolTables;

import java.util.*;

public class SymbolTable {

    private ArrayList<SymbolTableEntry> symbolTableEntries;
    ArrayList<SymbolTable> childrenTables;

    public SymbolTable() {
        symbolTableEntries = new ArrayList<>();
        childrenTables = new ArrayList<>();
    }

    public void addIdentifier(SymbolTableEntry ste) {
        symbolTableEntries.add(ste);
    }

    public String toString(){
        return "formatted table";
    }

}
