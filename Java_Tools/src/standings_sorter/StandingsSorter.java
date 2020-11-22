package standings_sorter;

public class StandingsSorter {
	
	StandingsSorterSpecific sst;
	StandingsSorterSpecific ssd;
	
	public StandingsSorter(String league, String season) {
		this.sst = new StandingsSorterSpecific(league, season, "Teams");
		this.ssd = new StandingsSorterSpecific(league, season, "Drivers");
	}
	
	public void sort() {
		this.sst.sort();
		this.ssd.sort();
	}
	
	public static void main(String[] args) {
		StandingsSorter ss = new StandingsSorter("F1 League", "Season 2");
		ss.sort();
	}
	
}
