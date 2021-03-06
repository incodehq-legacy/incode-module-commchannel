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
import org.incode.module.commchannel.dom.impl.postaladdress.PostalAddress_clearGeocode;
import org.incode.module.commchannel.fixture.dom.CommChannelDemoObject;
import org.incode.module.commchannel.fixture.dom.CommChannelDemoObjectMenu;
import org.incode.module.commchannel.fixture.scripts.teardown.CommChannelDemoObjectsTearDownFixture;
import org.incode.module.commchannel.integtests.CommChannelModuleIntegTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;

public class PostalAddress_clearGeocode_IntegTest extends CommChannelModuleIntegTest {

    @Inject
    CommChannelDemoObjectMenu commChannelDemoObjectMenu;

    CommChannelDemoObject fredDemoOwner;
    PostalAddress postalAddress;

    PostalAddress_clearGeocode mixinResetGeocode(final PostalAddress postalAddress) {
        return mixin(PostalAddress_clearGeocode.class, postalAddress);
    }

    @Before
    public void setUpData() throws Exception {
        fixtureScripts.runFixtureScript(new CommChannelDemoObjectsTearDownFixture(), null);

        fredDemoOwner = wrap(commChannelDemoObjectMenu).create("Fred");

        wrap(mixinNewPostalAddress(fredDemoOwner)).$$(
                "45", "High Street", "Oxford", null, "OX1", "UK", "Work", "Fred Smith's work", true);

        final SortedSet<CommunicationChannel> communicationChannels = wrap(mixinCommunicationChannels(fredDemoOwner)).$$();
        postalAddress = (PostalAddress) communicationChannels.first();

    }

    public static class ActionImplementationIntegrationTest extends PostalAddress_clearGeocode_IntegTest {

        @Test
        public void clear() throws Exception {

            assumeThat(isInternetReachable(), is(true));

            // given
            assertThat(postalAddress.getGeocodeApiResponseAsJson()).isNotNull();
            assertThat(postalAddress.getName()).isEqualTo("45 High St, Oxford OX1, UK");

            // when
            wrap(mixinResetGeocode(postalAddress)).$$();

            // then
            assertThat(postalAddress.getName()).isEqualTo("45, High Stree...ford, OX1, UK");
            assertThat(postalAddress.getFormattedAddress()).isNull();
            assertThat(postalAddress.getGeocodeApiResponseAsJson()).isNull();
            assertThat(postalAddress.getLatLng()).isNull();
            assertThat(postalAddress.getPlaceId()).isNull();
            assertThat(postalAddress.getAddressComponents()).isNull();
        }
    }


    public static class RaisesEventIntegrationTest extends PostalAddress_clearGeocode_IntegTest {

        @DomainService(nature = NatureOfService.DOMAIN)
        public static class TestSubscriber extends AbstractSubscriber {
            PostalAddress_clearGeocode.DomainEvent ev;

            @Subscribe
            public void on(PostalAddress_clearGeocode.DomainEvent ev) {
                this.ev = ev;
            }
        }

        @Inject
        TestSubscriber testSubscriber;

        @Test
        public void happy_case() throws Exception {

            wrap(mixinResetGeocode(postalAddress)).$$();

            assertThat(testSubscriber.ev).isNotNull();
        }
    }

}