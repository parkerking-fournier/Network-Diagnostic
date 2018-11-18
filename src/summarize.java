/* 	This class writes the average upload and download speeds as well as the 
	percentage that those were below the advertised speed to previousmonth_year/summary.txt
*/

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class summarize{

	public static void main(String[] args) {
		/*	THIS IS WHERE YOU CAN CHANGE THE ADVERTISED UPLOAD
			AND DOWNLOAD SPEED VALUES. THE SPEED IS IN MEGABITS PER SECOND

			Here are the steps to follow to change those values:

			1)	Change tha value after the lines:
					double advertised_upload_speed = INSERT_UPLOAD_SPEED_HERE;			(Line 30)
					double advertised_download_speed = INSERT_DOWNLOAD_SPEED_HERE;		(Line 31)

			2)	Compile the java file
					a)	open up terminal
					b)	copy the following line into the terminal
							javac ~/Desktop/network_diagnostic/src/summarize.java
					c) hit enter
		*/
		double advertised_upload_speed = 100;
		double advertised_download_speed = 50;

		/*	Declaration of variables	*/
		String upload_file = "../network_speeds/" + args[0] + "/upload.txt";
		String download_file = "../network_speeds/" + args[0] + "/download.txt";
		String summary_file = "../network_speeds/" + args[0] + "/summary.txt";
		String line = "";
		String[] line_array = new String[2];

		try{
			/*	Read in the upload speeds	*/
			double count = 0;
			double speed = 0;
			double average_speed = 0;
			double under_advertised_speed = 0;
			BufferedReader br = new BufferedReader(new FileReader(upload_file));
			
			while( (line = br.readLine()) != null){
				count++;
				average_speed += speed;
				line_array = line.split(",");
				speed = Double.parseDouble(line_array[1]);

				/*	According to the lady on the phone, we are paying for 100 megabit upload speed.
					Every time we have an upload speed less than this we will take note. 
				*/
				if(speed < advertised_upload_speed){
					under_advertised_speed++;
				}
			}

			double upload_average = average_speed / count;
			double upload_under_advertised_speed = under_advertised_speed / count * 100;

			br.close();

			/*	Read in the download speeds	*/
			count = 0;
			speed = 0;
			average_speed = 0;
			under_advertised_speed = 0;
			br = new BufferedReader(new FileReader(download_file));

			while( (line = br.readLine()) != null){
				count++;
				average_speed += speed;
				line_array = line.split(",");
				speed = Double.parseDouble(line_array[1]);

				/*	According to the lady on the phone, we are paying for 50 megabit upload speed.
					Every time we have an upload speed less than this we will take note. 
				*/
				if(speed < advertised_download_speed){
					under_advertised_speed++;
				}
			}
			double download_average = average_speed / count;
			double download_under_advertised_speed = under_advertised_speed / count * 100;

			/*	Write to file and close it.	*/
			BufferedWriter bw = new BufferedWriter(new FileWriter(summary_file));
			bw.write("Average upload speed:	" + upload_average + " megabits\n");
			bw.write("Percentage of time under advertised speed:	" + upload_under_advertised_speed + "\n");
			bw.write("\n");
			bw.write("Average upload speed:	" + download_average + " megabits\n");
			bw.write("Percentage of time under advertised speed:	" + download_under_advertised_speed + "\n");
			bw.close();
		}
		
		catch(IOException e){
			System.out.println("Error: " + e);
		}	
	}
}
