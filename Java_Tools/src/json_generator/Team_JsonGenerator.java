package json_generator;

public class Team_JsonGenerator {
	
	protected int position;
	protected String name;
	protected int points;
	protected int wins = 0;
	protected int podiums = 0;
	
	public Team_JsonGenerator(int position, String name, int points) {
		this.position = position;
		this.name = name;
		this.points = points;
	}
	
	public void addWin() {
		this.wins++;
	}
	
	public void addPodium() {
		this.podiums++;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toJson() {
		String s = "{\"position\":"+this.position;
		s+=",\"name\":\""+this.name+"\"";
		s+=",\"points\":"+this.points;
		s+=",\"victories\":"+this.wins;
		s+=",\"podiums\":"+this.podiums+"}";
		return s;
	}
	
	public static void main(String[] args) {
		Team_JsonGenerator test = new Team_JsonGenerator(4, "Roinel GP", 148);
		System.out.println(test.toJson());
	}
	
}
