package com.dizitiveit.pms.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.IOException;

public class C07E04_CreateFromURL {
	
	/**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/url2pdf_1.pdf";

    /**
     * The target folder for the result.
     */
    public static final String ADDRESS = "https://stackoverflow.com/help/on-topic";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    

    /**
     * Creates the PDF file.
     *
     * @param url  the URL object for the web page
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(URL url, ByteArrayOutputStream dest) throws IOException {
        try {
			HtmlConverter.convertToPdf(url.openStream(), dest);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
