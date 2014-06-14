package org.webdev.kpoint.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.CouponDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.OrganizationDao;
import org.webdev.kpoint.bl.persistence.RegionDao;
import org.webdev.kpoint.bl.pojo.Coupon;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.MessageTrigger;
import org.webdev.kpoint.bl.pojo.Organization;
import org.webdev.kpoint.bl.pojo.Region;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/coupons.action")
public class ManageCouponsActionBean extends SecureActionBean{

	private static final KinekLogger logger = new KinekLogger(ManageCouponsActionBean.class);
	public static enum Action { View, Edit, Create };
	public static enum CouponType { Generic, KP };
		
	private Action action;		
	private CouponType couponType;
	
	private int couponId;

	@Validate(required=true,on={"createCoupon"})
	private boolean alwaysShowCoupon;
	
	@Validate(required=true, on={"createCoupon"})
	private String endDate;
	
	@Validate(required=true, on={"createCoupon"})
	private String startDate;
	
	@Validate(required=true, on={"createCoupon"})
	private String expiryDate;
	
	@Validate(required=true, on={"createCoupon"})
	private FileBean couponImage;
	
	@Validate(required=true,on={"createCoupon"})
	private String description;	
	
	@Validate(required=true,on={"createCoupon"})
	private String title;
	
	private String targetEmailDelivery;	
	private String targetEmailDeliveryReminder;	
	private String targetEmailWelcome;
	private String targetEmailRegistrationReminder;
	
	private List<Region> regions;
	private List<Organization> organizations;
	private List<KinekPoint> kinekPoints;
	
	private int regionId;
	private int organizationId;
	private int kinekPointId;
	
	private Calendar startCalendar;
	private Calendar endCalendar;
	private Calendar expiryCalendar;
	
	private long imgSize = Long.parseLong(ApplicationProperty.getInstance().getProperty("coupon.img.size")); 
	private int imgWidth = Integer.parseInt(ApplicationProperty.getInstance().getProperty("coupon.img.width"));
	private int imgHeight = Integer.parseInt(ApplicationProperty.getInstance().getProperty("coupon.img.height"));
	
	@DefaultHandler
	public Resolution view(){
		return UrlManager.getCoupon();	
	}
	
	@ValidationMethod(on={"createCoupon"})
	public void validateDateFields(ValidationErrors errors){				
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date expiry = null;
		Date start = null;
		Date end = null;
		boolean isStartDateValidFormat = true;
		boolean isEndDateValidFormat = true;
		boolean isExpiryDateValidFormat = true;
		
		try
		{
			if (expiryDate != null && !expiryDate.isEmpty()) {
				expiry = df.parse(expiryDate);
			}	
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("expiryDate", expiryDate);
            logger.error(new ApplicationException("Error occurred processing the start date", e), logValues);
            
            String field = "expiryDate";
			ValidationError error = new SimpleError("The Expiry Date value must entered in a valid Date format");
			errors.add(field, error);
			isExpiryDateValidFormat = false;
		}
		
		try
		{
			if (startDate != null && !startDate.isEmpty()) {
				start = df.parse(startDate);
			}	
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("startDate", startDate);
            logger.error(new ApplicationException("Error occurred processing the start date", e), logValues);
            
            String field = "startDate";
			ValidationError error = new SimpleError("The Start Date value must entered in a valid Date format");
			errors.add(field, error);
			isStartDateValidFormat = false;
		}
		
		try
		{
			if(endDate != null && !endDate.isEmpty()){
				end = df.parse(endDate);
			}
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("endDate", endDate);
            logger.error(new ApplicationException("Error occurred processing the start date", e), logValues);
            
            String field = "endDate";
			ValidationError error = new SimpleError("The End Date value must entered in a valid Date format");
			errors.add(field, error);
			
			isEndDateValidFormat = false;
		}		
		
		if(isExpiryDateValidFormat && isStartDateValidFormat && isEndDateValidFormat){
			expiryCalendar = Calendar.getInstance();
			expiryCalendar.setTime(expiry);
			
			startCalendar = Calendar.getInstance();
			startCalendar.setTime(start);
			
			endCalendar = Calendar.getInstance();
			endCalendar.setTime(end);
			
			Calendar yesterday = Calendar.getInstance();
			yesterday.add(Calendar.DATE, -1);
			
			//Start Date is before current System Date or Start Date is after the End Date  
			if(!startCalendar.after(yesterday) || startCalendar.after(endCalendar)){
				errors.add("startDate", new SimpleError("Invalid Start Date was entered"));
			}
									
			//Expiry Date is before current System Date or Expiry Date is before/equal the End Date
			if(expiryCalendar.before(Calendar.getInstance()) || expiryCalendar.equals(endCalendar) || expiryCalendar.before(endCalendar)){			
				errors.add("expiryDate", new SimpleError("Invalid Expiry Date was entered."));
			}
		}
		
		if((targetEmailDelivery == null || targetEmailDelivery.isEmpty()) &&
				(targetEmailDeliveryReminder == null || targetEmailDeliveryReminder.isEmpty()) && 	
					(targetEmailWelcome == null || targetEmailDeliveryReminder.isEmpty())
					&& (targetEmailRegistrationReminder == null || targetEmailRegistrationReminder.isEmpty())){
			 errors.add("targetEmailDelivery", new SimpleError("At least one target email must be selected."));
		}

	}
	
