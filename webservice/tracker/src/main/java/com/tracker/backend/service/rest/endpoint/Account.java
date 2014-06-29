package com.tracker.backend.service.rest.endpoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tracker.backend.service.rest.entity.Index;
import com.tracker.backend.service.rest.entity.Session;
import com.tracker.backend.service.rest.envelop.AccountContactUs;
import com.tracker.backend.service.rest.envelop.AccountEmail;
import com.tracker.backend.service.rest.envelop.AccountLogin;
import com.tracker.backend.service.rest.envelop.AccountRecover;
import com.tracker.backend.service.rest.envelop.AccountRegister;
import com.tracker.backend.service.rest.envelop.AccountSearch;
import com.tracker.backend.service.rest.envelop.AccountSearchResponse;
import com.tracker.backend.service.rest.envelop.AccountStateResponse;
import com.tracker.backend.service.rest.envelop.AccountToken;
import com.tracker.backend.service.rest.envelop.AccountUpdate;
import com.tracker.backend.service.rest.envelop.BasicResponse;
import com.tracker.backend.service.rest.envelop.field.AccountContactUsPayload;
import com.tracker.backend.service.rest.envelop.field.AccountEmailPayload;
import com.tracker.backend.service.rest.envelop.field.AccountLoginPayload;
import com.tracker.backend.service.rest.envelop.field.AccountRecoverPayload;
import com.tracker.backend.service.rest.envelop.field.AccountRegisterPayload;
import com.tracker.backend.service.rest.envelop.field.AccountSearchField;
import com.tracker.backend.service.rest.envelop.field.AccountSearchPayload;
import com.tracker.backend.service.rest.envelop.field.AccountTokenPayload;
import com.tracker.backend.service.rest.envelop.field.AccountUpdatePayload;
import com.tracker.backend.service.rest.envelop.field.ResponseAccountSateField;
import com.tracker.backend.service.rest.envelop.field.ResponseAccountSearchField;
import com.tracker.backend.service.rest.envelop.field.ResponseAccountSearchPayload;
import com.tracker.backend.service.rest.envelop.field.ResponseAccountStatePayload;
import com.tracker.backend.service.rest.envelop.field.ResponseBasicPayload;

//The next big evolution is to change the payload to a json object and create a collector table that contains id table ids. It allows the recuperation of removed indexes
@Path("account")
public class Account extends AbstractEndpoint {
	
