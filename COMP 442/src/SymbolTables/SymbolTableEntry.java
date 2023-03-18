package SymbolTables;

public class SymbolTableEntry {
    private IdentifierScope scope;
    private IdentifierType type;
    private String IdentifierDataType;
    private String name;
    private IdentifierEncapsulation encapsulation;

    public SymbolTableEntry(IdentifierScope scope, IdentifierType type, String identifierDataType, String name, IdentifierEncapsulation encapsulation) {
        this.scope = scope;
        this.type = type;
        IdentifierDataType = identifierDataType;
        this.name = name;
        this.encapsulation = encapsulation;
    }

    public String ToString(){
        return "formatted identifier";
    }
}