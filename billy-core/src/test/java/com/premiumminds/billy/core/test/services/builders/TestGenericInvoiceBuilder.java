/**
 * Copyright (C) 2013 Premium Minds.
 * 
 * This file is part of billy core.
 * 
 * billy core is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * billy core is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.billy.core.test.services.builders;

import java.util.ArrayList;
import java.util.Currency;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.premiumminds.billy.core.persistence.dao.DAOGenericInvoice;
import com.premiumminds.billy.core.persistence.dao.DAOGenericInvoiceEntry;
import com.premiumminds.billy.core.services.UID;
import com.premiumminds.billy.core.services.entities.documents.GenericInvoice;
import com.premiumminds.billy.core.services.entities.documents.GenericInvoiceEntry;
import com.premiumminds.billy.core.test.AbstractTest;
import com.premiumminds.billy.core.test.fixtures.MockGenericInvoiceEntity;
import com.premiumminds.billy.core.test.fixtures.MockGenericInvoiceEntryEntity;
import com.premiumminds.billy.core.util.BillyMathContext;

public class TestGenericInvoiceBuilder extends AbstractTest {

	private static final String	INVOICE_YML	= AbstractTest.YML_CONFIGS_DIR
													+ "GenericInvoice.yml";
	private static final String	ENTRY_YML	= AbstractTest.YML_CONFIGS_DIR
													+ "GenericInvoiceEntry.yml";

	@Test
	public void doTest() {
		MockGenericInvoiceEntity mock = this.createMockEntity(
				MockGenericInvoiceEntity.class,
				TestGenericInvoiceBuilder.INVOICE_YML);

		mock.setCurrency(Currency.getInstance("EUR"));

		Mockito.when(
				this.getInstance(DAOGenericInvoice.class).getEntityInstance())
				.thenReturn(new MockGenericInvoiceEntity());

		MockGenericInvoiceEntryEntity mockInvoice = this.createMockEntity(
				MockGenericInvoiceEntryEntity.class,
				TestGenericInvoiceBuilder.ENTRY_YML);

		Mockito.when(
				this.getInstance(DAOGenericInvoiceEntry.class).get(
						Matchers.any(UID.class))).thenReturn(mockInvoice);

		mock.getEntries().add(mockInvoice);

		ArrayList<GenericInvoiceEntry> invoiceEntrys = (ArrayList<GenericInvoiceEntry>) mock
				.getEntries();

		GenericInvoice.Builder builder = this
				.getInstance(GenericInvoice.Builder.class);

		GenericInvoiceEntry.Builder invoice1 = this
				.getMock(GenericInvoiceEntry.Builder.class);
		Mockito.when(invoice1.build()).thenReturn(invoiceEntrys.get(0));

		builder.addEntry(invoice1).setCreditOrDebit(mock.getCreditOrDebit())
				.setBatchId(mock.getBatchId()).setDate(mock.getDate())
				.setGeneralLedgerDate(mock.getGeneralLedgerDate())
				.setOfficeNumber(mock.getOfficeNumber())
				.setPaymentTerms(mock.getPaymentTerms())
				.setSelfBilled(mock.selfBilled)
				.setSettlementDate(mock.getSettlementDate())
				.setSettlementDescription(mock.getSettlementDescription())
				.setSettlementDiscount(mock.getSettlementDiscount())
				.setSourceId(mock.getSourceId())
				.setTransactionId(mock.getTransactionId())
				.setCurrency(mock.getCurrency());

		GenericInvoice invoice = builder.build();

		Assert.assertTrue(invoice != null);
		Assert.assertTrue(mock.getAmountWithoutTax().setScale(2, BillyMathContext.get().getRoundingMode()).compareTo(
				invoice.getAmountWithoutTax()) == 0);
		Assert.assertTrue(mock.getAmountWithTax().setScale(2, BillyMathContext.get().getRoundingMode()).compareTo(
				invoice.getAmountWithTax()) == 0);
		Assert.assertTrue(mock.getTaxAmount().setScale(2, BillyMathContext.get().getRoundingMode()).compareTo(invoice.getTaxAmount()) == 0);

		Assert.assertEquals(mock.getCreditOrDebit(), invoice.getCreditOrDebit());
		Assert.assertEquals(mock.getGeneralLedgerDate(),
				invoice.getGeneralLedgerDate());
		Assert.assertEquals(mock.getBatchId(), invoice.getBatchId());
		Assert.assertEquals(mock.getDate(), invoice.getDate());
		Assert.assertEquals(mock.getPaymentTerms(), invoice.getPaymentTerms());

		Assert.assertTrue(invoice.getEntries() != null);
		Assert.assertEquals(invoice.getEntries().size(), mock.getEntries()
				.size());

		for (int i = 0; i < invoice.getEntries().size(); i++) {
			ArrayList<GenericInvoiceEntry> invoices = (ArrayList<GenericInvoiceEntry>) invoice
					.getEntries();
			ArrayList<GenericInvoiceEntry> mockInvoices = (ArrayList<GenericInvoiceEntry>) mock
					.getEntries();
			Assert.assertEquals(invoices.get(i).getUnitAmountWithoutTax(),
					mockInvoices.get(i).getUnitAmountWithoutTax());
			Assert.assertEquals(invoices.get(i).getUnitAmountWithTax(),
					mockInvoices.get(i).getUnitAmountWithTax());
			Assert.assertEquals(invoices.get(i).getUnitTaxAmount(),
					mockInvoices.get(i).getUnitTaxAmount());
			Assert.assertTrue(invoices.get(i).equals(mockInvoices.get(i)));
		}

	}
}