#!/bin/bash

months=(Dec Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov)

while :
	do
		# Variable declarations.
		day=`date '+%d'`
		month_year=`date '+%b_%Y'`
		date_and_time=`date '+%Y-%m-%d;%H:%M:%S'`
		download_speed="$(curl -s https://raw.githubusercontent.com/sivel/speedtest-cli/master/speedtest.py | python - | grep Download)"
		download_speed=$(echo "$download_speed" | grep -o -E '[0-9]*\.?[0-9]{2}')
		upload_speed="$(curl -s https://raw.githubusercontent.com/sivel/speedtest-cli/master/speedtest.py | python - | grep Upload)"
		upload_speed=$(echo "$upload_speed" | grep -o -E '[0-9]*\.?[0-9]{2}')

		# Make directory for each month.
		if [[ day -eq 01 ]]; then
			mkdir network_speeds/$month_year
		fi

		# Write speeds to network_speeds/Month_year directory
		echo "$date_and_time,$download_speed" >> ../network_speeds/$month_year/download.txt
		echo "$date_and_time,$upload_speed" >> ../network_speeds/$month_year/upload.txt

		# On the first of each month, make a summary file using the java program 'summarize.java' 
		# for the previous month. Then, zip the upload and download files and delete originals to 
		# save space.
		if [[ day -eq 01 ]]; then
			
			j=`date +%m`                
			i=`expr $j - 1`              
			prev_month=`echo ${months[$i]}`
			year=`date '+%Y'`

			java summarize $prev_month\_$year

			zip ../network_speeds/$prev_month\_$year/download.zip ../network_speeds/$prev_month\_$year/download.txt
			zip ../network_speeds/$prev_month\_$year/upload.zip ../network_speeds/$prev_month\_$year/upload.txt

			rm ../network_speeds/$prev_month\_$year/download.txt
			rm ../network_speeds/$prev_month\_$year/upload.txt
		fi

		# Wait 300 seconds = 5 minutes
		sleep 300
	done