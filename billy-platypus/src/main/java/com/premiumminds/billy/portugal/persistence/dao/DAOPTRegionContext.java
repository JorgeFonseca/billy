/*******************************************************************************
 * Copyright (C) 2013 Premium Minds.
 *  
 * This file is part of billy-platypus.
 * 
 * billy-platypus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * billy-platypus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy-platypus.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.premiumminds.billy.portugal.persistence.dao;

import com.premiumminds.billy.core.persistence.dao.DAO;
import com.premiumminds.billy.core.persistence.entities.ContextEntity;
import com.premiumminds.billy.portugal.persistence.entities.IPTRegionContextEntity;

public interface DAOPTRegionContext extends DAO<IPTRegionContextEntity> {

	IPTRegionContextEntity getPTRegionContextInstance(String name,
			String description, ContextEntity parent, String regionCode);
	
}
