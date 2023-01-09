package za.co.sunesh.springclean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.contentOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Controller.class,
		webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SpringCleanApplicationTests {

	@Autowired
	private Controller controller;
	private final PrintStream standardOut = System.out;
	private ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	@BeforeEach
	public void setUp() throws IOException {
		System.setOut(new PrintStream(outputStreamCaptor, true));
	}

	@Test
	public void testGcdSuccessResponses() {
		assertEquals(6, controller.gcd(new int[]{-54,24}));
		assertEquals(6, controller.gcd(new int[]{54,-24}));
		assertEquals(6, controller.gcd(new int[]{-54,-24}));
		assertEquals(6, controller.gcd(new int[]{54,24}));
		assertEquals(5, controller.gcd(new int[]{30,15,10}));
		assertEquals(20, controller.gcd(new int[]{120,80,60,40}));
		assertEquals(20, controller.gcd(new int[]{120,80,-60,40}));
	}

	@Test
	public void testGcdRuntimeException_whenOneValueIsProvided() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			controller.gcd(new int[]{10});
		});

		assertEquals("Enter 2 or more non-zero integer numbers",exception.getMessage());
	}

	@Test
	public void testGcdRuntimeException_whenZeroIsProvided() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			controller.gcd(new int[]{11, 0});
		});

		assertEquals("Values must be non-zero and between -2147483648 and 2147483647",exception.getMessage());
	}

	@Test
	public void testPrintAllAddressesFromJsonFile() {
		controller.printAllAddressesFromJsonFile();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("printAllAddressesFromJsonFileOutput.txt").getFile());
		assertEquals(contentOf(file), outputStreamCaptor.toString().trim());
	}

	@Test
	public void testPrintAddressOfTypePhysicalAddress() {
		String expectedResponse = "Physical Address - Address 1 Line 2 - City 1 - Eastern Cape - 1234 - South Africa";
		controller.printAddressOfType("Physical Address");
		assertEquals(expectedResponse, outputStreamCaptor.toString().trim());
	}

	@Test
	public void testPrintAddressOfTypeBusinessAddress() {
		String expectedResponse = "Business Address - Address 3  - City 3 -  - 3456 - South Africa";
		controller.printAddressOfType("Business Address");
		assertEquals(expectedResponse, outputStreamCaptor.toString().trim());
	}

	@Test
	public void testValidateJsonFileAddresses() {
		controller.validateJsonFileAddresses();
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("validateAddressesOutput.txt").getFile());
		assertEquals(contentOf(file), outputStreamCaptor.toString().trim());
	}

	//TODO: Add more test cases
}
