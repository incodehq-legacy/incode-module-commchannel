/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
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
package org.incode.module.commchannel.dom;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.objectstore.jdo.applib.service.JdoColumnLength;

import org.incode.module.commchannel.CommChannelModule;

/**
 * Domain service that contributes actions to create a new
 * {@link #newPhoneOrFax(CommunicationChannelOwner, CommunicationChannelType, String, String) phone or fax number} for a particular {@link CommunicationChannelOwner}.
 */
@DomainService(
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY
)
public class PhoneOrFaxNumberContributions {

    //region > event classes
    public static abstract class PropertyDomainEvent<T> extends CommChannelModule.PropertyDomainEvent<PhoneOrFaxNumberContributions, T> {
        public PropertyDomainEvent(final PhoneOrFaxNumberContributions source, final Identifier identifier) {
            super(source, identifier);
        }

        public PropertyDomainEvent(final PhoneOrFaxNumberContributions source, final Identifier identifier, final T oldValue, final T newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

    public static abstract class CollectionDomainEvent<T> extends CommChannelModule.CollectionDomainEvent<PhoneOrFaxNumberContributions, T> {
        public CollectionDomainEvent(final PhoneOrFaxNumberContributions source, final Identifier identifier, final Of of) {
            super(source, identifier, of);
        }

        public CollectionDomainEvent(final PhoneOrFaxNumberContributions source, final Identifier identifier, final Of of, final T value) {
            super(source, identifier, of, value);
        }
    }

    public static abstract class ActionDomainEvent extends CommChannelModule.ActionDomainEvent<PhoneOrFaxNumberContributions> {
        public ActionDomainEvent(final PhoneOrFaxNumberContributions source, final Identifier identifier) {
            super(source, identifier);
        }

        public ActionDomainEvent(final PhoneOrFaxNumberContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }

        public ActionDomainEvent(final PhoneOrFaxNumberContributions source, final Identifier identifier, final List<Object> arguments) {
            super(source, identifier, arguments);
        }
    }
    //endregion

    //region > newPhoneOrFax (contributed action)

    public static class NewPhoneOrFaxEvent extends ActionDomainEvent {

        public NewPhoneOrFaxEvent(
                final PhoneOrFaxNumberContributions source,
                final Identifier identifier) {
            super(source, identifier);
        }

        public NewPhoneOrFaxEvent(
                final PhoneOrFaxNumberContributions source,
                final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }

        public NewPhoneOrFaxEvent(
                final PhoneOrFaxNumberContributions source,
                final Identifier identifier, final List<Object> arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = NewPhoneOrFaxEvent.class,
            semantics = SemanticsOf.NON_IDEMPOTENT
    )
    @ActionLayout(
            named = "New Phone/Fax",
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(name = "CommunicationChannels", sequence = "3")
    public CommunicationChannelOwner newPhoneOrFaxNumber(
            @ParameterLayout(named = "Owner")
            final CommunicationChannelOwner owner,
            @ParameterLayout(named = "Type")
            final CommunicationChannelType type,
            @ParameterLayout(named = "Phone Number")
            @Parameter(
                    maxLength = CommChannelModule.JdoColumnLength.PHONE_NUMBER,
                    regexPattern = CommChannelModule.Regex.PHONE_NUMBER
            )
            final String phoneNumber,
            @Parameter(maxLength = JdoColumnLength.DESCRIPTION, optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Description")
            final String description,
            @Parameter(optionality = Optionality.OPTIONAL)
            @ParameterLayout(named = "Notes", multiLine = 10)
            final String notes) {
        phoneOrFaxNumberRepository.newPhoneOrFax(owner, type, phoneNumber, description, notes);
        return owner;
    }

    public List<CommunicationChannelType> choices1NewPhoneOrFaxNumber() {
        return CommunicationChannelType.matching(PhoneOrFaxNumber.class);
    }

    public CommunicationChannelType default1NewPhoneOrFaxNumber() {
        return choices1NewPhoneOrFaxNumber().get(0);
    }

    //endregion

    //region > injected services

    @Inject
    PhoneOrFaxNumberRepository phoneOrFaxNumberRepository;

    //endregion

}
