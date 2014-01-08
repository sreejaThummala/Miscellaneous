import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sreejat
 * Minimal Crawler
 * Date : 09/16/2013
 *
 */


public class Crawler {
  
	public static final String DISALLOW = "Disallow:";
	private static Logger LOGGER = Logger.getLogger("Crawler");
	private static final String HTTP = "http://";
	private static final String ROBOT = "/robots.txt";

	/**
	 * @param none
	 * handles all the processes related to crawling
	 */
	public void getCrawlingResults(){
		Handler consoleHandler = new ConsoleHandler();
		LOGGER.addHandler(consoleHandler);
		
		URL url = getSeed();
		int max = getLimit();
		
		crawl(url,max);
	}

	/**
	 * @return search limit of the crawler
	 * 
	 */
	private int getLimit() {
		Input input = new Input();
		int max= input.getLimit();
		
		return max;
	}

	/**
	 * @param none
	 * @return seed url if valid otherwise nullS
	 * @exception MalformedURLException
	 */
	private URL getSeed(){
		Input input = new Input();
		String seed= input.getSeed();
		
		URL url;
		try { 
			url = new URL(seed);

		} catch (MalformedURLException e) {
			url = null;
		}
		return url;
	}

	/**
	 * @param seed : url for the crawler
	 * @param max : search limit 
	 */
	private void crawl(URL seed,int max){

		setHTTPProxy();

		Queue<URL> foundURLList = new LinkedList<URL>();
		foundURLList.add(seed);

		for (int i = 0; i < max; i++) {

			URL url = foundURLList.poll();
			LOGGER.info("Searching " + url.toString());

			if (isRobot(url)) {  	
				String page = getpage(url);

				LOGGER.info("Page: \n"+page);

				if (page.length() != 0){
					foundURLList.addAll(getURLS(url,processpage(page)));
					removeDuplicates(foundURLList);
					filterNonHtmlPages(foundURLList);
					}
				if (foundURLList.isEmpty()) break;
				
			}
		}

		LOGGER.info("Search complete.");

	}

	/**
	 * @param foundURLList
	 * only html or html pages are taken
	 */
	private void filterNonHtmlPages(Queue<URL> foundURLList) {
		Iterator<URL> iter = foundURLList.iterator();
		
		while(iter.hasNext()){
			URL url = iter.next();
			String filename =  url.getFile();
			int iSuffix = filename.lastIndexOf("htm");						
			
			if (!(iSuffix == filename.length() - 3) ||
					(iSuffix == filename.length() - 4)) {
				iter.remove();	
				System.out.println("Found new URL - not a htm or html page " + url.toString());
			} else {
				System.out.println("Found new URL - htm or html page " + url.toString());
			}
		}		
	}

	/**
	 * @param foundURLList
	 * Removes duplicates in the URL so the crawlers doesnt crawl same page
	 */
	private void removeDuplicates(Queue<URL> foundURLList) {
		Iterator<URL> iter = foundURLList.iterator();

		Set<URL> tempSet = new HashSet<URL>();
	        while (iter.hasNext())
	        {

	        URL url = iter.next();
	                      if(tempSet.contains(url)){
	                          iter.remove();
	                      }else{
	                            tempSet.add(url);
	                      }
	        }
	}

	/**
	 * Set HTTP proxy properties
	 */
	private void setHTTPProxy() {
		Properties props= new Properties(System.getProperties());

			props.put("http.proxySet", "true");
			props.put("http.proxyHost", "webcache-cup");
			props.put("http.proxyPort", "8080");
			props.put("http.nonProxyHosts", "localhost|127.0.0.1");
			
			Input input = new Input();
	    	String user = input.getUserName();
	    	String password = input.getPassword();
	    	if(!user.isEmpty()){
	    		LOGGER.info("Authentication to be done"+"user:"+user+"password:"+password);
	    		Authenticator.setDefault(new ProxyAuthenticator(user,password));
	    	}

			Properties newprops = new Properties(props);
			System.setProperties(newprops);		
		}

		

		/**
		 * @param url
		 * @return whether the given url is robot allowing or not
		 */
		private boolean isRobot(URL url) {

			String host = url.getHost();

			// form URL of the robots.txt file
			String strRobot = new StringBuffer(HTTP).append(host).append(ROBOT).toString();
			URL urlRobot;

			try { 
				urlRobot = new URL(strRobot);
			} catch (MalformedURLException e) {
				return false;
			}

			LOGGER.info("Checking robot protocol " + urlRobot.toString());
			LOGGER.info("Will take time");

			BufferedReader br;
			StringBuffer robotText = new StringBuffer();
			String inputLine = null;
			try {
				br = new BufferedReader(new InputStreamReader(urlRobot.openStream()));

				while((inputLine = br.readLine())!=null){
					robotText.append(inputLine);
				}

				br.close();
			}catch(IOException e){
				LOGGER.info("No robot file");
				return false;

			}
			
			String roboURL = url.getFile();
			int index = 0;
			while ((index = robotText.indexOf(DISALLOW, index)) != -1) {
				index += DISALLOW.length();
				String strPath = robotText.substring(index);
				StringTokenizer st = new StringTokenizer(strPath);
				if (!st.hasMoreTokens())
					break;
				String strBadPath = st.nextToken();
				// if the URL starts with a disallowed path, it is not safe
				if (roboURL.indexOf(strBadPath) == 0)
					return false;
			}
			
			return true;
		}

		

		/**
		 * @param oldURL
		 * @param relativeURLStrings
		 * @return queue of absolute urls
		 * Form absolute URL and checks the validity of the urls
		 */
		public Queue<URL> getURLS(URL oldURL, Queue<String> relativeURLStrings){ 
			URL url = null;
			Queue<URL> newURLList = new LinkedList<URL>();
		
			for(String relativeURL:relativeURLStrings){
				try {
						url = new URL(oldURL,relativeURL);
					}catch (MalformedURLException e) { 
						LOGGER.info("invalidate URL detected");
					}
					if(url!=null){
						newURLList.add(url);
						}
					}
			return newURLList;
		}

		/**
		 * @param url
		 * @return entire page in the form of text
		 * Downloads contents of URL
		 */
		public String getpage(URL url)
		{ 
			StringBuffer content = new StringBuffer();
			try { 
			// try opening the URL
			URLConnection urlConnection = url.openConnection();
			LOGGER.info("Downloading " + url.toString());

			urlConnection.setAllowUserInteraction(false);

			BufferedReader br;
			
			String inputLine = null;
			try {
				br = new BufferedReader(new InputStreamReader(url.openStream()));

				while((inputLine = br.readLine())!=null){
					content.append(inputLine);
				}

				br.close();
				}catch(IOException e){
				LOGGER.info("Error : cannot open URL");
						}
				}catch (IOException e) {		
			LOGGER.info("ERROR: couldn't open URL");
				} 
			return content.toString();
			}

		
		/**
		 * @param page
		 * @return links in page in the form of string
		 */
		public Queue<String> processpage(String page)	
		{
			Pattern p = Pattern.compile("href=\"(.*?)\"");
			Matcher m = p.matcher(page);
			Queue<String> urlList = new LinkedList<String>();

			while (m.find()) {
				urlList.add( m.group(1)); // this variable should contain the link URL
			}
			return urlList;
		}
		
	}
