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
package com.indracompany.sofia2.flowengine.nodered.communication;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indracompany.sofia2.commons.flow.engine.dto.FlowEngineDomain;
import com.indracompany.sofia2.commons.flow.engine.dto.FlowEngineDomainStatus;
import com.indracompany.sofia2.flowengine.exception.NodeRedAdminServiceException;
import com.indracompany.sofia2.flowengine.exception.NotSynchronizedToCdbException;
import com.indracompany.sofia2.flowengine.nodered.communication.dto.SynchronizeDomainStatusRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NodeRedAdminClientImpl implements NodeRedAdminClient {
	@Value("${sofia2.flowengine.admin.url}")
	private String flowengineUrl;
	// Services
	@Value("${sofia2.flowengine.services.request.timeout.ms:5000}")
	private int restRequestTimeout;
	@Value("${sofia2.flowengine.services.stop.admin}")
	private String stopflowEngine;
	@Value("${sofia2.flowengine.services.domain.status}")
	private String flowEngineDomainStatus;
	@Value("${sofia2.flowengine.services.domain.getall}")
	private String flowEngineDomainGetAll;
	@Value("${sofia2.flowengine.services.domain.get}")
	private String flowEngineDomainGet;
	@Value("${sofia2.flowengine.services.domain.create}")
	private String flowEngineDomainCreate;
	@Value("${sofia2.flowengine.services.domain.delete}")
	private String flowEngineDomainDelete;
	@Value("${sofia2.flowengine.services.domain.start}")
	private String flowEngineDomainStart;
	@Value("${sofia2.flowengine.services.domain.stop}")
	private String flowEngineDomainStop;
	@Value("${sofia2.flowengine.services.sync}")
	private String syncFlowEngineDomains;
	private HttpComponentsClientHttpRequestFactory httpRequestFactory;
	private ObjectMapper mapper;
	private boolean isSynchronizedWithBDC;

	@PostConstruct
	public void init() {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(restRequestTimeout);
		this.mapper = new ObjectMapper();
		resetSynchronizedWithBDC();
	}

	@Override
	public String stopFlowEngine() {
		String response = null;
		checkIsSynchronized();
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			response = restTemplate.postForObject(flowengineUrl + stopflowEngine, null, String.class);
		} catch (Exception e) {
			log.warn("Unable to stop the flow engine. Cause={}, message={}", e.getCause(), e.getMessage());
			throw new NodeRedAdminServiceException("Unable to stop the flow engine.");
		}
		return response;
	}

	@Override
	public void resetSynchronizedWithBDC() {
		this.isSynchronizedWithBDC = false;
	}

	@Override
	public void stopFlowEngineDomain(String domain) {
		checkIsSynchronized();
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			restTemplate.put(flowengineUrl + flowEngineDomainStop + "/" + domain, null);
		} catch (Exception e) {
			log.warn("Unable to stop the flow engine Domain={}. Cause={}, message={}", domain, e.getCause(),
					e.getMessage());
			throw new NodeRedAdminServiceException("Unable to stop the flow engine Domain = " + domain + ".");
		}
	}

	@Override
	public String startFlowEngineDomain(FlowEngineDomain domain) {
		String response = null;
		checkIsSynchronized();
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<FlowEngineDomain> domainToStart = new HttpEntity<FlowEngineDomain>(domain, headers);
			response = restTemplate.postForObject(flowengineUrl + flowEngineDomainStart, domainToStart, String.class);
		} catch (Exception e) {
			log.warn("Unable to start Domain={}. Cause={}, message={}", domain, e.getCause(), e.getMessage());
			throw new NodeRedAdminServiceException("Unable to start Domain = " + domain + ".");
		}
		return response;
	}

	@Override
	public String createFlowengineDomain(FlowEngineDomain domain) {
		String response = null;
		checkIsSynchronized();
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<FlowEngineDomain> newDomain = new HttpEntity<FlowEngineDomain>(domain, headers);
			response = restTemplate.postForObject(flowengineUrl + flowEngineDomainCreate, newDomain, String.class);
		} catch (Exception e) {
			log.warn("Unable to create Domain={}. Cause={}, message={}", domain, e.getCause(), e.getMessage());
			throw new NodeRedAdminServiceException("Unable to create Domain = " + domain + ".");
		}
		return response;
	}

	@Override
	public void deleteFlowEngineDomain(String domainId) {
		checkIsSynchronized();
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			restTemplate.delete(flowengineUrl + flowEngineDomainDelete + "/" + domainId);
		} catch (Exception e) {
			log.warn("Unable to Delete Domain={}. Cause={}, message={}", domainId, e.getCause(), e.getMessage());
			throw new NodeRedAdminServiceException("Unable to delete Domain = " + domainId + ".");
		}
	}

	@Override
	public FlowEngineDomain getFlowEngineDomain(String domainId) {
		FlowEngineDomain response = null;
		checkIsSynchronized();
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(flowengineUrl + flowEngineDomainGet)
					.queryParam("domain", domainId);
			String responseRest = restTemplate.getForObject(builder.build().encode().toUri(), String.class);
			response = mapper.readValue(responseRest, new TypeReference<FlowEngineDomain>() {
			});
		} catch (Exception e) {
			log.warn("Unable to retrieve Domain={}. Cause={}, message={}", domainId, e.getCause(), e.getMessage());
			throw new NodeRedAdminServiceException("Unable to retrieve Domain " + domainId + ".");
		}
		return response;
	}

	@Override
	public List<FlowEngineDomainStatus> getAllFlowEnginesDomains() {
		List<FlowEngineDomainStatus> domainStatus = new ArrayList<>();
		checkIsSynchronized();
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			String responseRest = restTemplate.getForObject(flowengineUrl + flowEngineDomainGetAll, String.class);
			domainStatus = (List<FlowEngineDomainStatus>) FlowEngineDomainStatus
					.fromJsonArrayToDomainStatus(responseRest);
		} catch (Exception e) {
			log.warn("Unable to retrieve all flow engine domains. Cause={}, message={}", e.getCause(), e.getMessage());
			throw new NodeRedAdminServiceException("Unable to retrieve all flow engine domains.");
		}
		return domainStatus;
	}

	@Override
	public List<FlowEngineDomainStatus> getFlowEngineDomainStatus(List<String> domainList) {
		List<FlowEngineDomainStatus> response = null;
		checkIsSynchronized();
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(flowengineUrl + flowEngineDomainStatus)
					.queryParam("domains", mapper.writeValueAsString(domainList));

			String responseRest = restTemplate.getForObject(builder.build().encode().toUri(), String.class);
			response = (List<FlowEngineDomainStatus>) FlowEngineDomainStatus.fromJsonArrayToDomainStatus(responseRest);
		} catch (Exception e) {
			log.error("Error retrieving domain's statuses from NodeRedAdminClient. Cause = {}, message = {}",
					e.getCause(), e.getMessage());
			throw new NodeRedAdminServiceException("Unable to retrieve domain's statuses from NodeRedAdminClient.");
		}
		return response;
	}

	@Override
	public String synchronizeMF(List<FlowEngineDomainStatus> domainList) {
		String response = null;
		SynchronizeDomainStatusRequest synchronizeDomainStatusRequest = new SynchronizeDomainStatusRequest();
		synchronizeDomainStatusRequest.setListDomain(domainList);
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> domainsToSync = new HttpEntity<String>(synchronizeDomainStatusRequest.toJson(), headers);
			response = restTemplate.postForObject(flowengineUrl + syncFlowEngineDomains, domainsToSync, String.class);
			this.isSynchronizedWithBDC = true;
		} catch (Exception e) {
			log.warn("Unable to synchronize domains with CDB. Cause={}, message={}", e.getCause(), e.getMessage());
			throw new NodeRedAdminServiceException("Unable to synchronize domains with CDB.");
		}
		return response;
	}

	private void checkIsSynchronized() {
		if (!this.isSynchronizedWithBDC) {
			log.warn("NodeRed AdminClient is not synchronized with CDB data.");
			throw new NotSynchronizedToCdbException("NodeRed AdminClient is not synchronized with CDB data.");
		}
	}
}