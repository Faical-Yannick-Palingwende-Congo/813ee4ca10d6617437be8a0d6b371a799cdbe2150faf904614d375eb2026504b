package com.parkingchum.frontend.client.android.parkingtrack;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

public class AbstractRequest {
	private String stamp;
	private JSONObject payload = new JSONObject();
	private RequestType type;
	private JSONObject request;
	private String code;
	public String base_online = "http://www.kugri.com";//"http://10.0.0.35:8888";//"http://www.tag2tag.net";
	private boolean ok = false;
	
	public AbstractRequest(RequestType type, JSONObject request) {
		super();
		this.type = type;
		this.request = request;
	}
	
	public String getCode() {
		return code;
	}

	public String getBase_online() {
		return base_online;
	}

	public void setBase_online(String base_online) {
		this.base_online = base_online;
	}

	public String getStamp() {
		return stamp;
	}

	public JSONObject getpayload() {
		return payload;
	}

	public RequestType getType() {
		return type;
	}

	public JSONObject getRequest() {
		return request;
	}

	String type2endpoint(RequestType type){
		String endpoint = new String("/tracker/");
		switch(type){
			case ACCOUNT_REGISTER:
				endpoint += "account/register";
				break;
			case ACCOUNT_LOGIN:
				endpoint += "account/login";
				break;
			case ACCOUNT_LOGOUT:
				endpoint += "account/logout";
				break;
			case ACCOUNT_UNREGISTER:
				endpoint += "account/unregister";
				break;
			case ACCOUNT_LOST:
				endpoint += "account/lost";
				break;
			case ACCOUNT_RECOVER:
				endpoint += "account/recover";
				break;
			case ACCOUNT_UPDATE:
				endpoint += "account/update";
				break;
			case ACCOUNT_SEARCH:
				endpoint += "account/search";
				break;
			case ACTIVITY_START:
				endpoint += "activity/start";
				break;
			case ACTIVITY_END:
				endpoint += "activity/end";
				break;
			case ACTIVITY_RECORD:
				endpoint += "activity/record";
				break;
		}
		return endpoint;
	}
	
	public boolean send(){
		HttpClient client = new DefaultHttpClient();
	    String responseBody;
	    try{
	        HttpPost post = new HttpPost(base_online+""+type2endpoint(this.type));
	        StringEntity se = new StringEntity(this.request.toString()); 
	        post.setEntity(se);
	        post.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        post.setHeader("Content-type", "application/json");
	        
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        responseBody = client.execute(post, responseHandler);
	        JSONObject response = new JSONObject(responseBody);
	        Log.i("ABSTRACT_REQUEST", "RESPONSE: "+response.toString());
	        this.stamp = response.getString("stamp");
	        this.code = response.getString("code");
	        this.payload = response.getJSONObject("payload");
	        if(code.equals("100")){
	        	this.ok=true;
	        }else {
	        	this.ok = false;
	        	Log.i("ABSTRACT_REQUEST", "REQUEST FAILED: "+response.getString("payload"));
	        }
	        responseHandler = null;
	        client = null;
	    }
	    catch (Exception e) {
	    	this.ok = false;
	    	client = null;
	    	Log.i("ABSTRACT_REQUEST", "REQUEST EXCEPTION: "+Log.getStackTraceString(e));
	    }
		return this.ok;
	}
	
	public boolean getOk(){
		return this.ok;
	}
}
