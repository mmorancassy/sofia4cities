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
import org.springframework.data.jpa.repository.Query;

import com.indracompany.sofia2.config.model.FlowDomain;

public interface FlowDomainRepository extends JpaRepository<FlowDomain, String> {

	FlowDomain findByIdentification(String identification);

	FlowDomain findByUser_userId(String userId);

	@Query("SELECT d.port FROM FlowDomain as d")
	List<Integer> findAllDomainPorts();

	@Query("SELECT d.servicePort FROM FlowDomain as d")
	List<Integer> findAllServicePorts();

	void deleteByIdentification(String identification);
}
