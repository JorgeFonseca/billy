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
package com.premiumminds.billy.portugal.migration;

import javax.persistence.EntityManager;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.premiumminds.billy.core.persistence.dao.DAO;
import com.premiumminds.billy.core.persistence.dao.TransactionWrapper;
import com.premiumminds.billy.portugal.PortugalDependencyModule;
import com.premiumminds.billy.portugal.PortugalPersistenceDependencyModule;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTCustomer;
import com.premiumminds.billy.portugal.persistence.entities.jpa.QJPAPTCustomerEntity;

public class MigrationTest_1 implements CustomTaskChange {
	
	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFileOpener(ResourceAccessor arg0) {
	}

	@Override
	public void setUp() throws SetupException {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationErrors validate(Database arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(Database database) throws CustomChangeException {
		Injector injector = Guice.createInjector(
				new PortugalDependencyModule(),
				new PortugalPersistenceDependencyModule());
		injector.getInstance(PortugalDependencyModule.Initializer.class);
		injector.getInstance(PortugalPersistenceDependencyModule.Initializer.class);
		final EntityManager em = injector.getInstance(EntityManager.class);
		final QJPAPTCustomerEntity customer = QJPAPTCustomerEntity.jPAPTCustomerEntity;
		
		DAO<?> dao = injector.getInstance(DAOPTCustomer.class);

		try {
			new TransactionWrapper<Void>(dao) {

				@Override
				public Void runTransaction() throws Exception {
					
					return null;
					}
				}.execute();
		} catch(Exception e) {
			
		}
	}

}
