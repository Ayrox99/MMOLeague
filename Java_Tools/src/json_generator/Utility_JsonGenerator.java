package json_generator;

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

}