	@ValidationMethod(on={"createCoupon"})
	public void validateDescription(ValidationErrors errors){
		if(description.equals("")){
			ValidationError error = new SimpleError("Description is a required field.");
			errors.add("description", error);
		}
	}
	@ValidationMethod(on={"createCoupon"})
	public void validateImage(ValidationErrors errors){
		if(couponImage != null){
			if(couponImage.getSize() > imgSize){
				ValidationError error = new SimpleError("Coupon image too large. Size must be " + imgSize/1024 + "kb or smaller.");
				errors.add("couponImage", error);
			}
			if(!(couponImage.getContentType().equals("image/gif") || couponImage.getContentType().equals("image/png") || couponImage.getContentType().equals("image/x-png") 
					|| couponImage.getContentType().equals("image/jpeg") || couponImage.getContentType().equals("image/pjpeg"))){
				ValidationError error = new SimpleError("Invalid file format for coupon. Accepted formats: png, jpeg and gif");
				errors.add("couponImage", error);
				return;
			}
			try {
				InputStream in = couponImage.getInputStream();
				BufferedImage image = ImageIO.read(in);
				if(image.getWidth() != imgWidth || image.getHeight() != imgHeight){
					ValidationError error = new SimpleError("Invalid file dimensions for coupon. Size must be " + imgWidth + "px by " + imgHeight + "px");
					errors.add("couponImage", error);
				}
			}
			catch (Exception e) {
				Hashtable<String,String> logProps = new Hashtable<String,String>();
				logProps.put("file name", couponImage.getFileName());
				logger.error(new ApplicationException("An exception happended during uploading the image file for coupon", e), logProps);
			}
		}
	}
		
