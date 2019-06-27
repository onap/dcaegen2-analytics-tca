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

package org.onap.dcae.apod.analytics.test;

import org.json.JSONException;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;

import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.exists;

/**
 * Base common test class for all DCAE Analytics Test e.g. unit tests, integration test, CDAP tests etc.
 * <p>
 * @author Rajiv Singla . Creation Date: 10/19/2016.
 */
abstract class BaseDCAEAnalyticsCommonTest {
protected static final Logger LOG = LoggerFactory.getLogger(BaseDCAEAnalyticsCommonTest.class);
protected BaseDCAEAnalyticsCommonTest() {
}
    /**
     * Asserts if expected Json String and actual Json String contain the same properties ignoring
     * property order. Simple String assertion might fail as property order during serialization and deserialization
     * is generally non-deterministic. Also proper error message are generated more missing or unexpected
     * properties
     *
     * @param expectedJsonString expected Json String
     * @param actualJsonString actual Json String
     * @throws JSONException Json Exception
     */
    public static void assertJson(String expectedJsonString, String actualJsonString) throws JSONException {
        JSONAssert.assertEquals(expectedJsonString, actualJsonString, true);
    }

    /**
     * Converts given file location to String
     *
     * @param fileLocation location of the file which needs to be converted to String
     * @return Contents of file as string
     * @throws IOException IOException
     */
    public static String fromStream(String fileLocation) throws IOException {
        final InputStream jsonFileInputStream =
                BaseDCAEAnalyticsCommonTest.class.getClassLoader().getResourceAsStream(fileLocation);
        Assert.assertNotNull("Json File Location must be valid", jsonFileInputStream);
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(jsonFileInputStream, Charset.forName("UTF-8")))) {
            final StringBuilder result = new StringBuilder();
            final String newLine = System.getProperty("line.separator");
            String line = reader.readLine();
            while (line != null) {
                result.append(line)
                    .append(newLine);
                line = reader.readLine();
            }
            jsonFileInputStream.close();
            return result.toString();
        }
    }


    /**
     * Checks if object can be serialized properly
     *
     * @param object input object
     * @param callingClass calling class
     * @throws IOException IOException
     */
    public static void testSerialization(Object object, Class<?> callingClass) throws IOException {
        final URL location = callingClass.getProtectionDomain().getCodeSource().getLocation();
        final File serializedOutputFile =
                new File(location.getPath() + String.format("serialization/%s.ser", object.getClass().getSimpleName()));

        // Maybe file already try deleting it first
        final boolean deleteIfExists = deleteIfExists(Paths.get(serializedOutputFile.getPath()));

        if (deleteIfExists) {
            LOG.warn("Previous serialization file was overwritten at location: {}", serializedOutputFile.getPath());
        }

        boolean mkdirs = true;
        if (!exists(Paths.get(serializedOutputFile.getParentFile().getPath()))) {
            mkdirs = serializedOutputFile.getParentFile().mkdirs();
        }
        if (mkdirs) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(serializedOutputFile);
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(object);
                LOG.debug("Successfully created serialization file at location: {}", serializedOutputFile.getPath());
            }
        } else {
            throw new IllegalStateException(
                    String.format("Failed to create location to store serialization file: %s",
                            serializedOutputFile));
        }
    }

    /**
     * Writes Text to Output file
     *
     * @param textFileLocation - location of text file e.g. textfiles/fileName.json
     * @param content           - file content
     * @param callingClass      - calling class
     * @throws IOException      - ioException
     */
    public static void writeToOutputTextFile(String textFileLocation, String content, Class<?> callingClass) throws
            IOException {
        final URL location = callingClass.getProtectionDomain().getCodeSource().getLocation();
        final File fileLocation = new File(location.getPath() + textFileLocation);

        // Maybe file already try deleting it first
        final boolean deleteIfExists = deleteIfExists(Paths.get(fileLocation.getPath()));

        if (deleteIfExists) {
            LOG.warn("Previous file will be overwritten at location: {}", fileLocation.getPath());
        }

        boolean mkdirs = true;
        if (!exists(Paths.get(fileLocation.getParentFile().getPath()))) {
            mkdirs = fileLocation.getParentFile().mkdirs();
        }
        if (mkdirs) {
            try (
                    FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
                    OutputStreamWriter outputStream =
                            new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8"))) {
                outputStream.write(content);
                LOG.debug("Successfully created text file at location: {}", fileLocation.getPath());
            }
        } else {
            throw new IllegalStateException(
                    String.format("Failed to create location to store text file: %s", fileLocation));
        }

    }


    /**
     * For testing purposes only we may sometime we may want to access private fields of underlying
     * object to confirm the values are setup correctly.
     * <p>
     * This method uses java reflection to get the value to private object in the class
     *
     * @param object            Actual object which has the private field you want to check
     * @param fieldName         Field name in the Actual Object you want to get the value of
     * @param privateFieldClass Type of the private field
     * @param <T>               Class of Actual Object
     * @param <U>               Class of private field
     * @return value of the private field
     */
    public static <T, U> U getPrivateFiledValue(T object, String fieldName, Class<U> privateFieldClass) {

        final Class<?> objectClass = object.getClass();
        try {
            final Field privateField = objectClass.getDeclaredField(fieldName);
            try {

                // mark private field to be accessible for testing purposes
                AccessController.doPrivileged(new PrivilegedAction() {
                    @Override
                    public Object run() {
                        privateField.setAccessible(true);
                        return null;
                    }
                });


                return privateFieldClass.cast(privateField.get(object));

            } catch (IllegalAccessException e) {
                LOG.error("Unable to access field: {}", fieldName);
                throw new IllegalStateException(e);
            }
        } catch (NoSuchFieldException e) {
            LOG.error("Unable to locate field name: {} in class: {}", fieldName, objectClass.getSimpleName());
            throw new IllegalStateException(e);
        }


    }


    /**
     * Prints classpath jars which are visible inside the class
     *
     * @param classLoader classloader of the calling class
     */
    public static void dumpClasspath(ClassLoader classLoader) {

        LOG.info("Dumping ClassPath for classloader: {}", classLoader);

        if (classLoader instanceof URLClassLoader) {

            URLClassLoader ucl = (URLClassLoader) classLoader;
            LOG.info("\t ==========>>>" + Arrays.toString(ucl.getURLs()));

        } else {
            LOG.info("\t(cannot display components as not a URLClassLoader)");
        }

        if (classLoader.getParent() != null) {
            dumpClasspath(classLoader.getParent());
        }
    }

}
