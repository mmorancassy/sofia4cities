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
package com.indracompany.sofia2.iotbroker.processor.impl;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.indracompany.sofia2.iotbroker.common.MessageException;
import com.indracompany.sofia2.iotbroker.common.exception.AuthorizationException;
import com.indracompany.sofia2.iotbroker.common.exception.BaseException;
import com.indracompany.sofia2.iotbroker.common.exception.OntologySchemaException;
import com.indracompany.sofia2.iotbroker.common.exception.SSAPProcessorException;
import com.indracompany.sofia2.iotbroker.plugable.impl.security.SecurityPluginManager;
import com.indracompany.sofia2.iotbroker.plugable.interfaces.security.IoTSession;
import com.indracompany.sofia2.iotbroker.processor.MessageTypeProcessor;
import com.indracompany.sofia2.persistence.ContextData;
import com.indracompany.sofia2.router.service.app.model.NotificationModel;
import com.indracompany.sofia2.router.service.app.model.OperationModel;
import com.indracompany.sofia2.router.service.app.model.OperationModel.OperationType;
import com.indracompany.sofia2.router.service.app.model.OperationModel.QueryType;
import com.indracompany.sofia2.router.service.app.model.OperationResultModel;
import com.indracompany.sofia2.router.service.app.service.RouterService;
import com.indracompany.sofia2.ssap.SSAPMessage;
import com.indracompany.sofia2.ssap.body.SSAPBodyInsertMessage;
import com.indracompany.sofia2.ssap.body.SSAPBodyReturnMessage;
import com.indracompany.sofia2.ssap.body.parent.SSAPBodyMessage;
import com.indracompany.sofia2.ssap.enums.SSAPMessageDirection;
import com.indracompany.sofia2.ssap.enums.SSAPMessageTypes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InsertProcessor implements MessageTypeProcessor {

	@Autowired
	private RouterService routerService;

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	SecurityPluginManager securityPluginManager;

	@Override
	public SSAPMessage<SSAPBodyReturnMessage> process(SSAPMessage<? extends SSAPBodyMessage> message)
			throws BaseException {
		@SuppressWarnings("unchecked")
		final
		SSAPMessage<SSAPBodyInsertMessage> insertMessage = (SSAPMessage<SSAPBodyInsertMessage>) message;
		final SSAPMessage<SSAPBodyReturnMessage> responseMessage = new SSAPMessage<>();

		// TODO: Client Connection in contextData
		final ContextData contextData = new ContextData();
		final Optional<IoTSession> session = securityPluginManager.getSession(insertMessage.getSessionKey());
		contextData.setClientConnection("");

		session.ifPresent(s -> {
			contextData.setClientPatform(s.getClientPlatform());
			contextData.setClientPatformInstance(s.getClientPlatformInstance());
			contextData.setUser(s.getUserID());
		});

		contextData.setClientSession(insertMessage.getSessionKey());
		contextData.setTimezoneId(ZoneId.systemDefault().toString());

		((ObjectNode) insertMessage.getBody().getData()).set("contextData", objectMapper.valueToTree(contextData));

		final OperationModel model = new OperationModel();

		model.setBody(insertMessage.getBody().getData().toString());
		model.setOntologyName(insertMessage.getBody().getOntology());
		model.setOperationType(OperationType.POST);
		model.setQueryType(QueryType.NATIVE);
		session.ifPresent(s -> model.setUser(s.getUserID()));
		session.ifPresent(s -> model.setClientPlatformId(s.getClientPlatform()));

		final NotificationModel modelNotification= new NotificationModel();
		modelNotification.setOperationModel(model);

		String repositoryResponse = "";
		try {
			final OperationResultModel result = routerService.insert(modelNotification);
			repositoryResponse = result.getResult();

			responseMessage.setDirection(SSAPMessageDirection.RESPONSE);
			responseMessage.setMessageId(insertMessage.getMessageId());
			responseMessage.setMessageType(insertMessage.getMessageType());
			//			responseMessage.setOntology(insertMessage.getOntology());
			responseMessage.setSessionKey(insertMessage.getSessionKey());
			responseMessage.setBody(new SSAPBodyReturnMessage());
			responseMessage.getBody().setOk(true);

			responseMessage.getBody().setData(objectMapper.readTree("{\"id\":\""+repositoryResponse+"\"}"));

		} catch (final Exception e1) {
			log.error("Error processing Insert", e1);
			throw new SSAPProcessorException("Response from repository on insert is not JSON compliant");
		}

		return responseMessage;
	}

	@Override
	public List<SSAPMessageTypes> getMessageTypes() {
		return Collections.singletonList(SSAPMessageTypes.INSERT);
	}

	@Override
	public boolean validateMessage(SSAPMessage<? extends SSAPBodyMessage> message)
			throws AuthorizationException, OntologySchemaException, SSAPProcessorException {

		final SSAPMessage<SSAPBodyInsertMessage> insertMessage = (SSAPMessage<SSAPBodyInsertMessage>) message;

		if(insertMessage.getBody().getData() == null || insertMessage.getBody().getData().isNull() ) {
			throw new SSAPProcessorException(String.format(MessageException.ERR_FIELD_IS_MANDATORY, "data", insertMessage.getMessageType().name()));
		}
		return true;
	}
}
