/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.incode.module.commchannel.dom.gmap3;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import org.isisaddons.wicket.gmap3.cpt.applib.LocationDereferencingService;

import org.incode.module.commchannel.dom.CommunicationChannel;

@DomainService(
        nature = NatureOfService.DOMAIN
)
public class LocationDereferencingServiceForCommunicationChannels implements LocationDereferencingService {

    @Programmatic
	public Object dereference(final Object locatable) {
		return locatable instanceof CommunicationChannel
				? ((CommunicationChannel) locatable).getOwner()
				: null;
	}
}
