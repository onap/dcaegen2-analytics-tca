/*
 * ===============================LICENSE_START======================================
 *  dcae-analytics
 * ================================================================================
 *    Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  ============================LICENSE_END===========================================
 */

package org.openecomp.dcae.apod.analytics.test.runner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.openecomp.dcae.apod.analytics.test.annotation.GuiceModules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * A custom Junit Runner which can be used to run Guice Test with custom Test Modules
 * <p>
 * @author Rajiv Singla . Creation Date: 10/20/2016.
 */
public class GuiceJUnitRunner extends BlockJUnit4ClassRunner {

    private static final Logger LOG = LoggerFactory.getLogger(GuiceJUnitRunner.class);

    private final Injector injector;

    public GuiceJUnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
        Class<?>[] classes = getModulesFor(klass);
        injector = createInjectorFor(classes);
    }


    /**
     * Returns a new fixture for running a test. Injects Guice members
     */
    @Override
    public Object createTest() throws Exception {
        Object obj = super.createTest();
        injector.injectMembers(obj);
        return obj;
    }


    /**
     * Creates new Guice Injector and registers Guice test modules
     *
     * @param classes test module classes
     * @return Guice injector with test modules
     * @throws InitializationError
     */
    private Injector createInjectorFor(Class<?>[] classes) throws InitializationError {
        Module[] modules = new Module[classes.length];
        for (int i = 0; i < classes.length; i++) {
            try {
                modules[i] = (Module) classes[i].newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new InitializationError(e);
            }
        }
        LOG.debug("Creating Junit Test Runner with Guice Test Modules: {}", Arrays.toString(modules));
        return Guice.createInjector(modules);
    }

    /**
     * Extract user provide test modules from the {@link GuiceModules} annotation
     *
     * @param klass Target class which is running the test
     * @return Guice modules contained passed in annotation of Guice Modules
     * @throws InitializationError
     */
    private Class<?>[] getModulesFor(Class<?> klass) throws InitializationError {
        GuiceModules annotation = klass.getAnnotation(GuiceModules.class);
        if (annotation == null) {
            throw new InitializationError(
                    "Missing @GuiceModules annotation for unit test '" + klass.getName()
                            + "'");
        }
        return annotation.value();
    }

}
