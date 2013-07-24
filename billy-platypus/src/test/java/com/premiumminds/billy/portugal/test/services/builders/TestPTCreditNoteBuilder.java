package com.premiumminds.billy.portugal.test.services.builders;

import java.util.ArrayList;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.premiumminds.billy.core.services.UID;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTCreditNote;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTCreditNoteEntry;
import com.premiumminds.billy.portugal.services.entities.PTCreditNote;
import com.premiumminds.billy.portugal.services.entities.PTCreditNoteEntry;
import com.premiumminds.billy.portugal.test.PTAbstractTest;
import com.premiumminds.billy.portugal.test.fixtures.MockPTCreditNoteEntity;
import com.premiumminds.billy.portugal.test.fixtures.MockPTCreditNoteEntryEntity;

public class TestPTCreditNoteBuilder extends PTAbstractTest{

	private static final String PT_CREDIT_NOTE_YML = "src/test/resources/PTCreditNote.yml";
	private static final String PT_CREDIT_NOTE_ENTRY_YML = "src/test/resources/PTCreditNoteEntry.yml";
	
	@Test
	public void doTest(){
		MockPTCreditNoteEntity mock = this.createMockEntity(MockPTCreditNoteEntity.class, PT_CREDIT_NOTE_YML);
		
		mock.setCurrency(Currency.getInstance("EUR"));
		
		Mockito.when(this.getInstance(DAOPTCreditNote.class).getEntityInstance()).thenReturn(new MockPTCreditNoteEntity());
		
		MockPTCreditNoteEntryEntity entryMock = createMockEntity(MockPTCreditNoteEntryEntity.class, PT_CREDIT_NOTE_ENTRY_YML);
		
		Mockito.when(getInstance(DAOPTCreditNoteEntry.class).get(Matchers.any(UID.class))).thenReturn(entryMock);
		
		mock.getEntries().add(entryMock);
		
		ArrayList<PTCreditNoteEntry> creditNodeEntries= (ArrayList<PTCreditNoteEntry>) mock.getEntries();
		
		PTCreditNote.Builder builder = getInstance(PTCreditNote.Builder.class);
		
		PTCreditNoteEntry.Builder entry1 = this.getMock(PTCreditNoteEntry.Builder.class);
		Mockito.when(entry1.build()).thenReturn(creditNodeEntries.get(0));
		
		builder.addEntry(entry1)
		.setBatchId(mock.getBatchId())
		.setCreditOrDebit(mock.getCreditOrDebit())
		.setDate(mock.getDate())
		.setGeneralLedgerDate(mock.getGeneralLedgerDate())
		.setOfficeNumber(mock.getOfficeNumber())
		.setPaymentTerms(mock.getPaymentTerms())
		.setSelfBilled(mock.selfBilled)
		.setSettlementDate(mock.getSettlementDate())
		.setSettlementDescription(mock.getSettlementDescription())
		.setSettlementDiscount(mock.getSettlementDiscount())
		.setSourceId(mock.getSourceId())
		.setTransactionId(mock.getTransactionId());
		
		PTCreditNote creditNote = builder.build();
		
		assertTrue(creditNote != null);
		assertTrue(creditNote.getEntries() != null);
		assertEquals(creditNote.getEntries().size(), mock.getEntries().size());

		assertTrue(creditNote.isBilled() == mock.isBilled());
		assertTrue(creditNote.isCancelled() == mock.isCancelled());
		
		assertEquals(mock.getCreditOrDebit(), creditNote.getCreditOrDebit());
		assertEquals(mock.getGeneralLedgerDate(), creditNote.getGeneralLedgerDate());
		assertEquals(mock.getBatchId(), creditNote.getBatchId());
		assertEquals(mock.getDate(), creditNote.getDate());
		assertEquals(mock.getPaymentTerms(), creditNote.getPaymentTerms());
		
		assertTrue(mock.getAmountWithoutTax().compareTo(creditNote.getAmountWithoutTax()) == 0);
		assertTrue(mock.getAmountWithTax().compareTo(creditNote.getAmountWithTax()) == 0);
		assertTrue(mock.getTaxAmount().compareTo(creditNote.getTaxAmount()) == 0);
		
		
	}
}
