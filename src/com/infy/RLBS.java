package com.infy;

import java.util.List;
import java.util.Set;

public class RLBS {

	String name;
	Set<String> rules;
	
	public String getDetails(String dtn){

		String stn = "";
		String rtn = null;
		for( String rule : rules ) {
			String[] splits = rule.split(" ");
			if(splits.length > 1){
				stn = splits[0];
				if(stn.trim().equals(dtn)){
					rtn = splits[1]+" "+splits[2];
					if(splits.length >2){
						for(int l=3;l<splits.length;l++){
							if(!splits[l].equalsIgnoreCase("") || !splits[l].equalsIgnoreCase(")") )
								rtn=rtn+" "+splits[l];
						}
					}		
					return rtn;
				}
			}
		}
		return rtn;
	}
	public Set<String> getRules() {
		return rules;
	}
	public void setRules(Set<String> rules) {
		this.rules = rules;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
