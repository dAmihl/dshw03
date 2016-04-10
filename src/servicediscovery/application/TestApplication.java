package servicediscovery.application;

import servicediscovery.utility.client.ServiceLocator;
import servicediscovery.utility.server.ServiceAnnouncer;

/**
 * Application which can be run as client or as server to demonstrate our location service.
 * 
 * @author dAmihl, Martin
 *
 */

public class TestApplication {
	
	// port to test with
	private static int TEST_PORT = 1337;
	
	/**
	 * Main method.
	 * @param args
	 */
	 public static void main(String[] args) {		
		if (args.length < 1){ // no arguments given, run as client per default
			System.out.println("No Arguments found. Running as client.");
			runAsClient();
		}else{
			String arg = args[0].toLowerCase();
			
			if (arg.equals("server")){ // run as server if "server" argument is given
				System.out.println("Running as server.");
				runAsServer();
			}else{
				System.out.println("Running as client.");
				runAsClient(); // run as client else
			}
		}
		
	}
	 
	 /**
	  * Let application run as server.
	  */
	 private static void runAsServer(){
		 ServiceAnnouncer announcer = new ServiceAnnouncer(TEST_PORT);
		 announcer.start();
	 }
	 
	 /**
	  * Let application run as client.
	  */
	 private static void runAsClient(){
		 ServiceLocator locator = new ServiceLocator(TEST_PORT);
		 locator.startDiscovery();
	 }
}
