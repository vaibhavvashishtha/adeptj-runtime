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

package io.adeptj.runtime.common;

import java.util.function.Consumer;

/**
 * Handles exceptions in lambda expressions wherever a {@link Consumer} is needed..
 *
 * @author Rakesh.Kumar, AdeptJ
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T> {

    @Override
    default void accept(final T e) {
        try {
            acceptThatThrowsException(e);
        } catch (Exception ex) { // NOSONAR
            throw new RuntimeException(ex); // NOSONAR
        }
    }

    void acceptThatThrowsException(T e) throws Exception; // NOSONAR
}
