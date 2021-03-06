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
package com.premiumminds.billy.spain.util;

import com.google.inject.Injector;
import com.premiumminds.billy.core.services.builders.impl.BuilderManager;
import com.premiumminds.billy.spain.services.entities.ESApplication;
import com.premiumminds.billy.spain.services.persistence.ESApplicationPersistenceService;

public class Applications {

	private final Injector	injector;
	private final ESApplicationPersistenceService persistenceService;


	public Applications(Injector injector) {
		this.injector = injector;
		this.persistenceService = getInstance(ESApplicationPersistenceService.class);
	}

	public ESApplication.Builder builder() {
		return getInstance(ESApplication.Builder.class);
	}
	
	public ESApplication.Builder builder(ESApplication application) {
		ESApplication.Builder builder = getInstance(ESApplication.Builder.class);
		BuilderManager.setTypeInstance(builder, application);
		return builder;
	}
	
	public ESApplicationPersistenceService persistence() {
		return this.persistenceService;
	}

	private <T> T getInstance(Class<T> clazz) {
		return this.injector.getInstance(clazz);
	}
	
}
