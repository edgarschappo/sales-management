package test.java;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.controller.SalesController;
import com.view.OperationType;
import com.view.ResponseMessages;

/**
 * @author edgar.luis
 *
 */
public class TestCase {

	private String BASE_URL = "http://localhost:8080/sales-management/salesService/sendMessage";

	private String MESSAGE_TYPE_1 = "{\"message\":\"1\", \"product\":\"apple\", \"quantity\":\"2\", \"value\":\"10\"}";
	private String MESSAGE_TYPE_2 = "{\"message\":\"2\", \"product\":\"apple\", \"value\":\"10\"}";
	private String MESSAGE_TYPE_3_ADD = "{\"message\":\"3\", \"product\":\"apple\", \"operation\":\"ADD\", \"value\":\"1\"}";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/*
	 * Tests over Controller
	 */
	@Test
	public void testSalesController() {
		
		//Test mandatory field not found
		assertEquals(ResponseMessages.FIELD_NOT_FOUND.toString(),
				SalesController.getInstance().processMessage(null, null, null, null, null));
		
		//Test message type not found
		assertEquals(ResponseMessages.MESSAGE_TYPE_NOT_FOUND.toString(),
				SalesController.getInstance().processMessage(4, "apple", null, 1, 10f));
		
		//Test message type not found
		assertEquals(ResponseMessages.MESSAGE_TYPE_NOT_FOUND.toString(),
				SalesController.getInstance().processMessage(4, "apple", null, 1, 10f));
		
		//Test message type 1 processed
		assertEquals(ResponseMessages.MESSAGE_PROCESSED.toString(),
				SalesController.getInstance().processMessage(1, "apple", null, null, 10f));
		
		//Test message type 2 processed
		assertEquals(ResponseMessages.MESSAGE_PROCESSED.toString(),
				SalesController.getInstance().processMessage(2, "apple", null, 2, 10f));
		
		//Test message type 3 processed
				assertEquals(ResponseMessages.MESSAGE_PROCESSED.toString(),
						SalesController.getInstance().processMessage(3, "apple", OperationType.SUB, null, 10f));
	}
	
	/*
	 * Tests over Rest API
	 */
	@Test
	public void testSalesService() {

		Boolean EXPECTED = true;
		Boolean connectionResult = false;

		try {
			// Test Message Type 1
			requestWithBody(200, MESSAGE_TYPE_1, "POST");
			
			// Test Message Type 2
			requestWithBody(200, MESSAGE_TYPE_2, "POST");
			
			// Test Message Type 3
			requestWithBody(200, MESSAGE_TYPE_3_ADD, "POST");

			// assertEquals(ResponseMessages.MESSAGE_PROCESSED.toString(), str);
			connectionResult = true;

		} catch (IOException e) {
			connectionResult = false;
		}

		// Test server unavailable
		assertEquals(EXPECTED, connectionResult);
	}

	/**
	 * Method used as helper to send request with body
	 * @param code
	 *            the expected code of the request (0 to ignore assertion)
	 * @param content
	 *            the body content of the request
	 * @param method
	 *            the method used for the request
	 * @return the server string response
	 * @throws IOException
	 */
	private String requestWithBody(int code, String content, String method) throws IOException {

		BufferedReader br;

		URL url = new URL(BASE_URL);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setDoOutput(true);

		String str = content;

		OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream());
		outWriter.write(str);
		outWriter.flush();
		outWriter.close();

		conn.getResponseMessage();

		int responseCode = conn.getResponseCode();

		if (responseCode >= 200 && responseCode <= 399)
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		else
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

		String s = br.readLine();

		if (code != 0)
			assertEquals(code, responseCode);

		return s;
	}

}
