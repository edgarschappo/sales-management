package com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.view.OperationType;
import com.view.ResponseMessages;
import com.vo.VOAdjustments;
import com.vo.VOSales;
import com.vo.VOSalesResume;

/**
 * @author edgar.luis Singleton Pattern
 */
public class SalesController {

	private final static Logger logger = Logger.getLogger("SalesController");

	private static SalesController uniqueInstance = new SalesController();
	private static boolean ApplicationPaused = false;

	private static List<VOSales> sales = new ArrayList<>();
	private static HashMap<String, VOSalesResume> salesResume = new HashMap<String, VOSalesResume>();
	private static List<VOAdjustments> voAdjustments = new ArrayList<>();
	private static HashMap<String, Float> adjustmentsResume = new HashMap<String, Float>();
	private static int messageSize = 0;

	private SalesController() {

	}

	public static SalesController getInstance() {
		return uniqueInstance;
	}

	public boolean applicationIsPaused() {
		return ApplicationPaused;
	}

	public synchronized String processMessage(Integer message, String product, OperationType operation,
			Integer quantity, Float value) {

		boolean tenth = false;
		boolean fifetyth = false;

		try {

			messageSize++;
			tenth = ((messageSize % 10) == 0);
			fifetyth = ((messageSize % 50) == 0);

			if (message == null)
				return ResponseMessages.FIELD_NOT_FOUND.toString();

			switch (message) {
			case 1:
				if (product != null && value != null) {
					SalesController.processSaleMessage(product, 1, value);
				} else {
					return ResponseMessages.FIELD_NOT_FOUND.toString();
				}

				break;

			case 2:
				if (product != null && quantity != null && value != null) {
					SalesController.processSaleMessage(product, quantity, value);
				} else {
					return ResponseMessages.FIELD_NOT_FOUND.toString();
				}

				break;

			case 3:
				if (product != null && operation != null && value != null) {
					SalesController.processAdjustmentMessage(product, value, operation);
				} else {
					return ResponseMessages.FIELD_NOT_FOUND.toString();
				}

				break;

			default:
				return ResponseMessages.MESSAGE_TYPE_NOT_FOUND.toString();
			}

			/*
			 * After every 10th message received your application should log a
			 * report detailing the number of sales of each product and their
			 * total value.
			 */
			if (tenth)
				showSalesResume();

			/*
			 * After 50 messages your application should log that it is pausing,
			 * stop accepting new messages and log a report of the adjustments
			 * that have been made to each sale type while the application was
			 * running.
			 */
			if (fifetyth) {
				ApplicationPaused = true;

				showAdjustmentsResume();

				// used to test application paused scenario
				Thread.sleep(8000);

				ApplicationPaused = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessages.INTERNAL_ERROR.toString();
		}

		return ResponseMessages.MESSAGE_PROCESSED.toString();
	}

	// This method process messages type 1 and 2
	private static void processSaleMessage(String product, int quantity, float price) {

		try {

			float totalValue = price * quantity;

			// Store sale
			VOSales sale = new VOSales();
			sale.setProduct(product);
			sale.setQuantity(quantity);
			sale.setPrice(price);

			sales.add(sale);

			// Verify if key already exists
			VOSalesResume voResume = salesResume.get(product);

			if (voResume != null) {
				voResume.setNumberOfSales(voResume.getNumberOfSales() + 1);
				voResume.setTotalQuantity(voResume.getTotalQuantity() + quantity);
				voResume.setTotalValue(voResume.getTotalValue() + totalValue);
			} else {
				voResume = new VOSalesResume();

				voResume.setNumberOfSales(1);
				voResume.setTotalQuantity(quantity);
				voResume.setTotalValue(totalValue);

				// Initialize resume for product
				salesResume.put(product, voResume);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Message Type 3
	private static void processAdjustmentMessage(String product, float value, OperationType operation) {

		try {
			// Store Adjustments
			VOAdjustments adjustment = new VOAdjustments();
			adjustment.setProduct(product);
			adjustment.setValue(value);
			adjustment.setOperation(operation);

			voAdjustments.add(adjustment);

			Float adjustmentValue = adjustmentsResume.get(product);

			// Verify if key already exists
			if (adjustmentValue != null) {
				if (operation.equals(OperationType.ADD))
					adjustmentValue += value;
				if (operation.equals(OperationType.SUB))
					adjustmentValue += -value;

				adjustmentsResume.put(product, adjustmentValue);
			} else {
				// Initialize resume for product
				if (operation.equals(OperationType.ADD))
					adjustmentsResume.put(product, value);
				if (operation.equals(OperationType.SUB))
					adjustmentsResume.put(product, -value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * After every 10th message received your application should log a report
	 * detailing the number of sales of each product and their total value.
	 */
	private static void showSalesResume() {

		logger.info("Detailing Sales:");
		for (Map.Entry<String, VOSalesResume> r : salesResume.entrySet()) {

			// Get Adjustments Total
			Float valueAdjustments = adjustmentsResume.get(r.getKey());

			// List results
			VOSalesResume voResume = r.getValue();

			// Add adjustments
			float totalValue = voResume.getTotalValue();
			if (valueAdjustments != null)
				totalValue += valueAdjustments;

			logger.info("Product: " + r.getKey() + " Number of Sales: " + voResume.getNumberOfSales() + " Quantidade: "
					+ voResume.getTotalQuantity() + " Total Value: " + totalValue);
		}
	}

	/*
	 * After 50 messages your application should log that it is pausing, stop
	 * accepting new messages and log a report of the adjustments that have been
	 * made to each sale type while the application was running.
	 */
	private static void showAdjustmentsResume() {

		logger.info("Detailing Adjustments:");
		for (VOAdjustments vo : voAdjustments) {
			logger.info(
					"Product: " + vo.getProduct() + " Operation : " + vo.getOperation() + " Value : " + vo.getValue());
		}
	}

}
