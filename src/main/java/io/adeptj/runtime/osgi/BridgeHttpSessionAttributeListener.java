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

package io.adeptj.runtime.osgi;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import static io.adeptj.runtime.osgi.HttpSessionEvents.SESSION_ATTRIBUTE_ADDED;
import static io.adeptj.runtime.osgi.HttpSessionEvents.SESSION_ATTRIBUTE_REMOVED;
import static io.adeptj.runtime.osgi.HttpSessionEvents.SESSION_ATTRIBUTE_REPLACED;

/**
 * An {@link HttpSessionAttributeListener} which propagates the {@link javax.servlet.http.HttpSession} attribute
 * events like add, remove and replace to OSGi registered {@link HttpSessionAttributeListener} services.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
public class BridgeHttpSessionAttributeListener implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        HttpSessionEvents.handleHttpSessionBindingEvent(SESSION_ATTRIBUTE_ADDED, event);
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        HttpSessionEvents.handleHttpSessionBindingEvent(SESSION_ATTRIBUTE_REMOVED, event);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        HttpSessionEvents.handleHttpSessionBindingEvent(SESSION_ATTRIBUTE_REPLACED, event);
    }
}
