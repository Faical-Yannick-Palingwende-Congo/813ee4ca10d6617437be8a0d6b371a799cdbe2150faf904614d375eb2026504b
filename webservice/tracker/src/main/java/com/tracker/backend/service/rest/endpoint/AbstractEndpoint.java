package com.tracker.backend.service.rest.endpoint;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.tracker.backend.service.rest.entity.AbstractRecord;
import com.tracker.backend.service.rest.entity.Account;
import com.tracker.backend.service.rest.entity.Activity;
import com.tracker.backend.service.rest.entity.Index;
import com.tracker.backend.service.rest.entity.RecordAc;
import com.tracker.backend.service.rest.entity.RecordGy;
import com.tracker.backend.service.rest.entity.RecordPo;
import com.tracker.backend.service.rest.entity.RecordSp;
import com.tracker.backend.service.rest.entity.Session;
import com.tracker.backend.service.rest.utils.AES;
import com.tracker.backend.service.rest.utils.Configuration;
import com.tracker.backend.service.rest.utils.DBAdapter;
import com.tracker.backend.service.rest.utils.Logger;
import com.tracker.backend.service.rest.utils.exception.TechnicalUtilsException;

public abstract class AbstractEndpoint {
	public static final Logger LOGGER = new Logger();
	public static DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
	public static DateFormat viewFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
	public LinkedList<com.tracker.backend.service.rest.entity.Account> accounts;
	public LinkedList<com.tracker.backend.service.rest.entity.Activity> activities;
	public LinkedList<RecordAc> recordAc;
	public LinkedList<RecordGy> recordGy;
	public LinkedList<RecordSp> recordSp;
	public LinkedList<RecordPo> recordPo;
	public LinkedList<Index> indexes;
	public LinkedList<Session> sessions;
	
	static Configuration confloader = null;
	static {
		confloader = new Configuration();
		try {
			confloader.loadProperties("internal.service.properties");
		} catch (TechnicalUtilsException e) {
			LOGGER.pushErrors(e, "Internal properties file not found.");
		}
	}
	final String DATABASE_NAME = confloader.getLoader().getProperty("database.name.value");
	final String LOGIN_NAME = confloader.getLoader().getProperty("user.login.value");
	final String LOGIN_PASSWORD = confloader.getLoader().getProperty("user.password.value");

	public DBAdapter adapter = new DBAdapter("jdbc:mysql://localhost/" + DATABASE_NAME, LOGIN_NAME, LOGIN_PASSWORD, LOGGER);
	
	public LinkedList<Activity> StringToActitivies(String content) {
		String rows[] = content.split("\n");
		LinkedList<Activity> contents = new LinkedList<Activity>();
		for (String row : rows) {
			String fields[] = row.split(",");
			if (fields == null || row.equals(""))
				break;
			Activity interaction = new Activity(fields[0], fields[1], fields[2], fields[3], fields[4]);
			contents.add(interaction);
		}
		return contents;
	}
	
	public LinkedList<RecordAc> StringToRecordAcs(String content) {
		String rows[] = content.split("\n");
		LinkedList<RecordAc> contents = new LinkedList<RecordAc>();
		for (String row : rows) {
			String fields[] = row.split(",");
			if (fields == null || row.equals(""))
				break;
			RecordAc rating = new RecordAc(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
			contents.add(rating);
		}
		return contents;
	}
	
	public LinkedList<RecordGy> StringToRecordGys(String content) {
		String rows[] = content.split("\n");
		LinkedList<RecordGy> contents = new LinkedList<RecordGy>();
		for (String row : rows) {
			String fields[] = row.split(",");
			if (fields == null || row.equals(""))
				break;
			RecordGy rating = new RecordGy(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]);
			contents.add(rating);
		}
		return contents;
	}
	
	public LinkedList<RecordSp> StringToRecordSps(String content) {
		String rows[] = content.split("\n");
		LinkedList<RecordSp> contents = new LinkedList<RecordSp>();
		for (String row : rows) {
			String fields[] = row.split(",");
			if (fields == null || row.equals(""))
				break;
			RecordSp rating = new RecordSp(fields[0], fields[1], fields[2], fields[3]);
			contents.add(rating);
		}
		return contents;
	}
	
	public LinkedList<RecordPo> StringToRecordPos(String content) {
		String rows[] = content.split("\n");
		LinkedList<RecordPo> contents = new LinkedList<RecordPo>();
		for (String row : rows) {
			String fields[] = row.split(",");
			if (fields == null || row.equals(""))
				break;
			RecordPo rating = new RecordPo(fields[0], fields[1], fields[2], fields[3], fields[4]);
			contents.add(rating);
		}
		return contents;
	}
	
