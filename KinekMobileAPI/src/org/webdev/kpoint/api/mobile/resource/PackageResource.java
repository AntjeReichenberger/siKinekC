package org.webdev.kpoint.api.mobile.resource;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.domain.PackagesDomain;
import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.api.mobile.util.WSLogger;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.api.mapper.request.Package;
import org.webdev.kpoint.bl.api.mapper.request.PickupPackage;
import org.webdev.kpoint.bl.api.mapper.request.ReceivePackage;

import com.google.gson.Gson;

/**
 * This class provides access to all Package related operations and
 * functionality. It follows the REST standards by using the proper HTTP Method
 * based on the operation that is invoked. For example, GET (Fetch), POST
 * (Create), PUT (Update), and DELETE (Remove).
 * 
 * All user related API operations fall under the <code>/users</code> context.
 */
@Path("/packages")
public class PackageResource extends BaseResource {

	WSLogger logger = new WSLogger(PackageResource.class);
	@Context HttpServletRequest request;
	
	/**
	 * Updates or adds new information to an existing Package. The only data
	 * that is currently supported is updating a package status to be
	 * "picked up".
	 * 
	 * @param packageData
	 *            JSON describing the user to be created
	 * @return A Response object containing a JSON representation of the new
	 *         user.
	 */
	@POST
	@Path("{packageId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("packageId") int packageId,
			String packageData) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("PackageID", String.valueOf(packageId));
		logData.put("PackageData", packageData);
		logger.debug("Request to update Package", logData);

		String json = "";
		try {
			Package pkg = gson.fromJson(packageData, Package.class);
			pkg.setPackageId(packageId);
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			Pickup pickup = PackagesDomain.updatePackage(pkg, authenticatedUser);
			logger.info("Package successfully updated.", logData);
			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.Pickup(pickup));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}
	
	/**
	 * Accepts delivery of a Package. This method accepts valid JSON which describes the packages being received,
	 * where the package is recieved, and which users is it recieved for.
	 * 
	 * @param trackingData
	 *            JSON describing the package receipt record to be created
	 * @return A Response object containing a JSON representation of the new receipt data.
	 * 
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response receive(String packageReceiptData) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("PackageReceiptData", packageReceiptData);
		logger.debug("Request to create Package Receipt record", logData);

		String json = "";
		try {
			Gson gson = new Gson();
			ReceivePackage receipt = gson.fromJson(packageReceiptData, ReceivePackage.class);
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			String transactionId = PackagesDomain.createPackageReceipt(receipt, authenticatedUser);
			
			logData.put("TransactionId", transactionId);
			logger.info("Package Receipt record successfully created.", logData);
			json = gson.toJson(transactionId);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}
	
	/**
	 * Handled a package pickup. This method accepts valid JSON which describes the packages being picked-up and the user
	 * who is picking them up.
	 * 
	 * @param pickupData  JSON describing the package pickup record to be created
	 * @return A Response object containing a JSON representation of the new data.
	 * 
	 */
	@PUT
	@Path("/pickup")
	@Produces(MediaType.APPLICATION_JSON)
	public Response pickup(String pickupData) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("PickupData", pickupData);
		logger.debug("Request to create Package Pickup record", logData);

		String json = "";
		try {
			Gson gson = new Gson();
			PickupPackage pickup = gson.fromJson(pickupData, PickupPackage.class);
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			String transactionId = PackagesDomain.createPackagePickup(pickup, authenticatedUser);
			
			logData.put("TransactionId", transactionId);
			logger.info("Package Pickup record successfully created.", logData);
			json = gson.toJson(transactionId);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}
	
	/**
	 * Retrieves the specified package receipt and verifies that the provided user is a recipient of the receipt.
	 * 
	 * @param userId  The userid of the package recipient
	 * @param receiptId The id of the package receipt
	 * @return A Response object containing a JSON representation of the receipt
	 * 
	 */
	@GET
	@Path("/receipt/{receiptId}/userId/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPackageReceipt(@PathParam("userId") int userId,
									@PathParam("receiptId") int receiptId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userId", String.valueOf(userId));
		logData.put("receiptId", String.valueOf(receiptId));
		logger.debug("Request to retrieve a package receipt", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			PackageReceipt packageReceipt = PackagesDomain.getPackageReceipt(userId, receiptId, authenticatedUser);
			logger.info("Package receipt successfully retrieved.", logData);

			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.PackageReceipt(packageReceipt));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}

}
