/**
 * Copyright (C) 2013 Premium Minds.
 *
 * This file is part of billy core JPA.
 *
 * billy core JPA is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * billy core JPA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy core JPA. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.billy.core;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class CoreJPABootstrap {

	public static void main(String[] args) {
		CoreJPABootstrap.execute();
	}

	public static void execute() {
		// Load dependency injector
		Injector injector = Guice.createInjector(new CoreJPADependencyModule(),
				new CoreJPAPersistenceDependencyModule());
		injector.getInstance(CoreDependencyModule.Initializer.class);
		injector.getInstance(CoreJPAPersistenceDependencyModule.Initializer.class);
		CoreJPABootstrap.execute(injector);
	}

	public static void execute(Injector dependencyInjector) {
	}

}
