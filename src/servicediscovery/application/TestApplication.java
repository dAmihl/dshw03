package servicediscovery.application;

import servicediscovery.utility.client.DiscoveryNotify;
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
	
	private static ServiceLocator clientServiceLocator;
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
		 System.out.println("Started service announcer.. waiting for requests.");
		 
	 }
	 
	 /**
	  * Let application run as client.
	  */
	 private static void runAsClient(){
		 clientServiceLocator = new ServiceLocator(TEST_PORT);
		 System.out.println("Starting blocking discovery now..");
		 clientServiceLocator.startBlockingDiscovery();
		 System.out.println("Discovery blocked. Its done now since this is printed.");
		
		clientServiceLocator.startDiscoveryWithNotify(new DiscoveryNotify() {
			
			@Override
			public void onDiscoveryFinished() {
				System.out.println("NOTIFY: Discovery is finished since this notifier is called.");				
			}
		});
		System.out.println("Notify discovery started, onFinished may be called after that.");
		
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

	
}
