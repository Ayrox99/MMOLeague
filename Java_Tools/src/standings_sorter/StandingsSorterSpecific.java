package standings_sorter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class StandingsSorterSpecific{

	private String races;
	private ArrayList<Rankable> standings;
	private String path;
	private String type;
	
	public StandingsSorterSpecific(String league, String season, String type) {
		this.standings = new ArrayList<Rankable>();
		this.type = type;
		if (season.contentEquals("")) {
			this.path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\Standings\\"+type+".txt";
		}else {
			this.path = "D:\\Documents\\MMOnline\\MMOLeague\\"+league+"\\"+season+"\\Standings\\"+type+".txt";
		}
		try {
			File myObj = new File(this.path);
		    Scanner myReader = new Scanner(myObj);
		    int count = 0;
		    while (myReader.hasNextLine()) {
		    	String data = myReader.nextLine();
		        if (count==0) {
		        	this.races = data;
		        }
		        if (count>0 && !data.contentEquals("")) {
		        	if (this.type == "Teams") {
		        		this.standings.add(new Team_StandingsSorter(data));
		        	}else if (this.type == "Drivers") {
		        		this.standings.add(new Driver_StandingsSorter(data));
		        	}
		        	
		        }
//		        System.out.println(data);
		        count++;
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred for file " + path);
		      e.printStackTrace();
		    }
	}
	
	public void sort() {
		
		ArrayList<String> sortedRanking = new ArrayList<String>();
		int position = 1;
		while(this.standings.size()>0) {
			int maxPoints = -1;
			int indice = -1;
			for (int i = 0; i<this.standings.size(); i++) {
				if (this.standings.get(i).getPoints()>maxPoints) {
					maxPoints = this.standings.get(i).getPoints();
					indice = i;
				}
			}
			this.standings.get(indice).setPosition(position);
			sortedRanking.add(this.standings.get(indice).toString());
			this.standings.remove(indice);
			position++;
		}
		
		File file = new File(this.path);
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(this.races + "\n");
            for (int i = 0; i<sortedRanking.size(); i++) {
            	fr.write(sortedRanking.get(i));
            	if (i<sortedRanking.size()-1) {
            		fr.write("\n");
            	}
            }
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
        System.out.println("Standings sorted for " + path);
	}
	
	public static void main(String[] args) {
		String league = "F2 League";
		String season = "Season 1";
		StandingsSorterSpecific teams = new StandingsSorterSpecific(league, season, "Teams");
		teams.sort();
		StandingsSorterSpecific drivers = new StandingsSorterSpecific(league, season, "Drivers");
		drivers.sort();
	}
	
}