	public Resolution createCoupon() throws Exception {
		
		CouponDao couponDao = new CouponDao();
		Coupon coupon = new Coupon();
		
		coupon.setTitle(title);
		coupon.setDescription(description);
		coupon.setExpiryDate(expiryCalendar);
		coupon.setDistributionStartDate(startCalendar);
		coupon.setDistributionEndDate(endCalendar);
		coupon.setAlwaysShowCoupon(alwaysShowCoupon);
		
		String img_path = ApplicationProperty.getInstance().getProperty("coupon.img.upload.path"); 
		String img_url = ApplicationProperty.getInstance().getProperty("coupon.img.upload.url"); 
		
		String newFileName = couponImage.getFileName().replace(".", Calendar.getInstance().getTimeInMillis() + ".");

		File file = new File(img_path + newFileName);
		try {
			couponImage.save(file);
			coupon.setImageUrl(img_url + newFileName);
		} catch (IOException e) {
			try {
				couponImage.delete();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		if(couponType == CouponType.KP){
			
			//only selected region will added to coupon table
			if(regionId != 0){
				Region region = new Region();
				region.setRegionId(regionId);
				coupon.setRegion(region);
			}
			
			//only selected organization will added to coupon table
			if(organizationId != 0){
				Organization org = new Organization();
				org.setOrganizationId(organizationId);
				coupon.setOrganization(org);
			}
			
			//only selected kinekpoint will added to coupon table
			if(kinekPointId != 0){
				KinekPoint kp = new KinekPoint();
				kp.setDepotId(kinekPointId);		
				coupon.setKinekPoint(kp);
			}
			
			//check if multiple coupons contain same kp id and "Always Show Coupon" field is true then there is conflicts
			if(couponDao.isAlwaysShowCouponConflictsByKP(kinekPointId)){
				return UrlManager.getAlwaysShowCouponConflict();
			}
			
		}
				
		int deliveryMsgTriggerId;
		int deliveryReminderMsgTriggerId; 
		int welcomeMsgTriggerId; 
		int registrationReminderMsgTriggerId;
		
		List<MessageTrigger> messageTriggers = new ArrayList<MessageTrigger>();
		
		if(targetEmailDelivery != null && !targetEmailDelivery.equals("")){
			deliveryMsgTriggerId = ExternalSettingsManager.getMessageTrigger_acceptdelivery();
			MessageTrigger msgTrg = new MessageTrigger();
			msgTrg.setId(deliveryMsgTriggerId);
			messageTriggers.add(msgTrg);
		}
		if(targetEmailDeliveryReminder != null && !targetEmailDeliveryReminder.equals("")){
			deliveryReminderMsgTriggerId = ExternalSettingsManager.getMessageTrigger_pickupreminder();
			MessageTrigger msgTrg = new MessageTrigger();
			msgTrg.setId(deliveryReminderMsgTriggerId);
			messageTriggers.add(msgTrg);
		}
		if(targetEmailWelcome != null && !targetEmailWelcome.equals("")){
			welcomeMsgTriggerId = ExternalSettingsManager.getMessageTrigger_signup();
			MessageTrigger msgTrg = new MessageTrigger();
			msgTrg.setId(welcomeMsgTriggerId);
			messageTriggers.add(msgTrg);
		}
		if(targetEmailRegistrationReminder != null && !targetEmailRegistrationReminder.equals("")){
			registrationReminderMsgTriggerId = ExternalSettingsManager.getMessageTrigger_registrationReminderEmailNoDefaultKP();
			MessageTrigger msgTrg = new MessageTrigger();
			msgTrg.setId(registrationReminderMsgTriggerId);
			messageTriggers.add(msgTrg);

			registrationReminderMsgTriggerId = ExternalSettingsManager.getMessageTrigger_registrationReminderEmailNoProfile();
			msgTrg = new MessageTrigger();
			msgTrg.setId(registrationReminderMsgTriggerId);
			messageTriggers.add(msgTrg);
		}
		
		Set<MessageTrigger> msgTrgs = new HashSet<MessageTrigger>(messageTriggers);
		coupon.setMessageTriggers(msgTrgs);
		
		
		couponId = couponDao.create(coupon);
		
		if(couponId>0){
			setSuccessMessage(MessageManager.getCouponCreateSuccess());
		}
		
		RedirectResolution resolution = new RedirectResolution(ManageCouponsActionBean.class);
		resolution.addParameter("couponType", couponType);
		return resolution;
	}
	
	//setter and getter
	
	public int getCouponId(){
		return couponId;
	}

	public void setCouponId(int couponId){
		this.couponId = couponId;
	}	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = formatDescriptionContent(description);
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public boolean getAlwaysShowCoupon() {
		return alwaysShowCoupon;
	}

	public void setAlwaysShowCoupon(boolean alwaysShowCoupon) {
		this.alwaysShowCoupon = alwaysShowCoupon;
	}

	public List<Region> getRegions() throws Exception {
		RegionDao regionDao = new RegionDao();
		regions = regionDao.fetch();
		
		return regions;
	}
	public List<Organization> getOrganizations() throws Exception {
		OrganizationDao orgDao = new OrganizationDao();
		organizations = orgDao.fetch();
		
		return organizations;
	}
	public List<KinekPoint> getKinekPoints() throws Exception {
		
		KinekPointDao kpDao = new KinekPointDao();
		kinekPoints = kpDao.fetchLight();
		
		return kinekPoints;
	}	
	
	public int getRegionId(){
		return regionId;
	}
	public void setRegionId(int regionId){
		this.regionId = regionId;
	}	
	
	public int getOrganizationId(){
		return organizationId;
	}
	public void setOrganizationId(int organizationId){
		this.organizationId = organizationId;
	}
	
	public int getKinekPointId(){
		return kinekPointId;
	}
	public void setKinekPointId(int kinekPointId){
		this.kinekPointId = kinekPointId;
	}
	
		
	public String getTargetEmailDelivery() {
		return targetEmailDelivery;
	}

	public void setTargetEmailDelivery(String targetEmailDelivery) {
		this.targetEmailDelivery = targetEmailDelivery;
	}

	public String getTargetEmailDeliveryReminder() {
		return targetEmailDeliveryReminder;
	}

	public void setTargetEmailDeliveryReminder(String targetEmailDeliveryReminder) {
		this.targetEmailDeliveryReminder = targetEmailDeliveryReminder;
	}

	public String getTargetEmailWelcome() {
		return targetEmailWelcome;
	}

	public void setTargetEmailWelcome(String targetEmailWelcome) {
		this.targetEmailWelcome = targetEmailWelcome;
	}
	
	public String getTargetEmailRegistrationReminder() {
		return targetEmailRegistrationReminder;
	}

	public void setTargetEmailRegistrationReminder(String targetEmailRegistrationReminder) {
		this.targetEmailRegistrationReminder = targetEmailRegistrationReminder;
	}	

	public void setAction(Action action){
		this.action = action;
	}
	
	public Action getAction(){
		return action;
	} 
	
	public void setCouponType(CouponType couponType){
		this.couponType = couponType;
	}
	
	public CouponType getCouponType(){
		return couponType;
	}
	
	private String formatDescriptionContent(String description){
		
		Document doc = Jsoup.parseBodyFragment(description);		
		Element body = doc.body();
		
		//FF browser
		//This is normal<span style="font-weight: bold;">test</span><br><span style="font-style: italic;">test</span><br> convert to <span><b>test</b></span><br/><span><i>test</i></span>
		Elements spans = body.getElementsByTag("span");	
		for(Element span:spans){
			String text = span.text();
			if(span.attr("style").trim().equalsIgnoreCase("font-weight: bold;") || span.attr("style").trim().equalsIgnoreCase("font-weight:bold;") || span.attr("style").trim().equalsIgnoreCase("font-weight : bold;")){
				Element b = doc.createElement("b");
				b.append(text);
				span.replaceWith(b);
				
			}else if(span.attr("style").trim().equalsIgnoreCase("font-style: italic;") || span.attr("style").trim().equalsIgnoreCase("font-style:italic;") || span.attr("style").trim().equalsIgnoreCase("font-style : italic;")){
				Element i = doc.createElement("i");
				i.append(text);
				span.replaceWith(i);
				
			}else if(span.attr("style").trim().equalsIgnoreCase("font-weight: bold; font-style: italic;") || span.attr("style").trim().equalsIgnoreCase("font-weight:bold; font-style:italic;") || span.attr("style").trim().equalsIgnoreCase("font-weight : bold; font-style : italic;")){
				Element b = doc.createElement("b");
				Element i = doc.createElement("i");
				b.append(text);
				i.append(b.outerHtml());
				span.replaceWith(i);	
				
			}			
		}				
		
		//IE browser
		//<p>Test normal</p> 
		//<p><strong>Test Bold</strong>&nbsp; </p> 
		//<p><em>Test Italic</em> <strong><em>Test boldItalic</em></strong></p> contvert to </p> tag to Test normal<br /><strong>Test Bold</strong><br /><em>Test Italic</em><strong><em>Test boldItalic</em></strong> 
		Elements ps = body.getElementsByTag("p");
		for(Element p:ps){			
			String ele = p.html();			
			body.append(ele+"<br/>");
		}
		ps.remove();
		
		//now replace the strong tag with b tag 
		Elements strongs = doc.getElementsByTag("strong");
		for(Element strong:strongs){
			String text = strong.html();
			Element b = doc.createElement("b");
			b.append(text);
			strong.replaceWith(b);
		}
		//now replace the em tag with i tag
		Elements ems = doc.getElementsByTag("em");
		for(Element em:ems){
			String text = em.html();
			Element i = doc.createElement("i");
			i.append(text);
			em.replaceWith(i);
		}
		
		//chrome browser
		//<div>Test normal</div> 
		//<div><b>Test Bold</b></div> 
		//<div><i>Test Italic</div> <b><i>Test boldItalic</i></b></div> contvert to <div> tag to Test normal<br /><b>Test Bold</b><br /><i>Test Italic</i><b><i>Test boldItalic</i></b>		
		boolean isFirstDivTag = true;
		Elements divs = body.getElementsByTag("div");
		for(Element div:divs){
			
			String ele = div.html();			

			//chrome does not put <br> tag after each new line so we have to put <br/> tag after first div is encountered. (e.g:This is text<div>This 2nd line</div>)
			if(isFirstDivTag){
				body.append("<br/>"+ele+"<br/>");
				isFirstDivTag = false;
			}else{	
				body.append(ele+"<br/>");
			}
		}		
		divs.remove();
				
		//if image tag contain style="width:px" then remove it and append the width attribute in the img tag
		Elements imgs = doc.getElementsByTag("img");
		String[] styleAttr=null;
		for(Element img:imgs){
			styleAttr = imgs.attr("style").split(";");	
			
			String[] widthAttr = styleAttr[0].split(":");
						
			if(widthAttr[0].trim().equalsIgnoreCase("width")){				
				img.attr("width",widthAttr[1].trim());
			}
			
			if(styleAttr.length>1){				
				String[] heightAttr = styleAttr[1].split(":");
				if(heightAttr[0].trim().equalsIgnoreCase("height")){					
					img.attr("height", heightAttr[1].trim());
				}
			}
						
			//Resolve Chrome and IE bug....Chrome creates width attribute but does not have 'px' (e.g: width:100). So we need to put px next to the value of the width attribute (e.g: width:100px) 
			String width = img.attr("width");
			if((width != null && !width.equals("")) && width.indexOf("px") == -1)
			{
				img.removeAttr("width");				
				img.attr("width",width+"px");
			}
			//Resolve IE bug....IE creates height attribute but does not have 'px' (e.g: height:100). So we need to put px next to the value of the height attribute (e.g: height:100px) 
			String height = img.attr("height");
			if((height != null && !height.equals("")) && height.indexOf("px") == -1)
			{
				img.removeAttr("height");				
				img.attr("height",height+"px");
			}
			
		}
		imgs.removeAttr("style");
		
		//remove any font tag...We will ignore font related tag...some browser (chrome) sends font tag also.
		Elements fonts = body.getElementsByTag("font");
		fonts.remove();
		
		//sometime nicEdit put style attribute inside the <b style="font-style: italic; ">..We have remove it and put <i> tag outside the <b> tag
		Elements bs = doc.getElementsByTag("b");
		for(Element b:bs){			
			if(b.attr("style").trim().equalsIgnoreCase("font-style: italic;") || b.attr("style").trim().equalsIgnoreCase("font-style:italic;") || b.attr("style").trim().equalsIgnoreCase("font-style : italic;")){				
				Element i = doc.createElement("i");
				b.wrap(i.outerHtml());
			}
		}
		bs.removeAttr("style");
		
		
		String formattedDescriton = body.html();
		
		formattedDescriton = formattedDescriton.replaceAll("(\r\n|\n|\r|\n\r)","");
		formattedDescriton = formattedDescriton.replaceAll("\\$", "&#36;");
		formattedDescriton = formattedDescriton.replaceAll("\\%", "&#37;");
		formattedDescriton = formattedDescriton.replaceAll("\\(", "&#40;");
		formattedDescriton = formattedDescriton.replaceAll("\\)", "&#41;");		
		formattedDescriton = formattedDescriton.replaceAll("\\\\", "&#92;");
		formattedDescriton = formattedDescriton.replaceAll("\\*","&#42;");
		formattedDescriton = formattedDescriton.replaceAll("\\+", "&#43;");
		formattedDescriton = formattedDescriton.replaceAll("\\?", "&#63;");
		formattedDescriton = formattedDescriton.replaceAll("\\@", "&#64;");
		formattedDescriton = formattedDescriton.replaceAll("\\[", "&#91;");		
		formattedDescriton = formattedDescriton.replaceAll("\\]", "&#93;");
		formattedDescriton = formattedDescriton.replaceAll("\\^", "&#94;");
		formattedDescriton = formattedDescriton.replaceAll("\\_", "&#95;");
		formattedDescriton = formattedDescriton.replaceAll("\\~", "&#126;");
		
		if(formattedDescriton.equals("<br />")){
			formattedDescriton = "";
		}
		
		return formattedDescriton;
	}
	
	public String getDepotPortalBaseUrl(){
		return ExternalSettingsManager.getDepotPortalBaseUrl();
	}

	public void setCouponImage(FileBean couponImage) {
		this.couponImage = couponImage;
	}

	public FileBean getCouponImage() {
		return couponImage;
	}
	
	public long getImgSize() {
		return imgSize;
	}

	public void setImgSize(long imgSize) {
		this.imgSize = imgSize;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}	
}
