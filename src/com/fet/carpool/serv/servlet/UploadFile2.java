package com.fet.carpool.serv.servlet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class UploadFile1
 */
@WebServlet("/servlet/test/UploadFile2")
public class UploadFile2 extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public static final String UPLOAD_TEMPPATH_1 = "/WEB-INF/temp1";
	public static final String UPLOAD_FILEPATH_1 = "/WEB-INF/upload1";
	private static final int STANDARD_FILE_SIZE = 600 * 1024;
	
	private static final DateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final NumberFormat FILE_SEQ_FORMAT = new DecimalFormat("00000");
	private static int FILE_SEQ = 0;

	protected Logger logger;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadFile2() {
		super();
		logger = Logger.getLogger(getClass());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		checkFileExists(request);
		
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		fileItemFactory.setSizeThreshold(1024*1024);
		fileItemFactory.setRepository(new File(request.getServletContext()
				.getRealPath(UPLOAD_TEMPPATH_1)));

		ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
		upload.setFileSizeMax(1024*1024*10);
		
		boolean read = false;
		String fileSeq = null;

		try {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) {
					logger.debug("get form field : " + item.getFieldName()
							+ " / " + item.getString());
					continue;
				}

				if( !read ) {
					fileSeq = processUploadFile(request, response, item);
					logger.info( "save - " + item.getName() );
					read = true;
				}
			}

		} catch (FileUploadException e) {
			logger.error("upload file failed", e);
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
					"upload failed - " + e.getMessage() );
			return;
		}

		if( read ){
			response.setContentType("text/html");
			response.getWriter().print("<script>location.href='http://pc-sim01t.fareastone.com.tw/recharge/setCookie.html?pic="+fileSeq+"';</script>");
		}else
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "no image found." );			
	}

	private String processUploadFile(HttpServletRequest request,
			HttpServletResponse response, FileItem item) throws  ServletException, IOException, FileUploadException {
		
//		String fieldName = item.getFieldName();
		String fileName = item.getName();
//		String contentType = item.getContentType();
//		boolean isInMemory = item.isInMemory();
		long sizeInBytes = item.getSize();
		
		String fileSeqNo;
		synchronized (this) {
			fileSeqNo = FILE_DATE_FORMAT.format(new Date()) + "-" + FILE_SEQ_FORMAT.format(++FILE_SEQ);
		}
		
		// check image 
		logger.debug( "receive file : " + fileName + ", size=" + sizeInBytes );
		InputStream input = item.getInputStream();
		BufferedImage image = ImageIO.read(input);
		if( image == null ) 
			throw new FileUploadException( "Invalid Image Format." );
		
		//
		boolean needResize = ( sizeInBytes > STANDARD_FILE_SIZE ? true : false);
		if( needResize ) {
			logger.debug( "resize image..." );
			image = resizeImage( image, Math.sqrt( (double)STANDARD_FILE_SIZE / sizeInBytes ) );
		}
		
		File tempFile = new File( request.getServletContext().getRealPath(UPLOAD_FILEPATH_1), fileSeqNo + ".jpg" );
		ImageIO.write(image, "JPEG", tempFile);

		
		
		input.close();
		return fileSeqNo;
	}
	
	private BufferedImage resizeImage( BufferedImage original, double scale ) {
		
		// 只用來縮小, 不用 AffineTransform
		
	    final int w = new Double(original.getWidth() * scale).intValue();  
	    final int h = new Double(original.getHeight() * scale).intValue();  
	    final Image rescaled = original.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);  
	    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);  
	    final Graphics2D g = result.createGraphics();  
	    g.drawImage(rescaled, 0, 0, null);  
	    g.dispose();  
	    return result;  
	}
	
	private void checkFileExists(HttpServletRequest request ) {
		File theDir = new File(request.getServletContext()
				.getRealPath(UPLOAD_TEMPPATH_1));
		
		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating directory: user_pic_temp" );
		boolean result = false;
		
		try{
		    theDir.mkdir();
		    result = true;
		 } catch(SecurityException se){
		    //handle it
		 }        
		 if(result) {    
		   System.out.println("DIR created");  
		    }
		 }
		theDir = new File(request.getServletContext()
				.getRealPath(UPLOAD_FILEPATH_1));
		
		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating directory: user_upload" );
		boolean result = false;
		
		try{
		    theDir.mkdir();
		    result = true;
		 } catch(SecurityException se){
		    //handle it
		 }        
		 if(result) {    
		   System.out.println("DIR created");  
		    }
		 }
	}
}
