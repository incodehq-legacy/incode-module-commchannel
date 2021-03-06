/*
 *  Copyright 2014~2015 Dan Haywood
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
package org.incode.module.commchannel.integtests.postaladdress;

import java.util.SortedSet;

import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.applib.AbstractSubscriber;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;

import org.incode.module.commchannel.dom.impl.channel.CommunicationChannel;
import org.incode.module.commchannel.dom.impl.postaladdress.PostalAddress;
import org.incode.module.commchannel.dom.impl.postaladdress.PostalAddress_lookupGeocode;
import org.incode.module.commchannel.fixture.dom.CommChannelDemoObject;
import org.incode.module.commchannel.fixture.dom.CommChannelDemoObjectMenu;
import org.incode.module.commchannel.fixture.scripts.teardown.CommChannelDemoObjectsTearDownFixture;
import org.incode.module.commchannel.integtests.CommChannelModuleIntegTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;

public class PostalAddress_lookupGeocode_IntegTest extends CommChannelModuleIntegTest {

    @Inject
    CommChannelDemoObjectMenu commChannelDemoObjectMenu;

    CommChannelDemoObject fredDemoOwner;
    PostalAddress postalAddress;

    PostalAddress_lookupGeocode mixinLookupGeocode(final PostalAddress postalAddress) {
        return mixin(PostalAddress_lookupGeocode.class, postalAddress);
    }

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new CommChannelDemoObjectsTearDownFixture(), null);

        fredDemoOwner = wrap(commChannelDemoObjectMenu).create("Fred");

        wrap(mixinNewPostalAddress(fredDemoOwner)).$$(
                "45", "High Street", "Oxford", null, "OX1", "UK", "Work", "Fred Smith's work", false);

        final SortedSet<CommunicationChannel> communicationChannels = wrap(mixinCommunicationChannels(fredDemoOwner)).$$();
        postalAddress = (PostalAddress) communicationChannels.first();

    }

    public static class ActionImplementationIntegrationTest extends PostalAddress_lookupGeocode_IntegTest {

        @Test
        public void will_always_lookup_as_best_as_possible() throws Exception {

            assumeThat(isInternetReachable(), is(true));

            // given
            assertThat(postalAddress.getGeocodeApiResponseAsJson()).isNull();
            assertThat(postalAddress.getName()).isEqualTo("45, High Stree...ford, OX1, UK");

            // when
            wrap(mixinLookupGeocode(postalAddress)).$$("45, High Street, Oxford, OX1, UK");

            // then
            assertThat(postalAddress.getName()).isEqualTo("45 High St, Oxford OX1, UK");
            assertThat(postalAddress.getFormattedAddress()).isEqualTo("45 High St, Oxford OX1, UK");
            assertThat(postalAddress.getGeocodeApiResponseAsJson()).isNotNull();
            assertThat(postalAddress.getLatLng()).matches("51.752[\\d][\\d][\\d][\\d],-1.250[\\d][\\d][\\d][\\d]");
            assertThat(postalAddress.getPlaceId()).isEqualTo("Eho0NSBIaWdoIFN0LCBPeGZvcmQgT1gxLCBVSw");
            assertThat(postalAddress.getAddressComponents()).isNotNull();

        }
    }

    public static class DefaultsIntegrationTest extends PostalAddress_lookupGeocode_IntegTest {

        @Test
        public void concatenates_all_the_parts_of_the_address_ignoring_any_missing_parts() throws Exception {

            final String defaultAddress = mixinLookupGeocode(postalAddress).default0$$();

            assertThat(defaultAddress).isEqualTo("45, High Street, Oxford, OX1, UK");
        }
    }

    public static class RaisesEventIntegrationTest extends PostalAddress_lookupGeocode_IntegTest {

        @DomainService(nature = NatureOfService.DOMAIN)
        public static class TestSubscriber extends AbstractSubscriber {
            PostalAddress_lookupGeocode.DomainEvent ev;

            @Subscribe
            public void on(PostalAddress_lookupGeocode.DomainEvent ev) {
                this.ev = ev;
            }
        }

        @Inject
        TestSubscriber testSubscriber;

        @Test
        public void happy_case() throws Exception {

            wrap(mixinLookupGeocode(postalAddress)).$$("45, High Street, Oxford, OX1, UK");

            assertThat(testSubscriber.ev).isNotNull();
        }

    }


}