	@POST
	@Path("/register")
	@Consumes({ "application/xml", "application/json" })
	public Response register(AccountRegister request){
		
		BasicResponse response;
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		AccountRegisterPayload RegisterObject = request.getPayload();
		Date stamp = new Date();
		if (RegisterObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			ResponseBasicPayload envelop;
			long account = (getAccountfromEmail(RegisterObject.getEmail()) == null?0:Integer.parseInt(getAccountfromEmail(RegisterObject.getEmail()).getId()));
			if(account != 0){
				envelop = new ResponseBasicPayload("Registering failed, this email is already used. Please if you have registered use your default password or register. We recommand you to get a new password in the first case.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"200" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}else{
				Index index  = getIndex("account");
				account = Long.parseLong(index.getNext());
				this.adapter.write("account", "id,name,email,password", account +","+RegisterObject.getName()+","+RegisterObject.getEmail()+","+sha256(RegisterObject.getPassword(), "Tracker2014Password"));
				adapter.sync("indexer", "id", index.getId(), "next", ""+(account+1));
				index  = getIndex("session");
				long session = Long.parseLong(index.getNext());
				this.adapter.write("session", "account,status,token,stamp",account +","+""+status2connectivity("REGISTERED")+","+generateToken(RegisterObject.getEmail(), dateFormat.format(stamp))+","+dateFormat.format(stamp));
				adapter.sync("indexer", "id", index.getId(), "next", ""+(session+1));
				envelop = new ResponseBasicPayload("Your user account has been create successfully.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"100" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				sendMail("text", "noreply@parkingchum.com", RegisterObject.getEmail(), "Tracker Notifications", "Your user account has been create successfully.");
		   }
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Register account action failed, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Path("/login")
	@Consumes({ "application/xml", "application/json" })
	public Response login(AccountLogin request){
		
		BasicResponse response;
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		AccountLoginPayload LoginObject = request.getPayload();
		Date stamp = new Date();
		if (LoginObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			ResponseBasicPayload envelop;
			if(authentificate(LoginObject.getEmail(), LoginObject.getPassword())){
				com.tracker.backend.service.rest.entity.Account account = getAccountfromEmail(LoginObject.getEmail());
				Session session = account2Session(account.getId());
				if(session.getStatus().equals(""+status2connectivity("RECOVER"))){
					envelop = new ResponseBasicPayload("Login failed, you need to setup your new password first.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"300" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}else if(session.getStatus().equals(""+status2connectivity("LANDED"))){
					envelop = new ResponseBasicPayload("Login failed, you need to wait for our release to be able to use the app.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "500", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}else if(session.getStatus().equals(""+status2connectivity("CONFIRM"))){
					envelop = new ResponseBasicPayload("Login failed, your account has been just unlocked to use the app. Please try to create a new password by declaring it lost and setting up a new one.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"600" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}else if(session.getStatus().equals(""+status2connectivity("LOGIN")) || session.getStatus().equals(""+status2connectivity("LOGOUT")) || session.getStatus().equals(""+status2connectivity("REGISTERED"))){
					String token = generateToken(account.getEmail(), dateFormat.format(stamp));
					this.adapter.sync("session", "account", session.getAccount(),"status,token,stamp",""+status2connectivity("LOGIN")+","+token+","+dateFormat.format(stamp));
					envelop = new ResponseBasicPayload(token);
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"100" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}else{
					envelop = new ResponseBasicPayload("Login failed, your status is not consistent. Please contact us.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"700" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}
			}else{
				envelop = new ResponseBasicPayload("Login failed. Bad email or password. If you are registered automatically try to set a new password.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"400" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Login account action failed, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Path("/logout")
	@Consumes({ "application/xml", "application/json" })
	public Response logout(AccountToken request){
		
		BasicResponse response;
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		AccountTokenPayload LogoutObject = request.getPayload();
		Date stamp = new Date();
		if (LogoutObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			ResponseBasicPayload envelop;
			Session session = getSessionfromToken(LogoutObject.getToken());
			if(session == null){
				envelop = new ResponseBasicPayload("Logout failed, you need to have an account.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"200" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}else{
				if(session.getStatus().equals(""+status2connectivity("LOGIN"))){
					this.adapter.sync("session", "account", session.getAccount(),"status,token,stamp",""+status2connectivity("LOGOUT")+","+LogoutObject.getToken()+","+dateFormat.format(stamp));
					envelop = new ResponseBasicPayload("You have been logout.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"100" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}else{
					envelop = new ResponseBasicPayload("Logout failed, you need to login before.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"300" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Logout account action failed, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Path("/unregister")
	@Consumes({ "application/xml", "application/json" })
	public Response unregister(AccountToken request){
		
		BasicResponse response;
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		AccountTokenPayload LogoutObject = request.getPayload();
		Date stamp = new Date();
		if (LogoutObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			ResponseBasicPayload envelop;
			Session session = getSessionfromToken(LogoutObject.getToken());
			if(session == null){
				envelop = new ResponseBasicPayload("Unregistering failed, you need to have an account.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"200" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}else{
				if(session.getStatus().equals(""+status2connectivity("LOGIN"))){
					com.tracker.backend.service.rest.entity.Account account = getAccountfromId(session.getAccount());
					this.adapter.sync("spot", "creator", account.getId(),"status",""+0);
					this.adapter.delete("session", "account", account.getId());
					this.adapter.delete("account", "id", account.getId());
					envelop = new ResponseBasicPayload("You have been unregistered.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"100" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}else{
					envelop = new ResponseBasicPayload("Unregistering failed, you need to login before.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"300" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Unregister account action failed, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Path("/status")
	@Consumes({ "application/xml", "application/json" })
	public Response status(AccountToken request){
		
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		AccountTokenPayload LogoutObject = request.getPayload();
		Date stamp = new Date();
		if (LogoutObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			Session session = getSessionfromToken(LogoutObject.getToken());
			if(session == null){
				ResponseBasicPayload envelop = new ResponseBasicPayload("State failed, you need to have a valid key.");
				BasicResponse response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "200", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
			}else{
				ResponseAccountSateField context = new ResponseAccountSateField(session.getStatus());
				ResponseAccountStatePayload envelop = new ResponseAccountStatePayload( context);//<<<<
				AccountStateResponse response = new AccountStateResponse(AbstractEndpoint.dateFormat.format(stamp), "100", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Status account action failed, malformed request.");
			BasicResponse response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
		}
	}
	
	@POST
	@Path("/lost")
	@Consumes({ "application/xml", "application/json" })
	public Response lost(AccountEmail request){
		
		BasicResponse response;
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		AccountEmailPayload LostObject = request.getPayload();
		Date stamp = new Date();
		if (LostObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			ResponseBasicPayload envelop;
			long account = (getAccountfromEmail(LostObject.getEmail()) == null?0:Integer.parseInt(getAccountfromEmail(LostObject.getEmail()).getId()));
			if( account == 0){
				envelop = new ResponseBasicPayload("Account losting failed, no account associated.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"200" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}else{
				Session session = getsessionfromAccount(""+account);
				if(session.getStatus().equals(""+status2connectivity("LOGIN"))){//Let's reconcider this case here where you can delete the app and forget to logout. WHich might block you.
					// A will advise a renew key slighly equal to login but only the session key is renewed.
					envelop = new ResponseBasicPayload("Account losting failed. You are logged in.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"300" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}else if(session.getStatus().equals(""+status2connectivity("LANDED"))){
					envelop = new ResponseBasicPayload("you need to wait for our release to be able to use the app.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"400" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}else{
				
					String token = renewToken(LostObject.getEmail(), stamp, ""+""+status2connectivity("RECOVER"));
					//this.adapter.sync("session", "account", ""+account,"pass,stamp,status",token+","+dateFormat.format(stamp)+","+""+status2connectivity("RECOVER"));
					envelop = new ResponseBasicPayload(token);
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"100" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
					sendMail("html", "noreply@parking.com", LostObject.getEmail(), "Tracker Password Lost",
							"<form name=\"passwordRecovery\" action=\"#\" method=\"POST\"><div id=\"sectionPassword\">newPassword: <input type=\"password\" id=\"pass\" name=\"newPassword\"/><input type=\"submit\" id=\"login\" value=\"Recover\"/></div></form><script type=\"text/javascript\">var pass=$('#pass').val(); $('login').bind('click', function() { $.ajax('http://localhost:8080/drop/account/recover', {type: 'POST',contentType: 'text/json',data: JSON.stringify({key:\""+token
							 +"\", newPassword:pass}), complete: function() { document.getElementById('sectionPassword').innerHTML = \"<h1>New password confirmed.</h1>\";}});return false;});</script>");
				}
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Lost account action failed, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Path("/recover")
	@Consumes({ "application/xml", "application/json" })
	public Response recover(AccountRecover request){
		
		BasicResponse response;
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");
		AccountRecoverPayload RecoverObject = request.getPayload();
		Date stamp = new Date();
		if (RecoverObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			ResponseBasicPayload envelop;
			Session session = getSessionfromToken(RecoverObject.getToken());
			if( session == null){
				envelop = new ResponseBasicPayload("Account recovering failed, unknown session.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"200" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}else{
				com.tracker.backend.service.rest.entity.Account account = getAccountfromId(session.getAccount());
				if(session.getStatus().equals(""+status2connectivity("RECOVER"))){
					String token = sha256(RecoverObject.getNewPassword(), "Tracker2014Password");
					renewToken(account.getEmail(), stamp, ""+""+status2connectivity("LOGOUT"));
					adapter.sync("account", "id", account.getId(), "password", token);
					envelop = new ResponseBasicPayload("Your password has been successfully changed.");			
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"100" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
					sendMail("text", "noreply@parkingchum.com", account.getEmail(), "Tracker Password Renewed", "Your password has been successfully changed.");
				}else{
					envelop = new ResponseBasicPayload("Account recovering failed, you need to declare your account as lost first.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"300" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Recover account action failed, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Path("/update")
	@Consumes({ "application/xml", "application/json" })
	public Response update(AccountUpdate request){
		
		BasicResponse response;
		//String payload = request.payload;//aes256Decrypt(request.payload, "0123456789abcdef");///////Make a separate field called token which is NONE for not admins.
		AccountUpdatePayload RecoverObject = request.getPayload();
		Date stamp = new Date();
		if (RecoverObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			ResponseBasicPayload envelop;
			Session session = getSessionfromToken(RecoverObject.getToken());
			if( session == null){
				envelop = new ResponseBasicPayload("Account updating failed, unknown session.");
				response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"200" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			}else{
				com.tracker.backend.service.rest.entity.Account account = getAccountfromId(session.getAccount());
				if(session.getStatus().equals(""+status2connectivity("LOGIN"))){
					if(RecoverObject.getNewName() != null){
						adapter.sync("account", "id", account.getId(), "name", RecoverObject.getNewName());
					}
					if(RecoverObject.getNewPassword() != null){
						String token = sha256(RecoverObject.getNewPassword(), "Tracker2014Password");
						adapter.sync("account", "id", account.getId(), "password", token);
					}
					envelop = new ResponseBasicPayload("Your account has been successfully changed.");			
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"100" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
					sendMail("text", "noreply@parkingchum.com", account.getEmail(), "Tracker account Updated", "Your account has been successfully changed.");
				}else{
					envelop = new ResponseBasicPayload("Account updating failed, you need to login before doing this.");
					response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp),"300" , envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				}
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Update account action failed, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	@POST
	@Path("/search")
	@Consumes({ "application/xml", "application/json" })
	public Response search(AccountSearch request){
		
		//String payload = request.payload;aes256Decrypt(request.payload, "0123456789abcdef");
		AccountSearchPayload ResearchObject = request.getPayload();
		Date stamp = new Date();
		if (ResearchObject.sanity()) {
			accounts = StringToAccounts(adapter.read("account", "id,name,email,password"));
			sessions = StringToSessions(adapter.read("session", "account,status,token,stamp"));
			indexes = StringToIndexes(adapter.read("indexer", "id,table,next"));
			Session session = getSessionfromToken(ResearchObject.getToken());
			if( session == null){
				ResponseBasicPayload envelop = new ResponseBasicPayload("Search failed, your key is corrupted.");
				BasicResponse response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "200",envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
			}else{
				List<ResponseAccountSearchField> context = new ArrayList<ResponseAccountSearchField>();
				ResponseAccountSearchPayload envelop;
				AccountSearchField pattern = ResearchObject.getPattern();
				if(pattern.getField() != null){
					
					for(com.tracker.backend.service.rest.entity.Account acc: accounts){
						if(pattern.getField().toUpperCase().equals("EMAIL")){
							Session sess = account2Session(acc.getId());
							if(acc.getEmail().contains(pattern.getValue())){
								context.add(new ResponseAccountSearchField(acc.email, sess.getStatus()));
							}
						}
						if(pattern.getField().toUpperCase().equals("STATUS")){
							Session sess = account2Session(acc.getId());
							if(sess.getStatus().equals(pattern.getValue())){
								context.add(new ResponseAccountSearchField(acc.email, sess.getStatus()));
							}
						}
						if(pattern.getField().toUpperCase().equals("NAME")){
							Session sess = account2Session(acc.getId());
							if(acc.getName().contains(pattern.getValue())){
								context.add(new ResponseAccountSearchField(acc.email, sess.getStatus()));
							}
						}
					}
					
					envelop = new ResponseAccountSearchPayload(context);
							
				}else{
					for(com.tracker.backend.service.rest.entity.Account acc: accounts){
						Session sess = account2Session(acc.getId());
						context.add(new ResponseAccountSearchField(acc.email, sess.getStatus()));
					}
					envelop = new ResponseAccountSearchPayload(context);
				}
				AccountSearchResponse response = new AccountSearchResponse(AbstractEndpoint.dateFormat.format(stamp), "100", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
				return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
			}
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Search account action searching failed, malformed request.");
			BasicResponse response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
			return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
		}
	}
	
	@POST
	@Path("/contactus")
	@Consumes({ "application/xml", "application/json" })
	public Response contactus(AccountContactUs request){
		BasicResponse response;
		//String payload = request.payload;aes256Decrypt(request.payload, "0123456789abcdef");
		AccountContactUsPayload ContactUsObject = request.payload;
		Date stamp = new Date();
		if (ContactUsObject.sanity()) {
			sendMail("TEXT", ContactUsObject.getEmail(), "contactus@parkingchum.com", ContactUsObject.getTitle(), ContactUsObject.getContent());
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "100", new ResponseBasicPayload("Your message has been sent successfully. Verify that you have a copy in your email client."));//aes256Encrypt(envelop, "0123456789abcdef"));
		} else {
			ResponseBasicPayload envelop = new ResponseBasicPayload("Your message has not been sent, malformed request.");
			response = new BasicResponse(AbstractEndpoint.dateFormat.format(stamp), "000", envelop);//aes256Encrypt(envelop, "0123456789abcdef"));
		}
		return Response.ok(response, MediaType.APPLICATION_JSON_TYPE).build();
	}
}
