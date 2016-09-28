/* 
 * =============================================================================
 * 
 * Copyright (c) 2016 AdeptJ
 * Copyright (c) 2016 Rakesh Kumar <irakeshk@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * =============================================================================
*/
package com.adeptj.modularweb.micro.bootstrap;

import static com.adeptj.modularweb.micro.bootstrap.FrameworkConstants.BUNDLES_ROOT_DIR_KEY;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BundleProvisioner that handles the installation/activation of required bundles after the system bundle is up and running.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
public enum BundleProvisioner {
	
	INSTANCE;
	
	private static final String BUNDLES_JAR_DIR = "bundles" + File.separator;
	
	private static final String HEADER_FRAGMENT_HOST = "Fragment-Host";

	private static final String EXTN_JAR = ".jar";

	private static final Logger LOGGER = LoggerFactory.getLogger(BundleProvisioner.class);

	public void provisionBundles(BundleContext systemBundleContext) throws Exception {
		Set<Bundle> installedBundles = this.installBundles(systemBundleContext);
		LOGGER.info("Total:[{}] Bundles(excluding system bundle) installed!!", installedBundles.size());
		// Now start all the installed Bundles.
		this.startBundles(installedBundles);
	}

	private void startBundles(Set<Bundle> installedBundles) {
		// Fragment Bundles can't be started so put a check for [Fragment-Host] header.
		BundleStartConsumer consumer = (Bundle bundle) -> {
			bundle.start();
		};
		installedBundles.stream().filter(bundle -> bundle.getHeaders().get(HEADER_FRAGMENT_HOST) == null)
				.forEach(consumer);
	}

	private Set<Bundle> installBundles(BundleContext systemBundleContext) throws Exception {
		BundleInstallFunction installFunc = (url) -> {
			return systemBundleContext.installBundle(url.toExternalForm());
		};
		// First install all the Bundles.
		return this.collectBundles().stream().map(installFunc).filter(Objects::nonNull).collect(Collectors.toSet());
	}

	private List<URL> collectBundles() throws IOException {
		String rootPath = ServletContextAware.INSTANCE.getServletContext().getInitParameter(BUNDLES_ROOT_DIR_KEY);
		JarURLConnection conn = (JarURLConnection) BundleProvisioner.class.getResource(rootPath).openConnection();
		ClassLoader classLoader = BundleProvisioner.class.getClassLoader();
		List<URL> bundles = conn.getJarFile().stream()
				.filter(entry -> entry.getName().startsWith(BUNDLES_JAR_DIR) && entry.getName().endsWith(EXTN_JAR))
				.map(entry -> classLoader.getResource(entry.getName())).collect(Collectors.toList());
		LOGGER.info("Total:[{}] Bundles(excluding system bundle) collected from location:[{}]", bundles.size(), conn);
		return bundles;
	}
}
