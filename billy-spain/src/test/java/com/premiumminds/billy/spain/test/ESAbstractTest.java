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
package com.premiumminds.billy.spain.test;

import org.junit.BeforeClass;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.premiumminds.billy.core.test.AbstractTest;
import com.premiumminds.billy.spain.SpainDependencyModule;

public class ESAbstractTest extends AbstractTest {

	protected static Injector		injector;
	protected static final String	ES_COUNTRY_CODE	= "ES";

	@BeforeClass
	public static void setUpClass() {
		ESAbstractTest.injector = Guice.createInjector(Modules.override(
				new SpainDependencyModule()).with(
				new ESMockDependencyModule()));
	}

	public <T> T getInstance(Class<T> clazz) {
		return ESAbstractTest.injector.getInstance(clazz);
	}

}
