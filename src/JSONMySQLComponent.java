import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMySQLComponent {
	
	static String myDriver = "com.mysql.cj.jdbc.Driver";
    static String myUrl = "jdbc:mysql://localhost:3306/mysqlapp";

private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
}

 public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
}

public static void main(String[] args)
{	
	System.out.println("Please Enter The Zipcode");
	Scanner sc = new Scanner(System.in);
	String zipcode  = sc.next();

	 JSONObject contractObject = new JSONObject();
	try {
		contractObject = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?zip="+zipcode+",us&appid=4c0d9402daa2da0af1af90a091e2d35a");
		JSONObject mainTag = (JSONObject) contractObject.getJSONObject("main");
		Double temparature_k = (Double) mainTag.get("temp");
		long temparature_f = (long)((temparature_k-273.15)*1.8+32);
		String cityName = (String) contractObject.get("name");
		String temparature = Long.toString(temparature_f)+"F";
		insert_into_db(cityName, zipcode,temparature);
	} catch (IOException | JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}   
}

public static void insert_into_db(String city, String zipcode, String temparature)
{
	Properties properties = new Properties();
	properties.setProperty("user", "root");
	properties.setProperty("password", "mysql@221B");
	properties.setProperty("useSSL", "false");
	properties.setProperty("autoReconnect", "true");
    try {
		Class.forName(myDriver);
		Connection conn = DriverManager.getConnection(myUrl, properties);
		 String query = " insert into weather (city, zipcode, temparature)"
			        + " values (?, ?, ?)";
		 
		   PreparedStatement preparedStmt = conn.prepareStatement(query);
		      preparedStmt.setString(1, city);
		      preparedStmt.setString(2, zipcode);
		      preparedStmt.setString(3, temparature);
		      
		      preparedStmt.execute();			      
		  
		      conn.close();
		      
		      System.out.println("Success: Data Insertion Was Sucessful");
		      
	} catch (Exception e) {
		System.out.println("Error: Data Insertion Was Failed");
		e.printStackTrace();	
	}	
}

}
