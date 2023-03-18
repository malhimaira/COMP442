package SymbolTables;

public class ClassEntry extends SymbolTableEntry {

	public ClassEntry(String p_name, SymbolTable p_subtable){
		super(new String("class"), p_name, p_name, p_subtable);
	}

	@Override
	public String toString(){
		return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-40s" , "| " + m_name) + 
				"|" + 
				m_subtable;
	}
	
}

