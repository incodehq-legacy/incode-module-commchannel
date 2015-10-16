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
package org.incode.module.commchannel.dom.api.owner;

import org.incode.module.commchannel.dom.CommChannelModule;

public interface CommunicationChannelOwner  {

    abstract class PropertyDomainEvent<S,T> extends CommChannelModule.PropertyDomainEvent<S, T> { }
    abstract class CollectionDomainEvent<S,T> extends CommChannelModule.CollectionDomainEvent<S, T> { }
    abstract class ActionDomainEvent<S> extends CommChannelModule.ActionDomainEvent<S> { }

}