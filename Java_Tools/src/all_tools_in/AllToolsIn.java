package all_tools_in;

import json_generator.JsonGenerator;
import standings_sorter.StandingsSorter;

public class AllToolsIn {

	StandingsSorter ss;
	JsonGenerator jsg;
	
	
	public AllToolsIn (String league, String season) {
		this.ss = new StandingsSorter(league, season);
		this.jsg = new JsonGenerator(league, season);		
	}
	
	public void start() {
		this.ss.sort();
		this.jsg.createJson();
	}
	
	public static void main(String[] args) {
		AllToolsIn a = new AllToolsIn("F1 League", "Season 1");
		a.start();
	}
	
}
