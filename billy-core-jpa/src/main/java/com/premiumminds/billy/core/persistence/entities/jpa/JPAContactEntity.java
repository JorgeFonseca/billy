/*******************************************************************************
 * Copyright (C) 2013 Premium Minds.
 *  
 * This file is part of billy-core-jpa.
 * 
 * billy-core-jpa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * billy-core-jpa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy-core-jpa.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.premiumminds.billy.core.persistence.entities.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.premiumminds.billy.core.Config;
import com.premiumminds.billy.core.persistence.entities.ContactEntity;

@Entity
@Table(name = Config.TABLE_PREFIX + "CONTACT")
public class JPAContactEntity extends JPABaseEntity
implements ContactEntity{
	private static final long serialVersionUID = 1L;

	@Column(name = "NAME")
	protected String name;
	
	@Column(name = "PHONE")
	protected String phone;
	
	@Column(name = "MOBILE")
	protected String mobile;
	
	@Column(name = "FAX")
	protected String fax;
	
	@Column(name = "EMAIL")
	protected String email;
	
	@Column(name = "WEBSITE")
	protected String website;
	
	
	public JPAContactEntity() {}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getTelephone() {
		return phone;
	}

	@Override
	public String getMobile() {
		return mobile;
	}

	@Override
	public String getFax() {
		return fax;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getWebsite() {
		return website;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setTelephone(String telephone) {
		this.phone = telephone;
	}

	@Override
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setWebsite(String website) {
		this.website = website;
	}

}
