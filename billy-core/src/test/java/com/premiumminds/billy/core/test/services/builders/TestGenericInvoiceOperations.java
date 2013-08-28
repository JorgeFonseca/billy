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

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Currency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.premiumminds.billy.core.persistence.dao.DAOContext;
import com.premiumminds.billy.core.persistence.dao.DAOGenericInvoice;
import com.premiumminds.billy.core.persistence.dao.DAOGenericInvoiceEntry;
import com.premiumminds.billy.core.persistence.entities.GenericInvoiceEntity;
import com.premiumminds.billy.core.services.UID;
import com.premiumminds.billy.core.services.entities.Context;
import com.premiumminds.billy.core.services.entities.documents.GenericInvoice;
import com.premiumminds.billy.core.services.entities.documents.GenericInvoiceEntry;
import com.premiumminds.billy.core.test.AbstractTest;
import com.premiumminds.billy.core.test.fixtures.MockGenericInvoiceEntity;
import com.premiumminds.billy.core.test.fixtures.MockGenericInvoiceEntryEntity;
import com.premiumminds.billy.core.util.BillyMathContext;

public class TestGenericInvoiceOperations extends AbstractTest {

	private static final String INVOICE_YML = AbstractTest.YML_CONFIGS_DIR
			+ "GenericInvoice.yml";
	private static final String ENTRY_YML = AbstractTest.YML_CONFIGS_DIR
			+ "GenericInvoiceEntry.yml";
	private MathContext mc = BillyMathContext.get();
	private BigDecimal qnt = new BigDecimal("46");
	private BigDecimal tax = new BigDecimal("0.23");
	private BigDecimal testValue = new BigDecimal("0.12345");
	private BigDecimal testValue2 = new BigDecimal("0.98765");
	private MockGenericInvoiceEntity mockInvoiceEntity;

	@Before
	public void setUp() {
		mockInvoiceEntity = this.createMockEntity(
				MockGenericInvoiceEntity.class,
				TestGenericInvoiceOperations.INVOICE_YML);
		mockInvoiceEntity.setCurrency(Currency.getInstance("EUR"));

		Mockito.when(
				this.getInstance(DAOGenericInvoice.class).getEntityInstance())
				.thenReturn(new MockGenericInvoiceEntity());
		Mockito.when(
				this.getInstance(DAOContext.class).isSubContext(
						Matchers.any(Context.class),
						Matchers.any(Context.class))).thenReturn(true);

	}

	@Test
	public void simpleOperationsTest() {
		MockGenericInvoiceEntryEntity mockEntry = this.getMockEntryEntity(
				mockInvoiceEntity, testValue2);
		Mockito.when(
				this.getInstance(DAOGenericInvoiceEntry.class).get(
						Matchers.any(UID.class))).thenReturn(mockEntry);
		mockInvoiceEntity.getEntries().clear();
		mockInvoiceEntity.getEntries().add(mockEntry);
		
		BigDecimal result1 = mockEntry.getUnitAmountWithoutTax().setScale(2, mc.getRoundingMode()).multiply(qnt);
		BigDecimal result2 = mockEntry.getUnitAmountWithTax().setScale(2, mc.getRoundingMode()).multiply(qnt);
		BigDecimal result3 = mockEntry.getUnitTaxAmount().setScale(2, mc.getRoundingMode()).multiply(qnt);

		GenericInvoiceEntry.Builder invoiceEntry = this
				.getMock(GenericInvoiceEntry.Builder.class);
		Mockito.when(invoiceEntry.build()).thenReturn(mockEntry);

		GenericInvoice.Builder builder = getBuilder();
		builder.addEntry(invoiceEntry);

		GenericInvoice invoice = builder.build();

		Assert.assertTrue(invoice != null);
		Assert.assertTrue(invoice.getAmountWithoutTax().compareTo(
				result1) == 0);
		Assert.assertTrue(invoice.getAmountWithTax().compareTo(
				result2) == 0);
		Assert.assertTrue(invoice.getTaxAmount().compareTo(
				result3) == 0);

	}