	public int activity2action(String action){
		if(action.equals("WALKING"))
			return 0;
		if(action.equals("JOGGING"))
			return 1;
		if(action.contains("RUNNING"))
			return 2;
		if(action.contains("SWIMMING"))
			return 3;
		if(action.contains("BICYCLING"))
			return 4;
		if(action.contains("MOTOCYCLING"))
			return 5;
		if(action.contains("DRIVING"))
			return 6;
		if(action.contains("BOATING"))
			return 7;
		if(action.contains("FLYING"))
			return 8;
		if(action.contains("SITTING"))
			return 9;
		if(action.contains("SLEEPING"))
			return 10;
		if(action.contains("STANDING"))
			return 11;
		if(action.contains("LAYING"))
			return 12;
		if(action.contains("FALLING"))
			return 13;
		if(action.contains("DEING"))
			return 14;
		if(action.contains("METRO"))
			return 15;
		if(action.contains("BUS"))
			return 16;
		if(action.contains("TRAMWAY"))
			return 17;
		if(action.contains("SKYING"))
			return 18;
		if(action.contains("SURFING"))
			return 19;
		if(action.contains("LANDING"))
			return 20;
		if(action.contains("TAKING OFF"))
			return 21;
		if(action.contains("TRAIN"))
			return 22;
		if(action.contains("CRASHING"))
			return 23;
		if(action.contains("JUMPING"))
			return 24;
		if(action.contains("RAW"))
			return 25;
		return -1;
	}
	
	public int status2connectivity(String status){
		if(status.contains("REGISTERED"))
			return 0;
		if(status.equals("LOGIN"))
			return 1;
		if(status.equals("LOGOUT"))
			return 2;
		if(status.contains("LOST"))
			return 3;
		if(status.contains("RECOVER"))
			return 4;
		return -1;
	}
	
	public Activity getActivityfromId(String id){
		for(Activity activity: this.activities){
			if(activity.getId().equals(id)) return activity;
		}
		return null;
	}
	
	public Activity getActivityfromRecord(AbstractRecord record){
		for(Activity activity: this.activities){
			if(activity.getId().equals(record.getActivity())) return activity;
		}
		return null;
	}
	
	public Account getAccountfromActivity(Activity activity){
		for(Account account: this.accounts){
			if(account.getId().equals(activity.getAccount())) return account;
		}
		return null;
	}
	
	public Account getAccountfromId(String id){
		for(Account account: this.accounts){
			if(account.getId().equals(id)){
				return account;
			}
		}
		return null;
	}
	
	public Session getsessionfromAccount(String account){
		for(Session session: this.sessions){
			if(session.getAccount().equals(account)){
				return session;
			}
		}
		return null;
	}
	
	public Session getSessionfromToken(String token){
		for(Session session: this.sessions){
			if(session.getToken().equals(token)){
				return session;
			}
		}
		return null;
	}
	
	public LinkedList<Account> StringToAccounts(String content) {
		String rows[] = content.split("\n");
		LinkedList<Account> contents = new LinkedList<Account>();
		for (String row : rows) {
			String fields[] = row.split(",");
			if (fields == null || row.equals(""))
				break;
			Account account = new Account(fields[0], fields[1], fields[2], fields[3]);
			contents.add(account);
		}
		return contents;
	}

	
	public LinkedList<Index> StringToIndexes(String content) {
		String rows[] = content.split("\n");
		LinkedList<Index> contents = new LinkedList<Index>();
		for (String row : rows) {
			String fields[] = row.split(",");
			if (fields == null || row.equals(""))
				break;
			Index index = new Index(fields[0], fields[1], fields[2]);
			contents.add(index);
		}
		return contents;
	}
	
	public LinkedList<Session> StringToSessions(String content) {
		String rows[] = content.split("\n");
		LinkedList<Session> contents = new LinkedList<Session>();
		for (String row : rows) {
			String fields[] = row.split(",");
			if (fields == null || row.equals(""))
				break;
			Session session = new Session(fields[0], fields[1], fields[2], fields[3]);
			contents.add(session);
		}
		return contents;
	}
	
	public String generateCSV(String activity, String record, String component){
		String csvContent = "";
		if(record != null){
			if(component == null){// all components in the record in the activity: parameters
				
			}else{
				
			}
		}else{
			//Do not allow this. The record number is mandatory
		}
		return csvContent;
	}
	
	public String generateLocationKML(String activity, String record){
		String csvContent = "";
		if(record != null){
			
		}else{
			//Do not allow this. The record number is mandatory
		}
		return csvContent;
	}
	
