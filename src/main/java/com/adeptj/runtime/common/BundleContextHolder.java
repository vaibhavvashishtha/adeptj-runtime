/*
###############################################################################
#                                                                             # 
#    Copyright 2016, AdeptJ (http://adeptj.com)                               #
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
package com.adeptj.runtime.common;

import org.osgi.framework.BundleContext;

/**
 * This Enum provides the access to the OSGi System Bundle {@link BundleContext}.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
public enum BundleContextHolder {

    INSTANCE;

    private BundleContext bundleContext;
    
    private volatile boolean bundleContextAvailable;

    public BundleContext getBundleContext() {
        return this.bundleContext;
    }

    public boolean isBundleContextAvailable() {
        return this.bundleContextAvailable;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        this.bundleContextAvailable = true;
    }
}
