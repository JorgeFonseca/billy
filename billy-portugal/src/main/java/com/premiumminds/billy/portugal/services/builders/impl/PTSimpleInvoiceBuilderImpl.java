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
package com.premiumminds.billy.portugal.services.builders.impl;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.premiumminds.billy.core.exceptions.BillyValidationException;
import com.premiumminds.billy.core.util.BillyValidator;
import com.premiumminds.billy.core.util.Localizer;
import com.premiumminds.billy.portugal.exceptions.BillySimpleInvoiceException;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTBusiness;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTCustomer;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTSimpleInvoice;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTSupplier;
import com.premiumminds.billy.portugal.persistence.entities.PTSimpleInvoiceEntity;
import com.premiumminds.billy.portugal.services.builders.PTSimpleInvoiceBuilder;
import com.premiumminds.billy.portugal.services.entities.PTInvoiceEntry;
import com.premiumminds.billy.portugal.services.entities.PTSimpleInvoice;
import com.premiumminds.billy.portugal.services.entities.PTSimpleInvoice.CLIENTTYPE;

public class PTSimpleInvoiceBuilderImpl<TBuilder extends PTSimpleInvoiceBuilderImpl<TBuilder, TEntry, TDocument>, TEntry extends PTInvoiceEntry, TDocument extends PTSimpleInvoice>
		extends PTInvoiceBuilderImpl<TBuilder, TEntry, TDocument> implements
		PTSimpleInvoiceBuilder<TBuilder, TEntry, TDocument> {

	protected static final Localizer LOCALIZER = new Localizer(
			"com/premiumminds/billy/core/i18n/FieldNames");

	@Inject
	public PTSimpleInvoiceBuilderImpl(DAOPTSimpleInvoice daoPTSimpleInvoice,
			DAOPTBusiness daoPTBusiness, DAOPTCustomer daoPTCustomer,
			DAOPTSupplier daoPTSupplier) {
		super(daoPTSimpleInvoice, daoPTBusiness, daoPTCustomer, daoPTSupplier);
	}

	@Override
	public TBuilder setClientType(CLIENTTYPE type) {
		BillyValidator.mandatory(type, PTGenericInvoiceBuilderImpl.LOCALIZER
				.getString("field.clientType"));
		this.getTypeInstance().setClientType(type);
		return this.getBuilder();
	}

	@Override
	protected PTSimpleInvoiceEntity getTypeInstance() {
		return (PTSimpleInvoiceEntity) super.getTypeInstance();
	}

	@Override
	protected void validateInstance() throws BillyValidationException {
		PTSimpleInvoiceEntity i = this.getTypeInstance();
		BillyValidator.mandatory(i.getClientType(),
				PTGenericInvoiceBuilderImpl.LOCALIZER
						.getString("field.clientType"));
		super.validateInstance();
		
		if (i.getClientType() == CLIENTTYPE.CUSTOMER
				&& i.getAmountWithTax().compareTo(new BigDecimal(1000)) >= 0) {
			throw new BillySimpleInvoiceException(
					"Amount > 1000 for customer simple invoice. Issue invoice");
		} else if (i.getClientType() == CLIENTTYPE.BUSINESS
				&& i.getAmountWithTax().compareTo(new BigDecimal(100)) >= 0) {
			throw new BillySimpleInvoiceException(
					"Amount > 100 for business simple invoice. Issue invoice");
		}
	}

}