	public void sendMail(String type, String from, String to, String subject, String content) {
		String host = "localhost";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties);
		try {
			MimeMessage message = new MimeMessage(session);
			if (type.equals("html"))
				message.setHeader("Content-Type", "text/html");
			message.setFrom(new InternetAddress(from));
			message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
			message.addRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(from));
			message.setSubject(subject);
			if (type.equals("html"))
				message.setContent(content, "text/html; charset=ISO-8859-1");
			else
				message.setText(content);
			Transport.send(message);
			LOGGER.pushDebugs("Message sent successfully....");
		} catch (MessagingException mex) {
			LOGGER.pushWarnings(mex,"Email sending failure. Message problem." + mex.getMessage());
		}
	}
	
	//I have to check the sanity of the request after decryption
	//The package encryption will be AES 512
	//The package maximum payload will be 512 bits
	
	// Used for request encryption
	public String aes256Encrypt(String plaintext, String encryptionKey){
		byte[] cipher;
		String encrypted = "";
		try {
			cipher = AES.encrypt(packager(plaintext), encryptionKey);
			encrypted = byteToHex(cipher);
		} catch (Exception e) {
			LOGGER.pushErrors(e, "Encryption failed.");
		}
		return encrypted;
	}
	
	public String aes256Decrypt(String encrypted, String encryptionKey){
	    String decrypted = "";
		try {
			decrypted = AES.decrypt(HexTobyte(encrypted), encryptionKey);
		} catch (Exception e) {
			LOGGER.pushErrors(e, "Decryption failed.");
		}
		return decrypted.split("~")[0];
	}
	
	Index getIndex(String table){
		Index index = null;
		for(Index idx: this.indexes){
			if(idx.getTable().equals(table)){
				index = idx;
			}
		}
		return index;
	}
	
	public String packager(String data){
		  int complete = data.length() % 16;
		  String result = data;
		  if(complete >= 1) result+= "~";
		  for(int i = 0; i<(15-complete);i++){
			result+="0";
		  }
		  System.out.println(result);
		  return result;
	  }
	
	public String renewToken(String email, Date stamp, String status) {
		String provided = new String(dateFormat.format(stamp));
		String token = "";
		if(status.equals(status2connectivity("RECOVER")))
		   token = recoverToken(email, provided);
		else
		   token = generateToken(email, provided);
		Account account = getAccountfromEmail(email);
		adapter.sync("session", "account", account.getId(), "token,stamp,status",token+","+ provided+ ","+ status);
		return token;
	}

	public com.tracker.backend.service.rest.entity.Account getAccountfromEmail(String email) {
		for(com.tracker.backend.service.rest.entity.Account account: accounts){
			if(account.getEmail().equals(email)) return account;
		}
		return null;
	}
	
	public Session account2Session(String account) {
		for (Session session : this.sessions) {
			if (session.getAccount().equals(account)) {
				return session;
			}
		}
		return null;
	}
	
	public String generateToken(String pseudo, String date) {
		String newToken = sha256(pseudo + date, "Tracker2014Session");
		return newToken;
	}

	public String recoverToken(String pseudo, String date) {
		String newToken = sha256(pseudo + date, "Tracker2014Recover");
		return newToken;
	}

	public boolean authentificate(String email, String password) {
		boolean isit = false;
		for (com.tracker.backend.service.rest.entity.Account acc : this.accounts) {
			if (acc.getEmail().equals(email) &&
				acc.getPassword().equals(sha256(password, "Tracker2014Password"))) {
				isit = true;
				break;
			}
		}

		return isit;
	}
	
	public String sha256(String clear, String sel) {
		String hash = clear;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			hash = byteToHex(md.digest((clear + sel).getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return hash;
	}

	public String byteToHex(byte[] bits) {
		if (bits == null) {
			return null;
		}
		StringBuffer hex = new StringBuffer(bits.length * 2); // encod(1_bit) =>
																// 2 digits
		for (int i = 0; i < bits.length; i++) {
			if (((int) bits[i] & 0xff) < 0x10) { // 0 < .. < 9
				hex.append("0");
			}
			hex.append(Integer.toString((int) bits[i] & 0xff, 16)); // [(bit+256)%256]^16
		}
		return hex.toString();
	}
	
	public byte[] HexTobyte (String s) {
        String s2;
        byte[] b = new byte[s.length() / 2];
        int i;
        for (i = 0; i < s.length() / 2; i++) {
            s2 = s.substring(i * 2, i * 2 + 2);
            b[i] = (byte)(Integer.parseInt(s2, 16) & 0xff);
        }
        return b;
    }
	
	public boolean isNear(String lat1, String lon1, String alt1, String lat2, String lon2, String alt2){
		double dist = distance(Double.parseDouble(lat1), Double.parseDouble(lon1), Double.parseDouble(lat2), Double.parseDouble(lon2), 'K');
		return dist <= 50;
	}
	
	private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
      dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
      if (unit == 'K') {
        dist = dist * 1.609344;
      } else if (unit == 'N') {
        dist = dist * 0.8684;
      }else if (unit == 'M') {
        dist = dist * 1609.344;
        }
      return (dist);
    }

    private double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }
}
