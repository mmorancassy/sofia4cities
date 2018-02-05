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
package com.indracompany.sofia2.services.configuration;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indracompany.sofia2.config.model.Configuration;
import com.indracompany.sofia2.config.model.ConfigurationType;
import com.indracompany.sofia2.config.repository.ConfigurationRepository;
import com.indracompany.sofia2.config.repository.ConfigurationTypeRepository;
import com.indracompany.sofia2.service.user.UserService;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
	@Autowired
	ConfigurationRepository configurationRepository;
	@Autowired
	ConfigurationTypeRepository configurationTypeRepository;
	@Autowired
	UserService userService;

	@Override
	public List<Configuration> getAllConfigurations() {

		return this.configurationRepository.findAll();

	}

	@Override
	@Transactional
	public void deleteConfiguration(String id) {
		this.configurationRepository.deleteById(id);
	}

	@Override
	public List<ConfigurationType> getAllConfigurationTypes() {
		List<ConfigurationType> types = this.configurationTypeRepository.findAll();
		return types;

	}

	@Override
	public Configuration getConfiguration(String id) {
		return this.configurationRepository.findById(id);
	}

	@Override
	public void createConfiguration(Configuration configuration) {
		Configuration oldConfiguration = this.configurationRepository.findById(configuration.getId());
		if (oldConfiguration == null) {
			oldConfiguration = new Configuration();
			oldConfiguration.setYmlConfig(configuration.getYmlConfig());
			oldConfiguration.setDescription(configuration.getDescription());
			oldConfiguration.setSuffix(configuration.getSuffix());
			oldConfiguration.setEnvironment(configuration.getEnvironment());
			this.configurationRepository.save(oldConfiguration);

		} else {
			throw new RuntimeException("You cann´t create a Configuration that exists:" + configuration.toString());
		}
	}

	@Override
	// FIXME: Check Exception
	public void updateConfiguration(Configuration configuration) {
		Configuration oldConfiguration = this.configurationRepository.findById(configuration.getId());
		if (oldConfiguration != null) {
			oldConfiguration.setYmlConfig(configuration.getYmlConfig());
			oldConfiguration.setDescription(configuration.getDescription());
			oldConfiguration.setSuffix(configuration.getSuffix());
			oldConfiguration.setEnvironment(configuration.getEnvironment());
			this.configurationRepository.save(oldConfiguration);

		} else {
			throw new RuntimeException("You cann´t update a Configuration:" + configuration.toString());
		}
	}

	@Override
	public boolean existsConfiguration(Configuration configuration) {
		if (this.configurationRepository.findById(configuration.getId()) == null)
			return false;
		else
			return true;
	}

	@Override
	public boolean isValidYML(final String yml) {
		try {
			// ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(yml);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
