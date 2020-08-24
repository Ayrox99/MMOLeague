package standings_sorter;

public class Rankable {

	protected int ranking;
	protected String name;
	protected int points;
	
	public void setPosition(int pos) {
		this.ranking = pos;
	}
	
	public int getPoints() {
		return this.points;
	}
	
}
