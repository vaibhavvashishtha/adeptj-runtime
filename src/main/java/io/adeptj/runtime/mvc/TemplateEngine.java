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

package io.adeptj.runtime.mvc;

import io.adeptj.runtime.osgi.ServiceTrackers;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trimou.Mustache;

import java.lang.invoke.MethodHandles;

import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;

/**
 * TemplateEngine for rendering the HTML templates.
 *
 * @author Rakesh.Kumar, AdeptJ.
 */
public enum TemplateEngine {

    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TEMPLATE_ENGINE_INIT_MSG = "TemplateEngine initialized in [{}] ms!!";

    private static final int SB_CAPACITY = Integer.getInteger("template.builder.capacity", 100);

    private static final String RB_HELPER_NAME = "msg";

    private static final String KEY_RB_BASE_NAME = "resourceBundleBasename";

    private static final String KEY_START_DELIMITER = "startDelimiter";

    private static final String KEY_END_DELIMITER = "endDelimiter";

    private static final String KEY_CACHE_ENABLED = "cacheEnabled";

    private static final String KEY_CACHE_EXPIRATION = "cacheExpiration";

    private static final String KEY_TEMPLATE_LOCATOR_PRIORITY = "templateLocatorPriority";

    private static final String KEY_PREFIX = "prefix";

    private static final String KEY_SUFFIX = "suffix";

    //private final MustacheEngine mustacheEngine;

    TemplateEngine() {
        //long startTime = System.nanoTime();
        //Config config = Configs.of().trimou();
//        this.mustacheEngine = MustacheEngineBuilder.newBuilder()
//                .registerHelper(RB_HELPER_NAME, new ResourceBundleHelper(config.getString(KEY_RB_BASE_NAME), MESSAGE))
//                .addTemplateLocator(ClassPathTemplateLocator.builder()
//                        .setPriority(config.getInt(KEY_TEMPLATE_LOCATOR_PRIORITY))
//                        .setRootPath(config.getString(KEY_PREFIX))
//                        .setSuffix(config.getString(KEY_SUFFIX))
//                        .setScanClasspath(false)
//                        .build())
//                .setProperty(START_DELIMITER, config.getString(KEY_START_DELIMITER))
//                .setProperty(END_DELIMITER, config.getString(KEY_END_DELIMITER))
//                .setProperty(DEFAULT_FILE_ENCODING, UTF8)
//                .setProperty(TEMPLATE_CACHE_ENABLED, config.getBoolean(KEY_CACHE_ENABLED))
//                .setProperty(TEMPLATE_CACHE_EXPIRATION_TIMEOUT, config.getInt(KEY_CACHE_EXPIRATION))
//                .build();
        //this.mustacheEngine = ServiceTrackers.getInstance().getMustacheEngine();
        //LoggerFactory.getLogger(this.getClass()).info(TEMPLATE_ENGINE_INIT_MSG, Times.elapsedMillis(startTime));
    }

    /**
     * Renders the template contained by the TemplateContext.
     *
     * @param context the TemplateEngine context
     * @throws TemplateProcessingException the {@link TemplateProcessingException}
     */
    public void render(TemplateContext context) {
        try {
            Mustache mustache = ServiceTrackers.getInstance().getMustacheEngine().getMustache(context.getTemplate());
            StringBuilder output = new StringBuilder(SB_CAPACITY);
            mustache.render(output, context.getTemplateData());
            IOUtils.write(output.toString(), context.getResponse().getWriter());
        } catch (Exception ex) { // NOSONAR
            LOGGER.error(ex.getMessage(), ex);
            context.getRequest().setAttribute(ERROR_EXCEPTION, ex);
            throw new TemplateProcessingException(ex.getMessage(), ex);
        }
    }

    public static TemplateEngine getInstance() {
        return INSTANCE;
    }
}
