package json_generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import standings_sorter.Utility_StandingsSorter;

public class JsonGenerator {
	
	private ArrayList<Driver_JsonGenerator> driversList = new ArrayList<Driver_JsonGenerator>();
	private ArrayList<Team_JsonGenerator> teamsList = new ArrayList<Team_JsonGenerator>();;
	private ArrayList<String> racesList = new ArrayList<String>();
	private String league, season;
	
	public JsonGenerator(String league, String season){
		this.league = league;
		this.season = season;
		String path;
		if (season.contentEquals("")) {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\Standings\\Teams.txt";
		}else {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\"+season+"\\Standings\\Teams.txt";
		}
		try {
			File myObj = new File(path);
		    Scanner myReader = new Scanner(myObj);
		    int count = 0;
		    while (myReader.hasNextLine()) {
		    	String data = myReader.nextLine();
		        if (count==0) {
		        	String[] tabRaces = data.split(",");
		        	List<String> tempRaces = Arrays.asList(tabRaces);
		        	ArrayList<String> races = Utility_JsonGenerator.listToArrayList(tempRaces);
		        	Utility_JsonGenerator.racesSorter(races);
		        	for (int i = 0; i<tabRaces.length; i++) {
		        		tabRaces[i] = races.get(i);
		        	}
		        	for (int i = 0; i<tabRaces.length; i++) {
		        		racesList.add(tabRaces[i].split("-")[1]);
		        	}
		        	
		        }
		        if (count>0 && !data.contentEquals("")) {
		        	String[] rowTeam = data.split("-");
		        	teamsList.add(new Team_JsonGenerator(Integer.parseInt(rowTeam[0]), rowTeam[1], Utility_StandingsSorter.stringPtsToInt(rowTeam[2])));
		        }
		        count++;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred for file " + path);
		      e.printStackTrace();
		    }
		
		if (season.contentEquals("")) {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\Standings\\Drivers.txt";
		}else {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\"+season+"\\Standings\\Drivers.txt";
		}
		try {
			File myObj = new File(path);
		    Scanner myReader = new Scanner(myObj);
		    int count = 0;
		    while (myReader.hasNextLine()) {
		    	String data = myReader.nextLine();
		        if (count>0 && !data.contentEquals("")) {
		        	String[] rowDriver = data.split("-");
		        	driversList.add(new Driver_JsonGenerator(Integer.parseInt(rowDriver[0]), rowDriver[1], rowDriver[2], Utility_StandingsSorter.stringPtsToInt(rowDriver[3]), league, season));
		        }
		        count++;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred for file " + path);
		      e.printStackTrace();
		    }
		this.countPodiumsAndWins();
	}
	
	
	public void countPodiumsAndWins() {
		String path;
		ArrayList<String> races = new ArrayList<String>();
		if (season.contentEquals("")) {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+this.league;
		}else {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+this.league+"\\"+this.season;
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
				File myObj = new File(path+"\\"+race);
			    Scanner myReader = new Scanner(myObj);
			    while (myReader.hasNextLine()) {
			    	String data = myReader.nextLine();
			    	if (!data.contentEquals("")) {
			    		String[] rowDriver = data.split("-");
			    		if (rowDriver[0].contentEquals("1")) {
//			    			System.out.println("Bite");
			    			this.addWin(rowDriver[2]);
			    			this.addPodium(rowDriver[2]);
			    		}
			    		if (rowDriver[0].contentEquals("2") || rowDriver[0].contentEquals("3")) {
//			    			System.out.println("Bite");
			    			this.addPodium(rowDriver[2]);
			    		}
			    	}
			    }
			    myReader.close();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred");
		    e.printStackTrace();
		}	
	}
	
	public void addWin(String team) {
		for (Team_JsonGenerator t : this.teamsList) {
			if (t.getName().contentEquals(team)) {
				t.addWin();
			}
		}
	}
	
	public void addPodium(String team) {
		for (Team_JsonGenerator t : this.teamsList) {
			if (t.getName().contentEquals(team)) {
				t.addPodium();
			}
		}
	}
	
	public String racesToJson() {
		String s="\"races\":[";
		int i = 0;
		for (String race : this.racesList) {
			s+="\""+ race + "\"";
			if (i<this.racesList.size()-1) {
				s+=",";
			}
			i++;
		}
		s+="]";
		return s;
	}
	
	public String driversToJson() {
		String s="\"drivers\":[";
		int i = 0;
		for (Driver_JsonGenerator driver : this.driversList) {
			s+=driver.toJson();
			if (i<this.driversList.size()-1) {
				s+=",";
			}
			i++;
		}
		s+="]";
		return s;
	}
	
	public String teamsToJson() {
		String s="\"teams\":[";
		int i = 0;
		for (Team_JsonGenerator team : this.teamsList) {
			s+=team.toJson();
			if (i<this.teamsList.size()-1) {
				s+=",";
			}
			i++;
		}
		s+="]";
		return s;
	}
	
	public String toJson() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		String s = "{";
		s+="\"date\":\""+dtf.format(now)+"\",";  
		s+=this.racesToJson()+",";
		s+=this.driversToJson()+",";
		s+=this.teamsToJson();
		s+="}";
		return s;
	}
	
	public void createJson() {
		String path;
		if (season.contentEquals("")) {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\Standings";
		}else {
			path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\"+season+"\\Standings";
		}
		File file1 = new File(path+"\\infos.txt");
		try {
			file1.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        FileWriter fr = null;
        try {
            fr = new FileWriter(file1);
            fr.write(this.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file2 = new File(path+"\\infos.json");
        file2.delete();
        file1.renameTo(file2);
        
//        this.generateRaceJs(path);
        File file3 = new File ("D:\\Documents\\MMOnline\\MMOLeague\\tableau\\public\\data.json");
        file3.delete();
        try {
			file3.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        fr = null;
        try {
            fr = new FileWriter(file3);
            fr.write(this.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	public static void main(String[] args) {
		JsonGenerator test = new JsonGenerator("F3 League", "Season 2");
//		System.out.println(test.toJson());
		test.createJson();
	}
}