	@Test
	public void operationsTest() {
		MockGenericInvoiceEntryEntity mockEntry = this.getMockEntryEntity(
				mockInvoiceEntity, testValue);

		MockGenericInvoiceEntryEntity mockEntry2 = this.getMockEntryEntity(
				mockInvoiceEntity, testValue2);
		
		
		BigDecimal result1 = mockEntry.getUnitAmountWithoutTax().setScale(2, mc.getRoundingMode()).multiply(qnt).add(mockEntry2.getUnitAmountWithoutTax().setScale(2, mc.getRoundingMode()).multiply(qnt), mc);
		BigDecimal result2 = mockEntry.getUnitAmountWithTax().setScale(2, mc.getRoundingMode()).multiply(qnt).add(mockEntry2.getUnitAmountWithTax().setScale(2, mc.getRoundingMode()).multiply(qnt), mc);
		BigDecimal result3 = mockEntry.getUnitTaxAmount().setScale(2, mc.getRoundingMode()).multiply(qnt).add(mockEntry2.getUnitTaxAmount().setScale(2, mc.getRoundingMode()).multiply(qnt), mc);

		Mockito.when(
				this.getInstance(DAOGenericInvoiceEntry.class).get(
						Matchers.any(UID.class))).thenReturn(mockEntry);
		mockInvoiceEntity.getEntries().clear();
		mockInvoiceEntity.getEntries().add(mockEntry);

		GenericInvoiceEntry.Builder invoiceEntry = this
				.getMock(GenericInvoiceEntry.Builder.class);
		Mockito.when(invoiceEntry.build()).thenReturn(mockEntry);

		GenericInvoiceEntry.Builder invoiceEntry2 = this
				.getMock(GenericInvoiceEntry.Builder.class);
		Mockito.when(invoiceEntry2.build()).thenReturn(mockEntry2);

		GenericInvoice.Builder builder = getBuilder();
		builder.addEntry(invoiceEntry).addEntry(invoiceEntry2);

		GenericInvoice invoice = builder.build();

		Assert.assertTrue(invoice != null);

		System.out.println(invoice.getAmountWithoutTax() + " " + result1);
		
		Assert.assertTrue(invoice.getAmountWithoutTax().compareTo(
				result1) == 0);
		Assert.assertTrue(invoice.getAmountWithTax().compareTo(
				result2) == 0);
		Assert.assertTrue(invoice.getTaxAmount().compareTo(
				result3) == 0);

	}

	@Test
	public void manyEntriesTest() {
		BigDecimal taxAmount = BigDecimal.ZERO;
		BigDecimal amountWithTax = BigDecimal.ZERO;
		BigDecimal amountWithoutTax = BigDecimal.ZERO;
		mockInvoiceEntity.getEntries().clear();

		GenericInvoice.Builder builder = getBuilder();

		for (int i = 0; i < 20; i++) {
			MockGenericInvoiceEntryEntity mockEntry = this.getMockEntryEntity(
					mockInvoiceEntity, new BigDecimal("5.977"));
			taxAmount = taxAmount.add(mockEntry.getTaxAmount().setScale(2,  mc.getRoundingMode()), mc);
			amountWithTax = amountWithTax.add(mockEntry.getAmountWithTax().setScale(2,  mc.getRoundingMode()), mc);
			amountWithoutTax = amountWithoutTax.add(
					mockEntry.getAmountWithoutTax().setScale(2,  mc.getRoundingMode()), mc);

			Mockito.when(
					this.getInstance(DAOGenericInvoiceEntry.class).get(
							Matchers.any(UID.class))).thenReturn(mockEntry);
			mockInvoiceEntity.getEntries().add(mockEntry);

			GenericInvoiceEntry.Builder invoiceEntry = this
					.getMock(GenericInvoiceEntry.Builder.class);
			Mockito.when(invoiceEntry.build()).thenReturn(mockEntry);

			builder.addEntry(invoiceEntry);

		}

		GenericInvoice invoice = builder.build();

		Assert.assertTrue(invoice != null);
		System.out.println(invoice.getAmountWithoutTax() + " " + amountWithoutTax.setScale(2, mc.getRoundingMode()));
		Assert.assertTrue(invoice.getAmountWithoutTax().compareTo(
				amountWithoutTax.setScale(2, mc.getRoundingMode())) == 0);
		System.out.println(invoice.getAmountWithTax() + " " + amountWithTax.setScale(2, mc.getRoundingMode()));
		Assert.assertTrue(invoice.getAmountWithTax().compareTo(
				amountWithTax.setScale(2, mc.getRoundingMode())) == 0);
		Assert.assertTrue(invoice.getTaxAmount().compareTo(
				taxAmount.setScale(2, mc.getRoundingMode())) == 0);

	}

