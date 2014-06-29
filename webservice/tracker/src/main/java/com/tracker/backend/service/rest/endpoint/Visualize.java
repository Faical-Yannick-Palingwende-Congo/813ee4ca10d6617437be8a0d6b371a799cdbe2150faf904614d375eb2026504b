package com.tracker.backend.service.rest.endpoint;

import java.text.ParseException;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.tracker.backend.service.rest.entity.Activity;
import com.tracker.backend.service.rest.entity.RecordAc;
import com.tracker.backend.service.rest.entity.RecordGy;
import com.tracker.backend.service.rest.entity.RecordPo;
import com.tracker.backend.service.rest.entity.RecordSp;

@Path("visualize")
public class Visualize extends AbstractEndpoint{

	@GET
	@Path("/{activity}/records")
	@Produces(MediaType.TEXT_HTML)
	public String records(@PathParam("activity") String activity){//Return the activity start and end and the number of records
		activities = StringToActitivies(adapter.read("activity", "id,account,type,start,end"));
		recordSp = StringToRecordSps(adapter.read("record_sp", "id,activity,step,sp"));
		LinkedList<String> act_starts = new LinkedList<String>();
		LinkedList<String> act_ends = new LinkedList<String>();
		LinkedList<String> act_ids = new LinkedList<String>();
		LinkedList<String> act_sizes = new LinkedList<String>();
		long total_records = 0;
		long total_activities = 0;
		
		LOGGER.pushDebugs("Request activity parameter: "+activity.toUpperCase()+" - the id is: "+activity2action(activity.toUpperCase()));
		
		for(Activity acti: activities){
			LOGGER.pushDebugs("activity type: "+acti.getType());
			if(acti.getType().equals(""+activity2action(activity.toUpperCase()))){
				int contents = 0;
				for(RecordSp speed: recordSp){
					if(speed.getActivity().equals(acti.getId())){
						contents++;
						total_records++;
					}
				}
				try {
					act_starts.add(viewFormat.format(dateFormat.parse(acti.getStart())));
					act_ends.add(viewFormat.format(dateFormat.parse(acti.getEnd())));
				} catch (ParseException e) {
					act_starts.add("Unknown");
					act_ends.add("Unknown");
					LOGGER.pushErrors(e, "Formatting start and end date");
				}
				act_ids.add(acti.getId());
				act_sizes.add(""+contents);
				total_activities++;
			}
		}
		
		if(total_activities == 0){
			return "<html> " + "<title>" + "Research results for: "+ activity.toUpperCase() + " activity(ies) </title>"
			        + "<body><h1>" + "404 Error!!! Ooops no corresponding activity!! Please choose between the following one: WALKING, JOGGING, RUNNING,..." + "</body></h1>" + "</html> ";
		}else{
			String wrapping="<dl>";
			for(int i=0;i<total_activities;i++){
				wrapping += "<dt>Activity "+act_ids.get(i)+"</dt>";
				wrapping += "<dd>Start date: "+act_starts.get(i)+"</dd>";
				wrapping += "<dd>End date: "+act_ends.get(i)+"</dd>";
				wrapping += "<dd>Total records: "+act_sizes.get(i)+"</dd>";
			}
			wrapping += "</dl>";
			
			return "<html> " + "<title>" + "Research results for: "+ activity.toUpperCase() + " activity(ies)</title>"
	        + "<body><h1>"+activity.toUpperCase()+" Tracker records browser"+"</h1>" + "<ul><li>Total activities: "+total_activities+"</li><li>Total records: "+total_records+"</li></ul></br>"+wrapping+"</body>" + "</html> ";
		}
	}
	

	
	@GET
	@Path("/csv/{activity}")
	@Produces(MediaType.TEXT_PLAIN)
	public String recordCsvAll(@PathParam("activity") String activity){
		activities = StringToActitivies(adapter.read("activity", "id,account,type,start,end"));
		recordAc = StringToRecordAcs(adapter.read("record_ac", "id,activity,step,ax,ay,az"));
		recordGy = StringToRecordGys(adapter.read("record_gy", "id,activity,step,pitch,roll,azimuth"));
		recordPo = StringToRecordPos(adapter.read("record_po", "id,activity,step,la,lo"));
		recordSp = StringToRecordSps(adapter.read("record_sp", "id,activity,step,sp"));
		
		if(activity == null){
			return "Something went wrong!!! You have to specify the recorded activity type " ;
		}else{
			LOGGER.pushDebugs("Request activity parameter: "+activity.toUpperCase()+" - the id is: "+activity2action(activity.toUpperCase()));
			LinkedList<String> csvContent = new LinkedList<String>();
			csvContent.add("\"activity\",\"step\",\"ax\",\"ay\",\"az\",\"pitch\",\"roll\",\"azimuth\",\"longitude\",\"latitude\",\"speed\"");
			for(Activity acti: activities){
				LOGGER.pushDebugs("activity type: "+acti.getType());
				if(acti.getType().equals(""+activity2action(activity.toUpperCase()))){
					//LOGGER.pushDebugs("\"activity\",\"step\",\"ax\",\"ay\",\"az\",\"pitch\",\"roll\",\"azimuth\",\"longitude\",\"latitude\",\"speed\"");
					for(RecordAc accelerometer: recordAc){
						if(accelerometer.getActivity().equals(acti.getId())){
							RecordGy gyro = recordGy.get(Integer.parseInt(accelerometer.getId()) - 1);
							RecordPo posi = recordPo.get(Integer.parseInt(accelerometer.getId()) - 1);
							RecordSp spee = recordSp.get(Integer.parseInt(accelerometer.getId()) - 1);
							csvContent.add("\""+accelerometer.getActivity()+"\",\""+accelerometer.getStep()+"\",\""+accelerometer.getAx()+"\",\""+accelerometer.getAy()+"\",\""+accelerometer.getAz()+"\",\""+gyro.getPitch()+"\",\""+gyro.getRoll()+"\",\""+gyro.getAzimuth()+"\",\""+posi.getLo()+"\",\""+posi.getLa()+"\",\""+spee.getSp()+"\"");
							//LOGGER.pushDebugs("\""+accelerometer.getActivity()+"\",\""+accelerometer.getStep()+"\",\""+accelerometer.getAx()+"\",\""+accelerometer.getAy()+"\",\""+accelerometer.getAz()+"\",\""+gyro.getPitch()+"\",\""+gyro.getRoll()+"\",\""+gyro.getAzimuth()+"\",\""+posi.getLo()+"\",\""+posi.getLa()+"\",\""+spee.getSp()+"\"");
						}
					}
				}
			}
			return StringUtils.join(csvContent, "\n");
		}
	}
	
