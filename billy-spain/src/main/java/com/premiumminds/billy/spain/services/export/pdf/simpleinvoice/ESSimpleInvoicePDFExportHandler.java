/**
 * Copyright (C) 2013 Premium Minds.
 *
 * This file is part of billy spain (ES Pack).
 *
 * billy spain (ES Pack) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * billy spain (ES Pack) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy spain (ES Pack). If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.billy.spain.services.export.pdf.simpleinvoice;

import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import org.postgresql.util.Base64;

import com.premiumminds.billy.core.persistence.entities.GenericInvoiceEntity;
import com.premiumminds.billy.core.services.UID;
import com.premiumminds.billy.core.services.entities.Payment;
import com.premiumminds.billy.gin.services.ExportServiceRequest;
import com.premiumminds.billy.gin.services.exceptions.ExportServiceException;
import com.premiumminds.billy.gin.services.export.BillyTemplateBundle;
import com.premiumminds.billy.gin.services.export.ParamsTree;
import com.premiumminds.billy.gin.services.export.ParamsTree.Node;
import com.premiumminds.billy.gin.services.impl.pdf.AbstractPDFExportHandler;
import com.premiumminds.billy.spain.Config;
import com.premiumminds.billy.spain.persistence.dao.DAOESSimpleInvoice;
import com.premiumminds.billy.spain.persistence.entities.ESSimpleInvoiceEntity;

public class ESSimpleInvoicePDFExportHandler extends AbstractPDFExportHandler {

	protected static class ESParamKeys {
		public static final String	INVOICE_PAYSETTLEMENT		= "paymentSettlement";
	}

	private DAOESSimpleInvoice	daoESSimpleInvoice;
	private Config				config;

	@Inject
	public ESSimpleInvoicePDFExportHandler(DAOESSimpleInvoice daoESSimpleInvoice) {
		super(daoESSimpleInvoice);
		this.daoESSimpleInvoice = daoESSimpleInvoice;
		this.config = new Config();
	}

	public File toFile(URI fileURI, ESSimpleInvoiceEntity invoice,
			ESSimpleInvoiceTemplateBundle bundle) throws ExportServiceException {
		return super.toFile(fileURI, bundle.getXSLTFileStream(),
				this.mapDocumentToParamsTree(invoice, bundle), bundle);
	}

	protected void toStream(ESSimpleInvoiceEntity invoice,
			OutputStream targetStream, ESSimpleInvoiceTemplateBundle bundle)
		throws ExportServiceException {
		super.getStream(bundle.getXSLTFileStream(),
				this.mapDocumentToParamsTree(invoice, bundle), targetStream,
				bundle);
	}

	protected ParamsTree<String, String> mapDocumentToParamsTree(
			ESSimpleInvoiceEntity invoice, ESSimpleInvoiceTemplateBundle bundle) {

		ParamKeys.ROOT = "invoice";

		ParamsTree<String, String> params = super.mapDocumentToParamsTree(
				invoice, bundle);
		return params;
	}

	private String getVerificationHashString(byte[] hash) {
		String hashString = Base64.encodeBytes(hash);
		String rval = hashString.substring(0, 1) + hashString.substring(10, 11)
				+ hashString.substring(20, 21) + hashString.substring(30, 31);

		return rval;
	}

	@Override
	public <T extends ExportServiceRequest> void export(T request,
			OutputStream targetStream) throws ExportServiceException {

		if (!(request instanceof ESSimpleInvoicePDFExportRequest)) {
			throw new ExportServiceException("Cannot handle request of type "
					+ request.getClass().getCanonicalName());
		}
		ESSimpleInvoicePDFExportRequest exportRequest = (ESSimpleInvoicePDFExportRequest) request;
		UID docUid = exportRequest.getDocumentUID();

		try {
			ESSimpleInvoiceEntity simpleInvoice = (ESSimpleInvoiceEntity) this.daoESSimpleInvoice
					.get(docUid);
			this.toStream(simpleInvoice, targetStream,
					exportRequest.getBundle());
		} catch (Exception e) {
			throw new ExportServiceException(e);
		}
	}

	@Override
	public <T extends BillyTemplateBundle, K extends GenericInvoiceEntity> void setHeader(
			ParamsTree<String, String> params, K document, T bundle) {
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

		params.getRoot().addChild(ParamKeys.ID, document.getNumber());

		if (null != document.getPayments()) {
			for(Payment p : document.getPayments()) {
			params.getRoot().addChild(
					ParamKeys.INVOICE_PAYMETHOD,
					this.getPaymentMechanismTranslation(
							p.getPaymentMethod(), bundle));
			}
		}

		params.getRoot().addChild(ParamKeys.EMISSION_DATE,
				date.format(document.getDate()));
		return;

	}

	@Override
	protected void setCustomer(ParamsTree<String, String> params,
			GenericInvoiceEntity document, BillyTemplateBundle bundle) {

		Node<String, String> customer = params.getRoot().addChild(
				ParamKeys.CUSTOMER);

		customer.addChild(ParamKeys.CUSTOMER_FINANCIAL_ID,
				this.getCustomerFinancialId(document, bundle));
		return;
	}

	@Override
	protected void setTaxDetails(TaxTotals taxTotals,
			Node<String, String> taxDetails) {
		return;
	}

	@Override
	public <T extends BillyTemplateBundle, K extends GenericInvoiceEntity> String getCustomerFinancialId(
			K invoice, T bundle) {
		return (invoice.getCustomer().getTaxRegistrationNumber());
	}
}
