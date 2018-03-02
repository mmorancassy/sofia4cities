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
package com.indracompany.sofia2.router.service.app.service.crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indracompany.sofia2.config.model.ApiOperation;
import com.indracompany.sofia2.persistence.mongodb.MongoBasicOpsDBRepository;
import com.indracompany.sofia2.persistence.services.QueryToolService;
import com.indracompany.sofia2.router.service.app.model.OperationModel;
import com.indracompany.sofia2.router.service.app.model.OperationResultModel;
import com.indracompany.sofia2.router.service.app.service.RouterCrudService;

@Service
public class RouterCrudServiceImpl implements RouterCrudService {
	
	@Autowired
	private QueryToolService queryToolService;
	
	@Autowired
	private MongoBasicOpsDBRepository mongoBasicOpsDBRepository;
	
	static final String ONT_NAME = "contextData";
	static final String DATABASE = "sofia2_s4c";

	@Override
	public OperationResultModel insert(OperationModel operationModel) throws Exception {
		
		OperationResultModel result = new OperationResultModel();
		
		String METHOD = operationModel.getOperationType();
		String BODY = operationModel.getBody();
		String QUERY = operationModel.getQuery();
		
		String QUERY_TYPE = operationModel.getQueryType();
		String ontologyId = operationModel.getOntologyId();
		String ontologyName = operationModel.getOntologyName();

		String OBJECT_ID = operationModel.getObjectId();
		
		String OUTPUT="";
		
		if (METHOD.equalsIgnoreCase(ApiOperation.Type.POST.name()) || METHOD.equalsIgnoreCase(OperationModel.Operations.INSERT.name())) {
			OUTPUT = mongoBasicOpsDBRepository.insert(ontologyName, BODY);	
		}

		result.setResult(OUTPUT);
		result.setMessage("OK");
		return result;
		

	}

	@Override
	public OperationResultModel update(OperationModel operationModel) throws Exception {
	OperationResultModel result = new OperationResultModel();
		
		String METHOD = operationModel.getOperationType();
		String BODY = operationModel.getBody();
		String QUERY = operationModel.getQuery();
		
		String QUERY_TYPE = operationModel.getQueryType();
		
		String ontologyName = operationModel.getOntologyName();

		String OBJECT_ID = operationModel.getObjectId();
		
		String OUTPUT="";
		
		if (METHOD.equalsIgnoreCase(ApiOperation.Type.PUT.name()) || METHOD.equalsIgnoreCase(OperationModel.Operations.UPDATE.name())) {
			
			if (OBJECT_ID!=null && OBJECT_ID.length()>0) {
				mongoBasicOpsDBRepository.updateNativeByObjectIdAndBodyData(ontologyName, OBJECT_ID, BODY);	
				OUTPUT = mongoBasicOpsDBRepository.findById(ontologyName, OBJECT_ID);	
			}
			
			else {
				OUTPUT = ""+mongoBasicOpsDBRepository.updateNative(ontologyName, BODY);	
			}
	
		}

		result.setResult(OUTPUT);
		result.setMessage("OK");
		return result;
	}

	@Override
	public OperationResultModel delete(OperationModel operationModel) throws Exception {
		OperationResultModel result = new OperationResultModel();
		
		String METHOD = operationModel.getOperationType();
		String BODY = operationModel.getBody();
		String QUERY = operationModel.getQuery();
		
		String QUERY_TYPE = operationModel.getQueryType();
		
		String ontologyName = operationModel.getOntologyName();

		String OBJECT_ID = operationModel.getObjectId();
		
		String OUTPUT="";
		
		if (METHOD.equalsIgnoreCase(ApiOperation.Type.DELETE.name()) || METHOD.equalsIgnoreCase(OperationModel.Operations.DELETE.name())) {
			
			if (OBJECT_ID!=null && OBJECT_ID.length()>0) {
				OUTPUT = ""+ mongoBasicOpsDBRepository.deleteNativeById(ontologyName, OBJECT_ID);
			}
			
			else {
				OUTPUT = ""+ mongoBasicOpsDBRepository.deleteNative(ontologyName, BODY);	
			}
			
		}

		result.setResult(OUTPUT);
		result.setMessage("OK");
		return result;
	}

	@Override
	public OperationResultModel query(OperationModel operationModel) throws Exception {
		OperationResultModel result = new OperationResultModel();
		
		String METHOD = operationModel.getOperationType();
		String BODY = operationModel.getBody();
		String QUERY = operationModel.getQuery();
		
		String QUERY_TYPE = operationModel.getQueryType();
		String ontologyId = operationModel.getOntologyId();
		String ontologyName = operationModel.getOntologyName();

		String OBJECT_ID = operationModel.getObjectId();
		
		String OUTPUT="";
		
		if (METHOD.equalsIgnoreCase(ApiOperation.Type.GET.name()) || METHOD.equalsIgnoreCase(OperationModel.Operations.QUERY.name())) {
			
			if (QUERY_TYPE !=null)
			{
				if (QUERY_TYPE.equalsIgnoreCase("SQLLIKE")) {
					OUTPUT = queryToolService.querySQLAsJson(ontologyName, QUERY, 0);
				}
				else if (QUERY_TYPE.equalsIgnoreCase("NATIVE")) {
					OUTPUT = queryToolService.queryNativeAsJson(ontologyName, QUERY, 0,0);
				}
				else {
					OUTPUT = mongoBasicOpsDBRepository.findById(ontologyName, OBJECT_ID);
				}
			}
		}
		result.setResult(OUTPUT);
		result.setMessage("OK");
		return result;
	}

	@Override
	public OperationResultModel subscribe(OperationModel operationModel) throws Exception {
		
		OperationResultModel result = new OperationResultModel();
		
		
		result.setMessage("OK");
		return result;
	}
	

	public QueryToolService getQueryToolService() {
		return queryToolService;
	}

	public void setQueryToolService(QueryToolService queryToolService) {
		this.queryToolService = queryToolService;
	}

	public MongoBasicOpsDBRepository getMongoBasicOpsDBRepository() {
		return mongoBasicOpsDBRepository;
	}

	public void setMongoBasicOpsDBRepository(MongoBasicOpsDBRepository mongoBasicOpsDBRepository) {
		this.mongoBasicOpsDBRepository = mongoBasicOpsDBRepository;
	}
	
	

}
