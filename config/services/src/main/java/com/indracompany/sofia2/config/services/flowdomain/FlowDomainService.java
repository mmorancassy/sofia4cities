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
package com.indracompany.sofia2.config.services.flowdomain;

import com.indracompany.sofia2.config.model.FlowDomain;
import com.indracompany.sofia2.config.model.User;

public interface FlowDomainService {

	public FlowDomain getFlowDomainByUser(String user);

	public FlowDomain getFlowDomainByIdentification(String identification);

	public void deleteFlowDomainFlows(String domainIdentification);

	public void deleteFlowdomain(String domainIdentification);

	public FlowDomain createFlowDomain(String identification, User user);

	public boolean flowDomainExists(FlowDomain domain);

	public void updateDomain(FlowDomain domain);

	public boolean domainExists(String domainIdentification);
}
