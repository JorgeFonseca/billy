/**
 * Copyright (C) 2013 Premium Minds.
 *
 * This file is part of billy portugal (PT Pack).
 *
 * billy portugal (PT Pack) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * billy portugal (PT Pack) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy portugal (PT Pack). If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.billy.portugal.test.services.export;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import com.premiumminds.billy.gin.services.exceptions.ExportServiceException;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTReceiptInvoice;
import com.premiumminds.billy.portugal.persistence.entities.PTReceiptInvoiceEntity;
import com.premiumminds.billy.portugal.services.export.pdf.receiptinvoice.PTReceiptInvoicePDFExportHandler;
import com.premiumminds.billy.portugal.services.export.pdf.receiptinvoice.PTReceiptInvoiceTemplateBundle;
import com.premiumminds.billy.portugal.test.PTAbstractTest;
import com.premiumminds.billy.portugal.test.PTPersistencyAbstractTest;
import com.premiumminds.billy.portugal.test.util.PTReceiptInvoiceTestUtil;
import com.premiumminds.billy.portugal.util.PaymentMechanism;

public class TestPTReceiptInvoicePDFExportHandler extends
	PTPersistencyAbstractTest {

	public static final int		NUM_ENTRIES					= 10;
	public static final String	XSL_PATH					= "src/main/resources/pt_receiptinvoice.xsl";
	public static final String	LOGO_PATH					= "src/main/resources/logoBig.png";
	public static final String	URI_PATH					= "file://"
																	+ System.getProperty("java.io.tmpdir")
																	+ "/Result.pdf";

	public static final String	SOFTWARE_CERTIFICATE_NUMBER	= "4321";

	@Test
	public void testPDFcreation() throws NoSuchAlgorithmException,
		ExportServiceException, FileNotFoundException, URISyntaxException {
		InputStream xsl = new FileInputStream(
				TestPTReceiptInvoicePDFExportHandler.XSL_PATH);

		PTReceiptInvoiceTemplateBundle bundle = new PTReceiptInvoiceTemplateBundle(
				TestPTReceiptInvoicePDFExportHandler.LOGO_PATH, xsl,
				TestPTReceiptInvoicePDFExportHandler.SOFTWARE_CERTIFICATE_NUMBER);
		PTReceiptInvoicePDFExportHandler handler = new PTReceiptInvoicePDFExportHandler(
				PTAbstractTest.injector.getInstance(DAOPTReceiptInvoice.class));
		handler.toFile(new URI(TestPTReceiptInvoicePDFExportHandler.URI_PATH),
				this.generatePTReceiptInvoice(PaymentMechanism.CASH), bundle);
	}

	private PTReceiptInvoiceEntity generatePTReceiptInvoice(
			PaymentMechanism paymentMechanism) {

		PTReceiptInvoiceEntity receiptInvoice = new PTReceiptInvoiceTestUtil(
				PTAbstractTest.injector).getReceiptInvoiceEntity();
		receiptInvoice
				.setHash("mYJEv4iGwLcnQbRD7dPs2uD1mX08XjXIKcGg3GEHmwMhmmGYusffIJjTdSITLX+uujTwzqmL/U5nvt6S9s8ijN3LwkJXsiEpt099e1MET/J8y3+Y1bN+K+YPJQiVmlQS0fXETsOPo8SwUZdBALt0vTo1VhUZKejACcjEYJ9G6nI=");

		return receiptInvoice;
	}
}
