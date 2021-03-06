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

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.util.Map;

/**
 * Sets the given headers in the response header map on each request, then call the next handler.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
final class SetHeadersHandler implements HttpHandler {

    private final HttpHandler servletHandler;

    private Map<HttpString, String> headers;

    SetHeadersHandler(HttpHandler servletHandler, Map<HttpString, String> headers) {
        this.servletHandler = servletHandler;
        this.headers = headers;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        this.headers.forEach((name, value) -> exchange.getResponseHeaders().put(name, value));
        this.servletHandler.handleRequest(exchange);
    }

}
