/**
 * Copyright Indra Sistemas, S.A.
 * 2013-2018 SPAIN
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indracompany.sofia2.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    Logger log = LoggerFactory.getLogger(CustomDaoAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails u = null;
        
        try {
            u = getUserDetailsService().loadUserByUsername(name);
        } catch (UsernameNotFoundException ex) {
            log.error("User '" + name + "' not found");
        } catch (Exception e) {
            log.error("Exception in CustomDaoAuthenticationProvider: " + e);
        }

        if (u != null) {
            if (u.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(u, password, u.getAuthorities());
            }
        }

        throw new BadCredentialsException(messages.getMessage("CustomDaoAuthenticationProvider.badCredentials", "Bad credentials"));

    }
}