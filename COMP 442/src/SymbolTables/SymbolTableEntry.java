package SymbolTables;


import java.util.Vector;

public class SymbolTableEntry {
    public String          m_kind       = null;
    public String          m_type       = null;
    public String          m_name       = null;
    public String          m_encap       = "";

    public SymbolTable     m_subtable   = null;

    // Memory allocation size
    public int m_size = 0;

    public SymbolTableEntry() {}

    public SymbolTableEntry(String p_kind, String p_type, String p_name, SymbolTable p_subtable){
        m_kind = p_kind;
        m_type = p_type;
        m_name = p_name;
        m_subtable = p_subtable;
    }
    public SymbolTableEntry(String p_kind, String p_type, String p_name, SymbolTable p_subtable, String p_encap){
        m_kind = p_kind;
        m_type = p_type;
        m_name = p_name;
        m_subtable = p_subtable;
        m_encap = p_encap;
    }
}