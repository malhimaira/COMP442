package SymbolTables;

import java.util.ArrayList;

public class SymbolTable {
    public String m_name = null;
    public ArrayList<SymbolTableEntry> m_symlist = null;
    public int m_tablelevel = 0;
    public SymbolTable m_uppertable = null;

    public SymbolTable(int p_level, SymbolTable p_uppertable) {
        m_tablelevel = p_level;
        m_name = null;
        m_symlist = new ArrayList<SymbolTableEntry>();
        m_uppertable = p_uppertable;
    }

    public SymbolTable(int p_level, String p_name, SymbolTable p_uppertable) {
        m_tablelevel = p_level;
        m_name = p_name;
        m_symlist = new ArrayList<SymbolTableEntry>();
        m_uppertable = p_uppertable;
    }

    public void addEntry(SymbolTableEntry p_entry) {
        m_symlist.add(p_entry);
    }

    public SymbolTableEntry lookupName(String p_tolookup) {
        SymbolTableEntry returnvalue = new SymbolTableEntry();
        boolean found = false;
        for (SymbolTableEntry rec : m_symlist) {
            if (rec.m_name.equals(p_tolookup)) {
                returnvalue = rec;
                found = true;
            }
        }
        if (!found) {
            if (m_uppertable != null) {
                returnvalue = m_uppertable.lookupName(p_tolookup);
            }
        }
        return returnvalue;
    }

    public String toString() {
        String stringtoreturn = new String();
        String prelinespacing = new String();
        for (int i = 0; i < this.m_tablelevel; i++)
            prelinespacing += "|    ";
        stringtoreturn += "\n" + prelinespacing + "=====================================================\n";
        stringtoreturn += prelinespacing + String.format("%-25s", "| table: " + m_name) + String.format("%-27s", " ") + "|\n";
        stringtoreturn += prelinespacing + "=====================================================\n";
        for (int i = 0; i < m_symlist.size(); i++) {
            stringtoreturn += prelinespacing + m_symlist.get(i).toString() + '\n';
        }
        stringtoreturn += prelinespacing + "=====================================================";
        return stringtoreturn;
    }
}
