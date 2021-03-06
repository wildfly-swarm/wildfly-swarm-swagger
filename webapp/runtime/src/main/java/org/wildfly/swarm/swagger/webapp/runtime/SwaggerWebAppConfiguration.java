/**
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.swagger.webapp.runtime;

import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.Archive;
import org.wildfly.swarm.spi.api.ArtifactLookup;
import org.wildfly.swarm.spi.runtime.AbstractServerConfiguration;
import org.wildfly.swarm.swagger.webapp.SwaggerWebAppFraction;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * @author Lance Ball
 */
public class SwaggerWebAppConfiguration extends AbstractServerConfiguration<SwaggerWebAppFraction> {

    public SwaggerWebAppConfiguration() {
        super(SwaggerWebAppFraction.class);
    }

    @Override
    public List<Archive> getImplicitDeployments(SwaggerWebAppFraction fraction) throws Exception {
        ArtifactLookup lookup = ArtifactLookup.get();
        List<Archive> list = new ArrayList<>();
        try {
            // Get the swagger-ui bits as an Archive
            String gav = "org.wildfly.swarm:swagger-webapp-ui:war:" + SwaggerWebAppFraction.VERSION;

            WARArchive war = lookup.artifact(gav,
                    "swagger-webapp-ui.war")
                    .as(WARArchive.class)
                    .setContextRoot(fraction.getContext());

            // If any user content has been provided, merge that with the swagger-ui bits
            Archive<?> userContent = fraction.getWebContent();
            if (userContent != null) {
                war.merge(userContent);
            }
            //war.as(ZipExporter.class).exportTo(new File("swagger-webapp-ui.war"), true);
            list.add(war);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public SwaggerWebAppFraction defaultFraction() {
        return new SwaggerWebAppFraction();
    }
}