	@GET
	@Path("/csv/{activity}/{component}")
	@Produces(MediaType.TEXT_PLAIN)
	public String recordCsvComponent(@PathParam("activity") String activity, @PathParam("component") String component){
		activities = StringToActitivies(adapter.read("activity", "id,account,type,start,end"));
		recordAc = StringToRecordAcs(adapter.read("record_ac", "id,activity,step,ax,ay,az"));
		recordGy = StringToRecordGys(adapter.read("record_gy", "id,activity,step,pitch,roll,azimuth"));
		recordPo = StringToRecordPos(adapter.read("record_po", "id,activity,step,la,lo"));
		recordSp = StringToRecordSps(adapter.read("record_sp", "id,activity,step,sp"));
				
		if(activity == null || component == null){
			return "<html> " + "<title>" + "Research results for: activity with the type "+activity+" record</title>"
			        + "<body><h1>" + "404 Error!!! Ooops no corresponding activity!! Please choose between the following one: WALKING, JOGGING, RUNNING,..." + "</body></h1>" + "</html> ";
		}else{
			
			LinkedList<String> csvContent = new LinkedList<String>();
			if(component.toUpperCase().equals("ACCELEROMETER")){
				csvContent.add("\"activity\",\"step\",\"ax\",\"ay\",\"az\"");
				//LOGGER.pushDebugs("\"activity\",\"step\",\"ax\",\"ay\",\"az\"");
				for(Activity acti: activities){
					if(acti.getType().equals(""+activity2action(activity.toUpperCase()))){
						for(RecordAc accelerometer: recordAc){
							if(accelerometer.getActivity().equals(acti.getId())){
								csvContent.add("\""+accelerometer.getActivity()+"\",\""+accelerometer.getStep()+"\",\""+accelerometer.getAx()+"\",\""+accelerometer.getAy()+"\",\""+accelerometer.getAz()+"\"");
								//LOGGER.pushDebugs("\""+accelerometer.getActivity()+"\",\""+accelerometer.getStep()+"\",\""+accelerometer.getAx()+"\",\""+accelerometer.getAy()+"\",\""+accelerometer.getAz()+"\"");
							}
						}
					}
				}
				return StringUtils.join(csvContent, "\n");
			}else if(component.toUpperCase().equals("GYROSCOPE")){
				for(Activity acti: activities){
					if(acti.getType().equals(""+activity2action(activity.toUpperCase()))){
						csvContent.add("\"activity\",\"step\",\"pitch\",\"roll\",\"azimuth\"");
						//LOGGER.pushDebugs("\"activity\",\"step\",\"pitch\",\"roll\",\"azimuth\"");
						for(RecordGy gyroscope: recordGy){
							if(gyroscope.getActivity().equals(acti.getId())){
								csvContent.add("\""+gyroscope.getActivity()+"\",\""+gyroscope.getStep()+"\",\""+gyroscope.getPitch()+"\",\""+gyroscope.getRoll()+"\",\""+gyroscope.getAzimuth()+"\"");
								//LOGGER.pushDebugs("\""+gyroscope.getActivity()+"\",\""+gyroscope.getStep()+"\",\""+gyroscope.getPitch()+"\",\""+gyroscope.getRoll()+"\",\""+gyroscope.getAzimuth()+"\"");
							}
						}
					}
				}
				return StringUtils.join(csvContent, "\n");
			}else if(component.toUpperCase().equals("POSITION")){
				for(Activity acti: activities){
					if(acti.getType().equals(""+activity2action(activity.toUpperCase()))){
						csvContent.add("\"activity\",\"step\",\"longitude\",\"latitude\"");
						//LOGGER.pushDebugs("\"activity\",\"step\",\"longitude\",\"latitude\"");
						for(RecordPo position: recordPo){
							if(position.getActivity().equals(acti.getId())){
								csvContent.add("\""+position.getActivity()+"\",\""+position.getStep()+"\",\""+position.getLo()+"\",\""+position.getLa()+"\"");
								//LOGGER.pushDebugs("\""+position.getActivity()+"\",\""+position.getStep()+"\",\""+position.getLo()+"\",\""+position.getLa()+"\"");
							}
						}
					}
				}
				return StringUtils.join(csvContent, "\n");
			}else if(component.toUpperCase().equals("SPEED")){
				for(Activity acti: activities){
					if(acti.getType().equals(""+activity2action(activity.toUpperCase()))){
						csvContent.add("\"activity\",\"step\",\"speed\"");
						//LOGGER.pushDebugs("\"activity\",\"step\",\"speed\"");
						for(RecordSp speed: recordSp){
							if(speed.getActivity().equals(acti.getId())){
								csvContent.add("\""+speed.getActivity()+"\",\""+speed.getStep()+"\",\""+speed.getSp()+"\"");
								//LOGGER.pushDebugs("\""+speed.getActivity()+"\",\""+speed.getSp()+"\"");
							}
						}
					}
				}
				return StringUtils.join(csvContent, "\n");
			}else{
				return "Something went wrong!!! Unknown component. Please provide one of [accelerometer, gyroscope, position, speed] ";
			}
		}
	}
	
