/*
 *
 *  Copyright 2015 incode.org
 *
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
package org.incode.module.commchannel.dom.impl.postaladdress;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;

import org.incode.module.commchannel.dom.api.geocoding.GeocodingService;

@DomainService(
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY
)
public class PostalAddress_lookupGeocode {

    @Inject
    PostalAddress_updateAddress postalAddressUpdateAddress;
    @Inject
    GeocodingService geocodingService;


    public static class LookupGeocodeEvent extends PostalAddress.ActionDomainEvent<PostalAddress_lookupGeocode> { }
    @Action(
            semantics = SemanticsOf.IDEMPOTENT,
            domainEvent = LookupGeocodeEvent.class
    )
    public PostalAddress lookupGeocode(
            final PostalAddress postalAddress,
            @ParameterLayout(named = "Address")
            final String address) {

        postalAddressUpdateAddress.lookupAndUpdateGeocode(postalAddress, true, address);

        return postalAddress;
    }

    public String default1LookupGeocode(
            final PostalAddress postalAddress
    ) {
        return geocodingService.combine(
                GeocodingService.Encoding.NOT_ENCODED,
                postalAddress.getAddressLine1(), postalAddress.getAddressLine2(),
                postalAddress.getAddressLine3(), postalAddress.getAddressLine4(),
                postalAddress.getPostalCode(), postalAddress.getCountry());
    }



}