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
package com.indracompany.sofia2.config.services.datamodel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indracompany.sofia2.config.model.DataModel;
import com.indracompany.sofia2.config.repository.DataModelRepository;

@Service
public class DataModelServiceImpl implements DataModelService{
	
	@Autowired
	private DataModelRepository dataModelRepository;

	@Override
	public void deleteDataModel(String id) {
		dataModelRepository.delete(id);		
	}

	@Override
	public void createDataModel(DataModel dataModel) {
		dataModelRepository.save(dataModel);
	}

	@Override
	public List<DataModel> getAllDataModels() {
		return dataModelRepository.findAll();
	}

	@Override
	public List<DataModel> getDataModelsByCriteria(String id, String name, String description) {
		return dataModelRepository.findByIdOrNameOrDescription(id, name, description);
	}

	@Override
	public DataModel getDataModelById(String dataModelId) {
		return dataModelRepository.findById(dataModelId);
	}

}
