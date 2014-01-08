import java.net.MalformedURLException;
import java.net.URL;


public class Input {
	
 private static String seed = "http://en.wikipedia.org/wiki/K-means_clustering";
 private static int limit=20;
 private static String userName;
 private static String password;
 
 public String getSeed(){
	 return seed;
 }
 
 public void setSeed(String seed){
	 try {
		URL url = new URL(seed);
	} catch (MalformedURLException e) {
		return;
	}
	 this.seed= seed;
 }
 
 public int getLimit(){
	 return limit;
 }
 
 public void setLimit(String limit){
	 int max =0;
	 try{
	max = Integer.parseInt(limit);}
	 catch(NumberFormatException e){
		 return;
	 }
	 this.limit = max;
 }
 
 public String getUserName(){
	 return userName;
 }
 
 public void setUserName(String userName){
	 this.userName = userName;
 }
 
 public String getPassword(){
	 return password;
 }
 
 public void setPassword(String password){
	 this.password = password;
 }
 
}
