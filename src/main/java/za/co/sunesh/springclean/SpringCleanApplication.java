package za.co.sunesh.springclean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCleanApplication implements ApplicationRunner {

	@Autowired
	Controller controller;

	public static void main(String[] args) { SpringApplication.run(SpringCleanApplication.class, args); }

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println(controller.highestCommonFactor(new int[]{-54,24}));
		controller.printAllAddressesFromJsonFile();
		controller.printAddressOfType("Business Address");
		controller.validateJsonFileAddresses();
	}
}
