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

import com.indracompany.sofia2.config.model.Api;
import com.indracompany.sofia2.config.model.ApiOperation;

public interface ApiOperationRepository extends JpaRepository<ApiOperation, String> {

	ApiOperation findById(String id);
	
	List<ApiOperation> findByIdentificationIgnoreCase(String identification);

	List<ApiOperation> findByDescription(String description);

	List<ApiOperation> findByIdentification(String identification);

	List<ApiOperation> findByDescriptionContaining(String description);

	List<ApiOperation> findByIdentificationContaining(String identification);

	List<ApiOperation> findByIdentificationLikeAndDescriptionLike(String identification, String description);

	List<ApiOperation> findByIdentificationContainingAndDescriptionContaining(String identification,
			String description);

	List<ApiOperation> findByApiIdOrderByOperationDesc(String identification);

	List<ApiOperation> findByApiOrderByOperationDesc(Api api);

	List<ApiOperation> findAllByApi(Api api);

}