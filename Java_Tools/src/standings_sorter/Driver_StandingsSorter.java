package standings_sorter;

public class Driver_StandingsSorter extends Rankable{

	private String teamname;
	
	public Driver_StandingsSorter (String text) {
		String[] l = text.split("-");
		this.ranking = Integer.parseInt(l[0]);
		this.name = l[1];
		this.teamname = l[2];
		this.points = Utility_StandingsSorter.stringPtsToInt(l[3]);
	}
	
	public void getInfos() {
		System.out.println(this.ranking);
		System.out.println(this.name);
		System.out.println(this.teamname);
		System.out.println(this.points);
	}
	
	public String toString() {
		return this.ranking+"-"+this.name+"-"+this.teamname+"-"+this.points+"p.";
	}
	
}
