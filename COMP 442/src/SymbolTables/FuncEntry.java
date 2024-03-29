package SymbolTables;

import java.util.Vector;

public class FuncEntry extends SymbolTableEntry {
	
	public String m_params   = "";
	
	public FuncEntry(String p_type, String p_name, String p_params, SymbolTable p_table){
		super(new String("function"), p_type, p_name, p_table);
		m_params = p_params;
	}

	public FuncEntry(String p_type, String p_name, String p_params, SymbolTable p_table, String  p_encap){
		super(new String("function"), p_type, p_name, p_table, p_encap);
		m_params = p_params;
	}

	public String toString(){
		return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-12s" , "| " + m_name) + 
				String.format("%-12s"  , "| " + m_type) +
				String.format("%-12s"  , "| " + m_params) +
				String.format("%-12s"  , "| " + m_encap) +
				" |" +
				m_subtable;
	}	
}
