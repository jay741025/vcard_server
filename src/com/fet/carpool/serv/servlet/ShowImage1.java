package com.fet.carpool.serv.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ShowImage1
 */
@WebServlet("/servlet/test/ShowImage1")
public class ShowImage1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected Logger logger;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowImage1() {
        super();
        logger = Logger.getLogger(getClass());
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		showImage( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		showImage( request, response );
	}


	protected void showImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String seq = request.getParameter("seq");
		if( seq == null || seq.length() == 0 ) {
			logger.debug( "seq is empty..." );
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		File imageFile = new File( request.getServletContext().getRealPath( UploadFile1.UPLOAD_FILEPATH_1 ),
				seq + ".jpg" );
		if( !imageFile.exists() ) {
			logger.warn( "image not found - " + imageFile.getAbsolutePath() );
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		response.setContentType("image/jpeg");
		FileInputStream input = new FileInputStream(imageFile);
		OutputStream output = response.getOutputStream();
		byte[] buff = new byte[4096];
		int len;
		while( ( len = input.read(buff)) != -1 )
			output.write(buff, 0, len);
		input.close();
	}
}
