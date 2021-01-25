
public class Outcast {
	private WordNet wordNet;
	public Outcast(WordNet wordnet) {
		wordNet = wordnet;
	}
	   public String outcast(String[] nouns)  {
		int dist = Integer.MIN_VALUE;
		String temp = null;
		int tempDist;
		for(String noun: nouns) {
			
			tempDist = getDistance(noun);
			if(tempDist > dist) {
				dist = tempDist;
				temp = noun;
			}
		}
		return temp;
	   }
	   private int getDistance(String a) {
		   int temp = 0;
		   for(String b: wordNet.nouns()) {
			   temp += wordNet.distance(a, b);
		   }
		   return temp;
	   }
	   public static void main(String[] args) {
		   
	   }
}
