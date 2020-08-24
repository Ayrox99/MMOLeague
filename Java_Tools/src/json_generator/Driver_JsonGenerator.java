package json_generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver_JsonGenerator {
	
	protected int position;
	protected String team;
	protected String name;
	protected int points;
	protected ArrayList<Integer> resultsPerRace;
	protected ArrayList<String> pointsPerRace;
	
	public Driver_JsonGenerator (int position, String driverName, String teamName, int points, String league, String season) {
		String path;
		this.position = position;
		this.points = points;
		this.name = driverName;
		this.team = teamName;
		this.resultsPerRace = new ArrayList<Integer>();
		this.pointsPerRace = new ArrayList<String>();
		ArrayList<String> races = new ArrayList<String>();
		if (season.contentEquals("")) {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league;
		}else {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\"+season;
		}
//		System.out.println(path);
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
		    if (file.isFile() && file.getName().substring(file.getName().length()-4).equals(".txt")) {
		    	races.add(file.getName());
		    }
		}
		try {
			for (String race : races) {
				boolean hasRaced = false;
				File myObj = new File(path+"\\"+race);
			    Scanner myReader = new Scanner(myObj);
			    while (myReader.hasNextLine()) {
			    	String data = myReader.nextLine();
			    	String[] ligne = data.split("-");
			    	if (ligne.length > 1 && ligne[1].equals(driverName) && ligne[2].equals(teamName)) {
			    		int pos = Integer.parseInt(ligne[0]);
			    		resultsPerRace.add(pos);
			    		pointsPerRace.add(String.valueOf(Utility_JsonGenerator.positionToPoints(pos)));
			    		hasRaced = true;
			    	}
			    }
			    if (!hasRaced) {
			    	resultsPerRace.add(-1);
			    	pointsPerRace.add("DNS");
			    }
			    myReader.close();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred");
		    e.printStackTrace();
		}	
	}
	
	public void displayInfos() {
		System.out.println("Driver : " + this.name + ", team : " + this.team);
		int i = 1;
		for (int pos : this.resultsPerRace) {
			System.out.println("Position course " + i + " : " +pos);
			i++;
		}
		System.out.println();
		i = 1;
		for (String s : this.pointsPerRace) {
			System.out.println("Points course " + i + " : " +s);
			i++;
		}
	}
	
	public String toJson() {
		String s = "{\"position\":"+this.position;
		s+=",\"name\":\""+this.name+"\"";
		s+=",\"teamname\":\""+this.team+"\"";
		s+=",\"totalpoints\":"+this.points;
		s+=",\"results\":[";
		int i = 0;
		for (String res : this.pointsPerRace) {
			s+="\""+res+"\"";
			if (i<this.pointsPerRace.size()-1) {
				s+=",";
			}
			i++;
		}
		s+="],\"positions\":[";
		i = 0;
		for (Integer res : this.resultsPerRace) {
			s+=res;
			if (i<this.resultsPerRace.size()-1) {
				s+=",";
			}
			i++;
		}
		s+="]}";
		return s;
	}
	
	public static void main(String[] args) {
		Driver_JsonGenerator test = new Driver_JsonGenerator(3, "Buemi", "Roinel GP", 98, "F1 League", "Season 1");
		System.out.println(test.toJson());
	}
	
}
