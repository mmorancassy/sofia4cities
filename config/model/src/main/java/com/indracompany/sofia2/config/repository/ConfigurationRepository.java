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
package com.indracompany.sofia2.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.indracompany.sofia2.config.model.Configuration;
import com.indracompany.sofia2.config.model.ConfigurationType;
import com.indracompany.sofia2.config.model.User;

public interface ConfigurationRepository extends JpaRepository<Configuration, String> {

	List<Configuration> findByUser(User user);

	Configuration findById(String id);
	
	Configuration findByDescription(String description);

	List<Configuration> findByConfigurationType(ConfigurationType configurationTypeId);
	
	List<Configuration> findByConfigurationTypeAndUser(ConfigurationType configurationTypeId, User user);

	Configuration findByConfigurationTypeAndEnvironmentAndSuffix(ConfigurationType configurationType,
			String environment, String suffix);

	List<Configuration> findByUserAndConfigurationType(User userId, ConfigurationType configurationType);

	void deleteById(String id);

}
