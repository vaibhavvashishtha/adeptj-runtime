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
package com.adeptj.runtime.undertow;

import io.undertow.security.idm.Account;

import java.security.Principal;
import java.util.Set;

/**
 * SimpleAccount.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
public class SimpleAccount implements Account {

    private static final long serialVersionUID = -2090504892837494810L;

    private SimplePrincipal principal;

    private Set<String> roles;

    public SimpleAccount(SimplePrincipal principal, Set<String> roles) {
        this.principal = principal;
        this.roles = roles;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

}