	@Test
	public void testRoundingValues() {
		mockInvoiceEntity.getEntries().clear();
		MockGenericInvoiceEntryEntity mockEntry1 = getMockEntryEntity(
				mockInvoiceEntity, new BigDecimal("0.33"));
		MockGenericInvoiceEntryEntity mockEntry2 = getMockEntryEntity(
				mockInvoiceEntity, new BigDecimal("0.33"));

		mockInvoiceEntity.getEntries().add(mockEntry1);
		mockInvoiceEntity.getEntries().add(mockEntry2);

		Mockito.when(
				this.getInstance(DAOGenericInvoiceEntry.class).get(
						Matchers.any(UID.class))).thenReturn(mockEntry1);

		GenericInvoice.Builder builder = getBuilder();

		GenericInvoiceEntry.Builder invoiceEntry1 = this
				.getMock(GenericInvoiceEntry.Builder.class);
		Mockito.when(invoiceEntry1.build()).thenReturn(mockEntry1);

		GenericInvoiceEntry.Builder invoiceEntry2 = this
				.getMock(GenericInvoiceEntry.Builder.class);
		Mockito.when(invoiceEntry2.build()).thenReturn(mockEntry2);

		builder.addEntry(invoiceEntry1).addEntry(invoiceEntry2);

		GenericInvoice invoice = builder.build();

		Assert.assertTrue(invoice.getTaxAmount().compareTo(
				mockEntry1
						.getTaxAmount()
						.setScale(2, mc.getRoundingMode())
						.add(mockEntry2.getTaxAmount().setScale(2,
								mc.getRoundingMode()), mc)) == 0);

		Assert.assertTrue(invoice.getAmountWithoutTax().compareTo(
				mockEntry1
						.getAmountWithoutTax()
						.setScale(2, mc.getRoundingMode())
						.add(mockEntry2.getAmountWithoutTax().setScale(2,
								mc.getRoundingMode()), mc)) == 0);

		Assert.assertTrue(invoice.getAmountWithTax().compareTo(
				mockEntry1
						.getAmountWithTax()
						.setScale(2, mc.getRoundingMode())
						.add(mockEntry2.getAmountWithTax().setScale(2,
								mc.getRoundingMode()), mc)) == 0);

	}

	public MockGenericInvoiceEntryEntity getMockEntryEntity(
			MockGenericInvoiceEntity invoice, BigDecimal unitValue) {

		MockGenericInvoiceEntryEntity result = this.createMockEntity(
				MockGenericInvoiceEntryEntity.class,
				TestGenericInvoiceOperations.ENTRY_YML);

		result.setCurrency(Currency.getInstance("EUR"));
		result.getDocumentReferences().add(invoice);

		result.unitAmountWithoutTax = unitValue;

		result.unitTaxAmount = result.unitAmountWithoutTax.setScale(BillyMathContext.SCALE).multiply(this.tax,
				this.mc);

		result.unitAmountWithTax = result.unitAmountWithoutTax.setScale(BillyMathContext.SCALE).add(
				result.unitTaxAmount, mc);

		result.amountWithoutTax = result.unitAmountWithoutTax.setScale(BillyMathContext.SCALE).multiply(
				this.qnt, this.mc);
		result.amountWithTax = result.unitAmountWithTax.setScale(BillyMathContext.SCALE).multiply(this.qnt,
				this.mc);
		result.taxAmount = result.unitTaxAmount.setScale(BillyMathContext.SCALE, mc.getRoundingMode()).multiply(this.qnt, this.mc);

		return result;
	}

	public GenericInvoice.Builder getBuilder() {
		GenericInvoice.Builder builder = this
				.getInstance(GenericInvoice.Builder.class);

		builder.setCurrency(Currency.getInstance("EUR"))
				.setCreditOrDebit(mockInvoiceEntity.getCreditOrDebit())
				.setBatchId(mockInvoiceEntity.getBatchId())
				.setDate(mockInvoiceEntity.getDate())
				.setGeneralLedgerDate(mockInvoiceEntity.getGeneralLedgerDate())
				.setOfficeNumber(mockInvoiceEntity.getOfficeNumber())
				.setPaymentTerms(mockInvoiceEntity.getPaymentTerms())
				.setSelfBilled(mockInvoiceEntity.selfBilled)
				.setSettlementDate(mockInvoiceEntity.getSettlementDate())
				.setSettlementDescription(
						mockInvoiceEntity.getSettlementDescription())
				.setSettlementDiscount(
						mockInvoiceEntity.getSettlementDiscount())
				.setSourceId(mockInvoiceEntity.getSourceId())
				.setTransactionId(mockInvoiceEntity.getTransactionId());

		return builder;
	}
}
