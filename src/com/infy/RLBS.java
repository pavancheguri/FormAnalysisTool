package com.infy;

import java.util.List;
import java.util.Set;

public class RLBS {

	String name;
	Set<String> rules;
	
	public String getDetails(String dtn){

		String stn = "";
		for( String rule : rules ) {
			String[] splits = rule.split(" ");
			if(splits.length > 1){
				stn = splits[0];
				if(stn.trim().equals(dtn)){
					return splits[1]+" "+splits[2];
				}
			}
		}
		return null;
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
