package standings_sorter;

public class Team_StandingsSorter extends Rankable{
	
//	private int ranking;
//	private String name;
//	private int points;
	
	public Team_StandingsSorter (String text) {
		String[] l = text.split("-");
		this.ranking = Integer.parseInt(l[0]);
		this.name = l[1];
		this.points = Utility_StandingsSorter.stringPtsToInt(l[2]);
	}
	
//	public void setPosition(int pos) {
//		this.ranking = pos;
//	}
	
//	public int getPoints() {
//		return this.points;
//	}
	
	public void getInfos() {
		System.out.println(this.ranking);
		System.out.println(this.name);
		System.out.println(this.points);
	}
	
	public String toString() {
		return this.ranking+"-"+this.name+"-"+this.points+"p.";
	}
	
	public static void main(String[] args) {
		Team_StandingsSorter t = new Team_StandingsSorter ("1-Roinel GP-25p.");
		System.out.println(t.ranking);System.out.println(t.name);System.out.println(t.points);
	}
	
}