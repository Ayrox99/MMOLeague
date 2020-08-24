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
	
}
