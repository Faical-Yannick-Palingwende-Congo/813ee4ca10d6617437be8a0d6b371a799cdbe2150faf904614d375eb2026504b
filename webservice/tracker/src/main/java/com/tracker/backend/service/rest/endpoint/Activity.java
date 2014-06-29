package com.tracker.backend.service.rest.endpoint;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tracker.backend.service.rest.entity.Index;
import com.tracker.backend.service.rest.entity.Session;
import com.tracker.backend.service.rest.envelop.ActivityEnd;
import com.tracker.backend.service.rest.envelop.ActivityRecord;
import com.tracker.backend.service.rest.envelop.ActivityStart;
import com.tracker.backend.service.rest.envelop.BasicResponse;
import com.tracker.backend.service.rest.envelop.field.ActivityEndPayload;
import com.tracker.backend.service.rest.envelop.field.ActivityRecordPayload;
import com.tracker.backend.service.rest.envelop.field.ActivityStartPayload;
import com.tracker.backend.service.rest.envelop.field.ResponseBasicPayload;

@Path("activity")
public class Activity extends AbstractEndpoint {
	
	@POST
	@Path("/start")
	@Consumes({ "application/xml", "application/json" })
	public Response start(ActivityStart request){
		
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		ActivityStartPayload ActivityObject = request.getPayload();
		Date stamp = new Date();
		if (ActivityObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			activities = StringToActitivies(adapter.read("activity", "id,account,type,start,end"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			Session session = getSessionfromToken(ActivityObject.getToken());
			if( session == null){
				ResponseBasicPayload envelop = new ResponseBasicPayload("Activity starting failed, unknown session.");
				BasicResponse response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "200", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
			}else{
				if(session.getStatus().equals(""+status2connectivity("LOGIN"))){
					Index index  = getIndex("activity");
					long activity = Long.parseLong(index.getNext());
					this.adapter.write("activity", "id,account,type,start,end",activity +","+ session.getAccount() +","+activity2action(ActivityObject.getType())+","+dateFormat.format(stamp)+",00000000000000000");
					adapter.sync("indexer", "id", index.getId(), "next", ""+(activity+1));
					ResponseBasicPayload envelop = new ResponseBasicPayload(""+activity);
					BasicResponse response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "100", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
					return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
				}else{
					ResponseBasicPayload envelop = new ResponseBasicPayload("Activity starting failed, you need to login before.");
					BasicResponse response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "300", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
					return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
				}
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Activity start action failed, malformed request.");
			BasicResponse response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
		}
	}
	
	@POST
	@Path("/end")
	@Consumes({ "application/xml", "application/json" })
	public Response end(ActivityEnd request){
		
		BasicResponse response;
		ResponseBasicPayload envelop;
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		ActivityEndPayload ActivityObject = request.getPayload();
		Date stamp = new Date();
		if (ActivityObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			activities = StringToActitivies(adapter.read("activity", "id,account,type,start,end"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			Session session = getSessionfromToken(ActivityObject.getToken());
			if( session == null){
				envelop = new ResponseBasicPayload("Activity ending failed, unknown session.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "200", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}else{
				if(session.getStatus().equals(""+status2connectivity("LOGIN"))){
					
					com.tracker.backend.service.rest.entity.Activity activity = getActivityfromId(ActivityObject.getActivity());
					
					if(activity != null){
						if(activity.getAccount().equals(session.getAccount())){
							if(activity.getEnd().equals("00000000000000000")){
								this.adapter.sync("activity", "id", activity.getId(), "end", ""+dateFormat.format(stamp));
								envelop = new ResponseBasicPayload("Activity ending succeed, activity ended.");
								response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "100", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
							}else{
								envelop = new ResponseBasicPayload("Activity ending failed, activity already ended.");
								response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "300", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
							}
						}else{
							envelop = new ResponseBasicPayload("Activity ending failed, permission denied.");
							response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "300", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
						}
					}else{
						envelop = new ResponseBasicPayload("Activity ending failed, unknown activity.");
						response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "300", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
					}
				}else{
					envelop = new ResponseBasicPayload("Activity ending failed, you need to login before.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "300", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}
			}
		} else {
			envelop = new ResponseBasicPayload("End Activity action failed, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Path("/record")
	@Consumes({ "application/xml", "application/json" })
	public Response record(ActivityRecord request){
		
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		ActivityRecordPayload ActivityObject = request.getPayload();
		ResponseBasicPayload envelop;
		BasicResponse response;
		Date stamp = new Date();
		if (ActivityObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			Session session = getSessionfromToken(ActivityObject.getToken());
			if( session == null){
				envelop = new ResponseBasicPayload("Activity recording failed, unknown session.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "200", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}else{
				Index index  = getIndex("record_ac");
				long record_ac = Long.parseLong(index.getNext());
				this.adapter.write("record_ac", "id,activity,step,ax,ay,az",record_ac +","+ ActivityObject.getActivity() +","+ActivityObject.getStep()+","+ActivityObject.getAx()+","+ActivityObject.getAy()+","+ActivityObject.getAz());
				adapter.sync("indexer", "id", index.getId(), "next", ""+(record_ac+1));
				
				index  = getIndex("record_gy");
				long record_gy = Long.parseLong(index.getNext());
				this.adapter.write("record_gy", "id,activity,step,pitch,roll,azimuth",record_gy +","+ ActivityObject.getActivity() +","+ActivityObject.getStep()+","+ActivityObject.getPitch()+","+ActivityObject.getRoll()+","+ActivityObject.getAzimuth());
				adapter.sync("indexer", "id", index.getId(), "next", ""+(record_gy+1));
				
				index  = getIndex("record_sp");
				long record_sp = Long.parseLong(index.getNext());
				this.adapter.write("record_sp", "id,activity,step,sp",record_sp +","+ ActivityObject.getActivity() +","+ActivityObject.getStep()+","+ActivityObject.getSpeed());
				adapter.sync("indexer", "id", index.getId(), "next", ""+(record_sp+1));
				
				index  = getIndex("record_po");
				long record_po = Long.parseLong(index.getNext());
				this.adapter.write("record_po", "id,activity,step,la,lo",record_po +","+ ActivityObject.getActivity() +","+ActivityObject.getStep()+","+ActivityObject.getLatitude()+","+ActivityObject.getLongitude());
				adapter.sync("indexer", "id", index.getId(), "next", ""+(record_po+1));
				envelop = new ResponseBasicPayload("Activity recording succeed, Thank you contributing on Parkingchum tracking.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "100", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}
		} else {
			envelop = new ResponseBasicPayload("Record activity action, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
}
