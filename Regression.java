/*
 * @author GUNA BEAR
 */

import java.util.*;
import java.io.*;


public class Regression {

	public static void main(String[] args) throws IOException {
		
		//lets start by read in the input/data
		
		BufferedReader input   = new BufferedReader(new FileReader("dataReg.csv")); 
		String y = "";
		y = input.readLine();
		
		//arrayList of the x-values and of the y-values
		ArrayList<Integer> exes = new ArrayList<>();
		ArrayList<Float> whys = new ArrayList<>();
		ArrayList<Float> zs = new ArrayList<>();
		//array of sums also
		double[] sums = new double[6];
		
		int items = 0;
		
		while((y = input.readLine())!=null)
		{
			StringTokenizer this1 = new StringTokenizer(y,",");
			int x1 = (int)Double.parseDouble(this1.nextToken());
			Float y1 = Float.parseFloat(this1.nextToken());
			Float z1 = Float.parseFloat(this1.nextToken());
			//System.out.println(x1+"," +y1);
			
			//add to the current sum
			sums[0]+=x1;
			sums[1]+=y1;
			sums[2]+=(x1*y1);
			sums[3]+=(x1*x1);
			sums[4]+=(z1*x1);
			sums[5]+=z1;
			
			//add them to the lists
			exes.add(new Integer(x1));
			whys.add(new Float(y1));
			zs.add(new Float(z1));
			
			items++;
		}
		input.close();
		// Let's get the the summary statistics
		double xbar = sums[0] / items;
        double ybar = sums[1] / items;
        double zbar = sums[5] / items;

        double xxbar = 0.0, yybar = 0.0, xybar = 0.0, xzbar = 0.0, zzbar = 0.0;
        for (int i = 0; i < items; i++) {
            xxbar += (exes.get(i) - xbar) * (exes.get(i) - xbar);
            yybar += (whys.get(i) - ybar) * (whys.get(i) - ybar);
            xybar += (exes.get(i) - xbar) * (whys.get(i) - ybar);
            zzbar += (zs.get(i) -zbar) * (zs.get(i) - zbar);
            xzbar += (exes.get(i) - xbar) * (zs.get(i) - zbar);
        }
		
        double beta1 = xybar / xxbar;
        double beta0 = ybar - beta1 * xbar;
        double beta3 = xzbar / zzbar;
        double beta2 = zbar - beta3 * xbar;
		
        //int df = items - 2;
        //double rss = 0.0;      // residual sum of squares
        double ssr = 0.0;      // regression sum of squares
        //double rss2 = 0.0;
        double ssr2 = 0.0;
        for (int i = 0; i < items; i++) {
            double fit = beta1*exes.get(i) + beta0;
            double fit2 = beta3*exes.get(i) + beta2;
            //rss2 += (fit2 - zs.get(i)) * (fit2 - zs.get(i));
            ssr2 += (fit2 - zbar) * (fit2 - zbar);
            //rss += (fit - whys.get(i)) * (fit - whys.get(i));
            ssr += (fit - ybar) * (fit - ybar);
        }
        double r    = ssr / yybar;
        //double svar  = rss / df;
        //double svar1 = svar / xxbar;
        //double svar0 = svar/items + xbar*xbar*svar1;
        double r2 = ssr2 / zzbar;
       // double svar2 = rss2 / df;
        //double svar4 = svar2 / xxbar;
        //double svar3 = svar2/ items + xbar*xbar*svar4;
        
        
        
		// get the slope and intercept
		double slope = (items*sums[2] - (sums[1]*sums[0]))/(items*sums[3] - Math.pow(sums[0],2));
		double intercept = (sums[1]-slope*sums[0])/items;
		double slope2 = (items*sums[4] - (sums[5]*sums[0]))/(items*sums[3] - Math.pow(sums[0],2));
		double intercept2 = (sums[5]-slope*sums[0])/items;
		
		System.out.printf("TIME PRESSED: Line of best fit is: y = %.3fx + %.3f\nR (coeffiecent of correlation): %.3f\n",slope,intercept,Math.sqrt(r));
		System.out.printf("TRAVERSAL TIME: Line of best fit is: y = %.3fx + %.3f\nR (coeffiecent of correlation): %.3f\n",slope2,intercept2,Math.sqrt(r2));
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(args[0] + "Awns.txt", true);
			
			fw.append(String.format("TIME PRESSED;%.3f;%.3f;%.3f;",slope,intercept,Math.sqrt(r)));
			fw.append('\n');
			fw.append(String.format("TRAVERSAL TIME;%.3f;%.3f;%.3f;",slope2,intercept2,Math.sqrt(r2)));
			fw.append('\n');
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				fw.flush();
				fw.close();
			} catch (Exception e) {
				
			}
		}

		
			
	}

}
