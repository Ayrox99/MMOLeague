package json_generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
	
	public void generateRaceJs(String path) {
		String s = "function loadJSON(callback) {   \r\n" + 
				"\r\n" + 
				"    let xobj = new XMLHttpRequest();\r\n" + 
				"    xobj.overrideMimeType(\"application/json\");\r\n" + 
				"    xobj.open('GET', '";
		s+=path+"\\infos.json";
		s+="', true);\r\n" + 
				"    xobj.onreadystatechange = function () {\r\n" + 
				"          if (xobj.readyState == 4 && xobj.status == \"200\") {\r\n" + 
				"            callback(xobj.responseText);\r\n" + 
				"          }\r\n" + 
				"    };\r\n" + 
				"    xobj.send(null);  \r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"function init() {\r\n" + 
				"    loadJSON(function(response) {\r\n" + 
				"\r\n" + 
				"        let actual_JSON = JSON.parse(response);\r\n" + 
				"\r\n" + 
				"        let points = document.getElementById(\"driver-points\");\r\n" + 
				"\r\n" + 
				"        actual_JSON[\"races\"].forEach(race => {\r\n" + 
				"            let new_th = document.createElement(\"th\");\r\n" + 
				"            new_th.textContent = race;\r\n" + 
				"            new_th.className = \"gp\";\r\n" + 
				"            points.before(new_th);\r\n" + 
				"        });\r\n" + 
				"\r\n" + 
				"        let driver_data = document.getElementById(\"driver-data\");\r\n" + 
				"\r\n" + 
				"        actual_JSON[\"drivers\"].forEach(driver => {//Tableau des pilotes\r\n" + 
				"            let new_tr = document.createElement(\"tr\");\r\n" + 
				"\r\n" + 
				"            let position = document.createElement(\"th\");\r\n" + 
				"            position.scope = \"row\";\r\n" + 
				"            position.textContent = driver[\"position\"];\r\n" + 
				"\r\n" + 
				"            let name = document.createElement(\"td\");\r\n" + 
				"            name.textContent = driver[\"name\"];\r\n" + 
				"            name.className = \"score\";\r\n" + 
				"\r\n" + 
				"            let teamname = document.createElement(\"td\");\r\n" + 
				"            teamname.textContent = driver[\"teamname\"];\r\n" + 
				"\r\n" + 
				"            new_tr.append(position);\r\n" + 
				"            new_tr.append(teamname);\r\n" + 
				"            new_tr.append(name);\r\n" + 
				"\r\n" + 
				"            for (let i = 0; i < driver[\"results\"].length; i++) {\r\n" + 
				"                let res = document.createElement(\"td\");\r\n" + 
				"                res.textContent = driver[\"results\"][i];\r\n" + 
				"                switch (driver[\"positions\"][i]) {\r\n" + 
				"                    case 1:\r\n" + 
				"                        res.className = \"first-place sp\";\r\n" + 
				"                        break;\r\n" + 
				"                    case 2:\r\n" + 
				"                        res.className = \"second-place sp\";\r\n" + 
				"                        break;\r\n" + 
				"                    case 3:\r\n" + 
				"                        res.className = \"third-place sp\";\r\n" + 
				"                        break;\r\n" + 
				"                    case -1:\r\n" + 
				"                        res.className = \"dns sp\";\r\n" + 
				"                        break;\r\n" + 
				"                }\r\n" + 
				"                new_tr.append(res);\r\n" + 
				"              }\r\n" + 
				"\r\n" + 
				"            let points = document.createElement(\"td\");\r\n" + 
				"            points.textContent = driver[\"totalpoints\"];\r\n" + 
				"            points.className = \"score\";\r\n" + 
				"            new_tr.append(points);\r\n" + 
				"\r\n" + 
				"            driver_data.append(new_tr);\r\n" + 
				"        });\r\n" + 
				"\r\n" + 
				"        let team_data = document.getElementById(\"team-data\");\r\n" + 
				"\r\n" + 
				"        actual_JSON[\"teams\"].forEach(team => {//Tableau des equipes\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"            let new_tr = document.createElement(\"tr\");\r\n" + 
				"            switch (team[\"position\"]) {\r\n" + 
				"                case 1:\r\n" + 
				"                    new_tr.className = \"first-place\";\r\n" + 
				"                    break;\r\n" + 
				"                case 2:\r\n" + 
				"                    new_tr.className = \"second-place\";\r\n" + 
				"                    break;\r\n" + 
				"                case 3:\r\n" + 
				"                    new_tr.className = \"third-place\";\r\n" + 
				"                    break;\r\n" + 
				"            }\r\n" + 
				"\r\n" + 
				"            let position = document.createElement(\"th\");\r\n" + 
				"            position.scope = \"row\";\r\n" + 
				"            position.textContent = team[\"position\"];\r\n" + 
				"\r\n" + 
				"            let name = document.createElement(\"td\");\r\n" + 
				"            name.textContent = team[\"name\"];\r\n" + 
				"            name.className = \"score\";\r\n" + 
				"\r\n" + 
				"            let victories = document.createElement(\"td\");\r\n" + 
				"            victories.textContent = team[\"victories\"];\r\n" + 
				"\r\n" + 
				"            let podiums = document.createElement(\"td\");\r\n" + 
				"            podiums.textContent = team[\"podiums\"];\r\n" + 
				"\r\n" + 
				"            let points = document.createElement(\"td\");\r\n" + 
				"            points.textContent = team[\"points\"];\r\n" + 
				"            points.className = \"score\";\r\n" + 
				"\r\n" + 
				"            new_tr.append(position);\r\n" + 
				"            new_tr.append(name);\r\n" + 
				"            new_tr.append(victories);\r\n" + 
				"            new_tr.append(podiums);\r\n" + 
				"            new_tr.append(points);\r\n" + 
				"\r\n" + 
				"            team_data.append(new_tr);\r\n" + 
				"        });\r\n" + 
				"\r\n" + 
				"        //date \r\n" + 
				"\r\n" + 
				"        let jsondate = actual_JSON[\"date\"];\r\n" + 
				"\r\n" + 
				"        let dates = document.querySelectorAll(\".updated\");\r\n" + 
				"        dates.forEach(date => {\r\n" + 
				"            date.textContent = jsondate;\r\n" + 
				"        });\r\n" + 
				"\r\n" + 
				"    });\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"init();";
		File file = new File("D:\\Documents\\MMOnline\\MMOLeague\\tableau\\public\\race.js");
		file.delete();
		FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(s);
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
		JsonGenerator test = new JsonGenerator("F3 League", "Season 1");
//		System.out.println(test.toJson());
		test.createJson();
	}
}
