import java.util.Scanner;


public class Main {

	public static void main(String[] args){
		  Input input = new Input();
	      Scanner scanner = new Scanner(System.in);
	      scanner.useDelimiter("\\n");
	      
	      System.out.println("Give a seed to crawl, to use default press enter : ");
	      String seed = scanner.nextLine();
	      input.setSeed(seed);
	      
	      System.out.println("Give a limit to number of pages to be searched , to use default of 20 press enter:");
	      String limit= scanner.nextLine();
	      input.setLimit(limit);
	      
	      System.out.println("Give userName, to continue without authorization press enter (Caution might cause 407 or 401 error, in case run program again):");
	      String userName = scanner.next();
	      input.setUserName(userName);
	      
	      System.out.println("Give password:");
	      String password = scanner.next();
	      input.setPassword(password);
	      
	      scanner.close();
	      
	      Crawler crawler = new Crawler();
	      crawler.getCrawlingResults();
	      	      
	}
	}
