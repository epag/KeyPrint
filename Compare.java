import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.lang.Math;


public class Compare {
	float slope;
	float coeff; 
	float offset;
	
	Compare (float slope, float coeff, float offset) {
		this.slope = slope;
		this.coeff = coeff;
		this.offset = offset;
	}
	
	public static float cmp(ArrayList<Compare> a, ArrayList<Compare> b) {
		float slopeA = 0;
		float coeffA = 0;
		float offsetA = 0;
		float slopeB = 0;
		float coeffB = 0;
		float offsetB = 0;
		float newSA = 0;
		float newCA = 0;
		float newOA = 0;
		float newSB = 0;
		float newCB = 0;
		float newOB = 0;
		
		for (int x = 0; x < a.size() -1; x++) {
			slopeA+=a.get(x).slope;
			coeffA+=a.get(x).coeff;
			offsetA+=a.get(x).offset;
			slopeB+=b.get(x).slope;
			coeffB+=b.get(x).coeff;
			offsetB+=b.get(x).offset;
		}
		slopeA = slopeA/(a.size()-1);
		coeffA = coeffA/(a.size()-1);
		offsetA = offsetA/(a.size()-1);
		slopeB = slopeB/(b.size()-1);
		coeffB = coeffB/(b.size()-1);
		offsetB = offsetB/(b.size()-1);
		
		newSA = (Math.abs((slopeA - a.get(a.size()-1).slope)) / (Math.abs(slopeA + a.get(a.size()-1).slope))/2)*100;
		newCA = (Math.abs((coeffA - a.get(a.size()-1).coeff)) / (Math.abs(coeffA + a.get(a.size()-1).coeff))/2)*100;
		newOA = (Math.abs((offsetA - a.get(a.size()-1).offset)) / (Math.abs(offsetA + a.get(a.size()-1).offset))/2)*100;
		
		newSB = (Math.abs((slopeB - b.get(b.size()-1).slope)) / (Math.abs(slopeB + b.get(b.size()-1).slope))/2)*100;
		newCB = (Math.abs((coeffB - b.get(b.size()-1).coeff)) / (Math.abs(coeffB + b.get(b.size()-1).coeff))/2)*100;
		newOB = (Math.abs((offsetB - b.get(b.size()-1).offset)) / (Math.abs(offsetB + b.get(b.size()-1).offset))/2)*100;
		
		float avgError = (Math.abs(newSA) + Math.abs(newCA) + Math.abs(newOA) + Math.abs(newSB) + Math.abs(newCB) + Math.abs(newOB)) / 6;
		
		return avgError;
	}
	
	public static void deleteLine(String s) throws IOException {
		RandomAccessFile f = new RandomAccessFile(s, "rw");
		byte b;
		long length = f.length() - 1;
		do {                     
		  length -= 1;
		  f.seek(length);
		  b = f.readByte();
		} while(b != 10);
		f.setLength(length+1);
		f.close();
	}
	
	public static void main(String[] args) throws IOException {
		String line;
		ArrayList<Compare> TimeHeld = new ArrayList<Compare>();
		ArrayList<Compare> TimeBetween = new ArrayList<Compare>();
		try (
		    InputStream fis = new FileInputStream(args[0] + "Awns.txt");
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		) {
		    while ((line = br.readLine()) != null) {
		        String []holder = line.split(";");
		        Compare c = new Compare(Float.parseFloat(holder[1]), Float.parseFloat(holder[2]), Float.parseFloat(holder[3]));
		        TimeHeld.add(c);
		        line = br.readLine();
		        String []holderz = line.split(";");
		        Compare s = new Compare(Float.parseFloat(holderz[1]), Float.parseFloat(holderz[2]), Float.parseFloat(holderz[3]));
		        TimeBetween.add(s);
		    }
		}
		float err = cmp(TimeHeld, TimeBetween);
		
		//System.out.println(cmp(TimeHeld, TimeBetween));
		if (err > 4) {
			System.out.println("You may not be who you say you are");
			deleteLine(args[0] + "Awns.txt");
			deleteLine(args[0] + "Awns.txt");
		} else {
			System.out.println("Welcome " + args[0]);
		}
	}
}
