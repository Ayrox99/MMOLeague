package json_generator;

import java.util.ArrayList;
import java.util.List;

public class Utility_JsonGenerator {
	
	private static String systPoints = "Top20";
	
	private static int F1Points(int pos) {
		Integer[] pts = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1, 0};
		return pts[Math.min(pos-1, 10)];
	}
	
	private static int top20Points(int pos) {
		return 21-pos;
	}
	
	public static int positionToPoints(int pos) {
		if(systPoints.equals("Top20")) {
			return top20Points(pos);
		}else if (systPoints.equals("F1")) {
			return F1Points(pos);
		}
		return -1;
	}
	
	public static void racesSorter(ArrayList<String> races) {
		int sizeTravel = races.size();
		while (sizeTravel > 1) {
			int ind = -1;
			int min = 100;
			for (int i = 0; i< sizeTravel; i++) {
				String s1 = races.get(i).split("-")[0];
				String s2 = s1.substring(1, s1.length());
				int nb = Integer.parseInt(s2);
				if (nb<min) {
					min = nb;
					ind = i;
				}
			}
			String s = races.get(ind);
			races.add(s);
			races.remove(ind);
			sizeTravel--;
		}
		String s = races.get(0);
		races.add(s);
		races.remove(0);
	}
	
	public static ArrayList<String> listToArrayList(List<String> list){
		ArrayList<String> l = new ArrayList<String>();
		for (String s : list) {
			l.add(s);
		}
		return l;
	}

}
