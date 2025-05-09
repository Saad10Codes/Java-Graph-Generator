/* 
 * Copyright 2012 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.vaadin.training.fundamentals.happening.ui;

import com.vaadin.Application;
import com.vaadin.ui.UriFragmentUtility;

/**
 * Application that provides additional services in addition to those of
 * {@link Application}
 * 
 * @author Johannes
 * 
 */
public interface ApplicationWithServices {
    /**
     * Gets the user data stored in a single cookie value
     * 
     * @return
     */
    String getCookie();

    /**
     * Clears the cookie value used for storing user data
     */
    void clearCookie();

    /**
     * Set the cookie value used for storing user data
     * 
     * @param data
     */
    void setCookie(String data);

    /**
     * Get the UriFragmentUtility added to application (or its main window to be
     * specific)
     * 
     * @return
     */
    UriFragmentUtility getUriFragmentUtility();
}
