package standings_sorter;

public class Utility_StandingsSorter {
	
	public static String charRemoveAt(String str, int p) {  
        return str.substring(0, p) + str.substring(p + 1);  
     }  

	public static int stringPtsToInt(String points) {
		String temp = points;
		temp = charRemoveAt(temp, temp.length()-1);
		temp = charRemoveAt(temp, temp.length()-1);
		return Integer.parseInt(temp);
	}
	
	public static void main(String[] args) {
		System.out.println(stringPtsToInt("25p."));
	}
}
