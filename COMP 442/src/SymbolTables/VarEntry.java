package SymbolTables;

import java.util.Vector;

public class VarEntry extends SymbolTableEntry {
		
	public VarEntry(String p_kind, String p_type, String p_name){
		super(p_kind, p_type, p_name, null);
	}

	public VarEntry(String p_kind, String p_type, String p_name, Vector<Integer> p_dims){
		super(p_kind, p_type, p_name, null, p_dims);
	}

	public VarEntry(String p_kind, String p_type, String p_name, String p_encap){
		super(p_kind, p_type, p_name, null, p_encap);
	}

	public VarEntry(String p_kind, String p_type, String p_name, String p_encap, Vector<Integer> p_dims){
		super(p_kind, p_type, p_name, null, p_encap, p_dims);
	}
		
	public String toString(){
		return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-12s" , "| " + m_name) + 
				String.format("%-12s"  , "| " + m_type)+
				String.format("%-12s"  , "| " + m_size)+
				String.format("%-8s"  , "| " + m_encap)
		        + " |";
	}
}
