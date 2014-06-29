#!/bin/bash

for  i in $(seq 1 100)
do
	base=$[100+(RANDOM%100)]$[1000+(RANDOM%1000)]
	lat_base=`echo 39.${base:1:2}${base:4:3}${base:6:5}`
	base=$[100+(RANDOM%100)]$[1000+(RANDOM%1000)]
	lon_base=`echo -77.${base:1:2}${base:4:3}${base:6:5}`
	
	base=$[100+(RANDOM%100)]$[1000+(RANDOM%1000)]
	speed_base=`echo 0.${base:1:2}`
	
	base=$[100+(RANDOM%100)]$[1000+(RANDOM%1000)]
	ax_base=`echo ${base:1:1}.${base:4:3}`
	base=$[100+(RANDOM%100)]$[1000+(RANDOM%1000)]
	ay_base=`echo ${base:1:1}.${base:4:3}`
	base=$[100+(RANDOM%100)]$[1000+(RANDOM%1000)]
	az_base=`echo ${base:1:1}.${base:4:3}`
	
	echo 'Moving to --> ('$lat_base','$lon_base')'
	echo 'Speed to --> '$speed_base
	echo 'Accelerometer to -->('$ax_base','$ay_base','$az_base')'
	
	echo
	
	post=`echo '{"stamp":"20140329143750","payload":{"token":"12e43c1dc70475ac14e2d5df3431626c7ef84995e1f3795499c06fc656b445ba","activity":"1","step":"'$i'","ax":"'$ax_base'","ay":"'$ay_base'","az":"'$az_base'","speed":"'$speed_base'","latitude":"'$lat_base'","longitude":"'$lon_base'"}}'`
	
	curl -H "Content-Type: application/json" -d $post http://127.0.0.1:8888/tracker/activity/record
done