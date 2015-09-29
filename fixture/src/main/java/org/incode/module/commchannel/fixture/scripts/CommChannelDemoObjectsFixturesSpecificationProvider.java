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
package org.incode.module.commchannel.fixture.scripts;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecification;
import org.apache.isis.applib.services.fixturespec.FixtureScriptsSpecificationProvider;

import org.incode.module.commchannel.fixture.scripts.scenarios.CommChannelDemoObjectsFixture;

/**
 * Specifies where to find fixtures, and other settings.
 */
@DomainService(nature = NatureOfService.DOMAIN)
public class CommChannelDemoObjectsFixturesSpecificationProvider
        implements FixtureScriptsSpecificationProvider {

    @Override
    public FixtureScriptsSpecification getSpecification() {
        return FixtureScriptsSpecification
                .builder(CommChannelDemoObjectsFixturesSpecificationProvider.class)
                .with(FixtureScripts.MultipleExecutionStrategy.EXECUTE)
                .withRunScriptDefault(CommChannelDemoObjectsFixture.class)
                .withRunScriptDropDown(FixtureScriptsSpecification.DropDownPolicy.CHOICES)
                .withRecreate(CommChannelDemoObjectsFixture.class)
                .build();

    }
}
