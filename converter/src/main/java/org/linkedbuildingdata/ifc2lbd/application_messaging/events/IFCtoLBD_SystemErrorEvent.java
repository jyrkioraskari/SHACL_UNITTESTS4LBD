package org.linkedbuildingdata.ifc2lbd.application_messaging.events;

/*
 * The GNU Affero General Public License
 * 
 * Copyright (c) 2018 Jyrki Oraskari (Jyrki.Oraskari@aalto.fi / jyrki.oraskari@aalto.fi)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

public class IFCtoLBD_SystemErrorEvent {
    private final String class_name;
	private final String status_message;
	public IFCtoLBD_SystemErrorEvent(String class_name, String txt) {
		this.status_message=txt;
		this.class_name=class_name;
	}
	public String getStatus_message() {
		return status_message;
	}
    public String getClass_name() {
        return class_name;
    }
}