	@GET
	@Path("/graph/{activity}")
	@Produces(MediaType.TEXT_HTML)
	public String recordGraphAll(@PathParam("activity") String activity){
		activities = StringToActitivies(adapter.read("activity", "id,account,type,start,end"));
		recordAc = StringToRecordAcs(adapter.read("record_ac", "id,activity,step,ax,ay,az"));
		recordGy = StringToRecordGys(adapter.read("record_gy", "id,activity,step,pitch,roll,azimuth"));
		recordPo = StringToRecordPos(adapter.read("record_po", "id,activity,step,la,lo"));
		recordSp = StringToRecordSps(adapter.read("record_sp", "id,activity,step,sp"));
		
		if(activity == null){
			return "Something went wrong!!! You have to specify the recorded activity type " ;
		}else{
			
			return "";
		}
	}
	
	@GET
	@Path("/graph/{activity}/{component}")
	@Produces(MediaType.TEXT_HTML)
	public String recordGraphComponent(@PathParam("activity") String activity, @PathParam("component") String component){
		activities = StringToActitivies(adapter.read("activity", "id,account,type,start,end"));
		recordAc = StringToRecordAcs(adapter.read("record_ac", "id,activity,step,ax,ay,az"));
		recordGy = StringToRecordGys(adapter.read("record_gy", "id,activity,step,pitch,roll,azimuth"));
		recordPo = StringToRecordPos(adapter.read("record_po", "id,activity,step,la,lo"));
		recordSp = StringToRecordSps(adapter.read("record_sp", "id,activity,step,sp"));
				
		if(activity == null || component == null){
			return "<html> " + "<title>" + "No graph to generate: activity with the type "+activity+" record</title>"
			        + "<body><h1>" + "404 Error!!! Ooops no corresponding activity!! Please choose between the following one: WALKING, JOGGING, RUNNING,..." + "</body></h1>" + "</html> ";
		}else{
			return "";
		}
	}
}
