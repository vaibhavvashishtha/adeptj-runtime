/*
###############################################################################
#                                                                             #
#    Copyright 2016, AdeptJ (http://www.adeptj.com)                           #
#                                                                             #
#    Licensed under the Apache License, Version 2.0 (the "License");          #
#    you may not use this file except in compliance with the License.         #
#    You may obtain a copy of the License at                                  #
#                                                                             #
#        http://www.apache.org/licenses/LICENSE-2.0                           #
#                                                                             #
#    Unless required by applicable law or agreed to in writing, software      #
#    distributed under the License is distributed on an "AS IS" BASIS,        #
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. #
#    See the License for the specific language governing permissions and      #
#    limitations under the License.                                           #
#                                                                             #
###############################################################################
*/

package io.adeptj.runtime.server;

import io.adeptj.runtime.common.BundleContextHolder;
import io.adeptj.runtime.common.Servlets;
import io.adeptj.runtime.common.StartupOrder;
import io.adeptj.runtime.core.StartupAware;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

/**
 * DefaultStartupAware is a {@link io.adeptj.runtime.core.StartupAware} that registers the bridge listeners and servlet.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
@StartupOrder(1)
public class DefaultStartupAware implements StartupAware {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStartup(ServletContext servletContext) {
        Logger logger = LoggerFactory.getLogger(DefaultStartupAware.class);
        Servlets.registerBridgeListeners(servletContext);
        logger.info("OSGi bridge listeners registered successfully!!");
        // Set the System Bundle's BundleContext as a ServletContext attribute per the Felix HttpBridge Specification.
        servletContext.setAttribute(BundleContext.class.getName(), BundleContextHolder.getInstance().getBundleContext());
        Servlets.registerBridgeServlet(servletContext);
        logger.info("BridgeServlet registered successfully!!");
    }
}
