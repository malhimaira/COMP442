package SymbolTables;

public class InheritListEntry extends SymbolTableEntry {

	public String inheritedClass;
	public InheritListEntry(String p_kind, String p_type, String p_name){
		super(p_kind, p_type, p_name, null);
	}

	public InheritListEntry(String p_kind, String p_type, String p_name, String p_encap){
		super(p_kind, p_type, p_name, null, p_encap);
	}
		
	public String toString(){
		return 	String.format("%-12s" , "| " + "inherit") +
				String.format("%-12s" , "| " + m_name)
		        + " |";
	}
}
