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

import com.adeptj.runtime.tools.OSGiConsolePasswordVault;
import io.adeptj.runtime.config.Configs;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * CredentialMatcher, Logic for creating password hash and comparing submitted credential is same as implemented
 * in [org.apache.felix.webconsole.internal.servlet.Password]
 * <p>
 * Because, we want to be consistent with the hashing mechanism used by OSGi Web Console configuration management,
 * but supporting classes available there are package private and therefore can't be accessible to outside world.
 *
 * @author Rakesh.Kumar, AdeptJ
 */
final class CredentialMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialMatcher.class);

    private static final String SHA256 = "SHA-256";

    private static final String CURLY_BRACE_OPEN = "{";

    private static final String CURLY_BRACE_CLOSE = "}";

    private static final String PREFIX = CURLY_BRACE_OPEN + SHA256.toLowerCase() + CURLY_BRACE_CLOSE;

    private static final String KEY_USER_CREDENTIAL_MAPPING = "common.user-credential-mapping";

    private CredentialMatcher() {
    }

    static boolean match(String username, char[] password) {
        // When OsgiManager.config file is non-existent as configuration was never saved from OSGi console,
        // make use of default password maintained in provisioning file.
        return OSGiConsolePasswordVault.INSTANCE.isPasswordSet() ?
                fromOSGiManagerConfig(password) :
                fromServerConfig(username, password);
    }

    private static boolean fromServerConfig(String id, char[] password) {
        return Configs.of().undertow()
                .getObject(KEY_USER_CREDENTIAL_MAPPING)
                .unwrapped()
                .entrySet()
                .stream()
                .anyMatch(entry -> StringUtils.equals(entry.getKey(), id)
                        && Arrays.equals(makeHash(password), ((String) entry.getValue()).toCharArray()));
    }

    private static boolean fromOSGiManagerConfig(char[] password) {
        try {
            return Arrays.equals(makeHash(password), OSGiConsolePasswordVault.INSTANCE.getPassword());
        } catch (Exception ex) { // NOSONAR
            LOGGER.error(ex.getMessage(), ex);
        }
        return false;
    }

    private static char[] makeHash(char[] password) {
        if (ArrayUtils.isEmpty(password)) {
            return null;
        }
        try {
            byte[] digest = MessageDigest.getInstance(SHA256).digest(new String(password).getBytes(UTF_8));
            byte[] encoded = Base64.getEncoder().encode(digest);
            return (PREFIX + new String(encoded, UTF_8)).toCharArray();
        } catch (Exception ex) { // NOSONAR
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }
}
