package org.webdev.kpoint.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.util.ApplicationProperty;

/**
 * Servlet implementation class FileUpload
 */
public class CouponImageUploader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final KinekLogger logger = new KinekLogger(CouponImageUploader.class);
	private static final String img_path = ApplicationProperty.getInstance().getProperty("coupon.img.upload.path"); 
	private static final String img_url = ApplicationProperty.getInstance().getProperty("coupon.img.upload.url"); 

	private static final long img_size = Long.parseLong(ApplicationProperty.getInstance().getProperty("coupon.img.size")); 
	private static final int img_width_max = Integer.parseInt(ApplicationProperty.getInstance().getProperty("coupon.img.width"));
	private static final int img_height_max = Integer.parseInt(ApplicationProperty.getInstance().getProperty("coupon.img.height"));
	private boolean hasError = false;
	private String reasonForError = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CouponImageUploader() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!hasError){	
			if(request.getParameter("check") == null){
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				String id = request.getParameter("id");
				if(isMultipart){
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					try {
						List items = upload.parseRequest(request);

						Iterator iter = items.iterator();
						while(iter.hasNext()){
							FileItem item = (FileItem)iter.next();
							//if there is no file name then it is not a file...without a name no file cannot be saved
							if(item.getName() != null && !item.getName().isEmpty()){
								processFormField(item,id);
							}
						}

					} catch (FileUploadException e) {										
						Hashtable<String,String> logProps = new Hashtable<String,String>();
						logProps.put("id", id);
						logger.error(new ApplicationException("Image file could not be uploaded for coupon", e), logProps);
						hasError = true;
						reasonForError = e.getMessage();
					}

					//display image loading animation
					StringBuilder anim = new StringBuilder();
					anim.append("<html><body>");
					anim.append("<div id=\"uploadingMessage\" style=\"text-align: center; font-size: 14px;\">");
					anim.append("<img src=\"http://js.nicedit.com/ajax-loader.gif\" style=\"float: right; margin-right: 40px;\" />");
					anim.append("<strong>Uploading...</strong><br />");
					anim.append("Please wait");
					anim.append("</div>");
					anim.append("</body></html>");

					response.getWriter().write(anim.toString());
					response.getWriter().flush();
				}
			}else{
				//retrives list of files
				File directory = new File(img_path);
				boolean isFound = false;
				String targetFileName = "";

				if(directory.isDirectory()){
					String fileNames[] = directory.list();

					for(String fileName:fileNames){
						String fileId = fileName.split("_")[0];
						if(fileId.equals(request.getParameter("check"))){
							targetFileName = fileName;
							isFound = true;
							break;
						}
					}
				}
				
				if(isFound){			
					//any default image width is necessary...otherwise nicedit will not work with the image
					int width = 150;
					response.setContentType("text/xml");				
					StringBuilder json = new StringBuilder();
					json.append("nicUploadButton.statusCb({");
					json.append("\"name\":\"nicImage\",");
					json.append("\"url\":\""+img_url+targetFileName+"\",");
					json.append("\"width\":\""+width+"\",");
					json.append("\"interval\":\"1000\"");
					json.append("})");

					response.getWriter().write(json.toString());
					response.getWriter().flush();
				}
			}
		}
		else{
			response.setContentType("text/xml");			
			StringBuilder json = new StringBuilder();
			json.append("nicUploadButton.statusCb({");
			json.append("\"error\":\""+ reasonForError + "\"");
			json.append("})");

			response.getWriter().write(json.toString());
			response.getWriter().flush();
			reasonForError = null;
			hasError = false;
		}
	}


	@ValidationMethod(on="processFormField")
	public void processFormFieldValidation(ValidationErrors errors) {
		System.out.println("Test Validation");
		errors.add("imageRestriction", new SimpleError("Image too large"));
	}

	private void processFormField(FileItem item, String id) throws FileUploadException{		
		BufferedImage image;
		boolean dimensionRestrictionsMet = false;
		String name = item.getName();
		String fileName = "";		
		//sometime IE sends path with file name but we need only file name. So we have to parse only file name
		if(name != null){
			fileName = FilenameUtils.getName(name);
		}

		InputStream in = new ByteArrayInputStream(item.get());
		try {
			image = ImageIO.read(in);
			if(image.getWidth() <= img_width_max && image.getHeight() <= img_height_max){
				dimensionRestrictionsMet = true;
			}
		} catch (Exception e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("id", id);
			logProps.put("file name", fileName);
			logger.error(new ApplicationException("There is an exception happended during uploading the image file for coupon", e), logProps);
		}

		if(dimensionRestrictionsMet){
			File uploadedFile = new File(img_path+File.separator+id+"_"+fileName);
			if(item.getSize() < img_size){
				try {						
					item.write(uploadedFile);
				}catch (Exception e) {
					Hashtable<String,String> logProps = new Hashtable<String,String>();
					logProps.put("id", id);
					logProps.put("file name", fileName);
					logger.error(new ApplicationException("There is an exception happended during uploading the image file for coupon", e), logProps);
				}
			}
			else{
				throw new FileUploadException("Too large [Max size:" + img_size/1000 +"kb]");
			}
		}
		else{
			throw new FileUploadException("Too large [Max width:" + img_width_max + "px, Max height:" + img_height_max + "px]");
		}
	}
}
