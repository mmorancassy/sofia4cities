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
/*******************************************************************************
 * © Indra Sistemas, S.A.
 * 2013 - 2014  SPAIN
 * 
 * All rights reserved
 ******************************************************************************/
package com.indracompany.sofia2.api.rest.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


public class ApiSuscripcionDTO implements Cloneable, Serializable{

	@ApiModelProperty(value = "APISubscription apiIdentification")
	@Getter
	@Setter
	private String apiIdentification;
	
	@ApiModelProperty(value = "APISubscription userId")
	@Getter
	@Setter
	private String userId;
	
	@ApiModelProperty(value = "APISubscription initDate")
	@Getter
	@Setter
	private String initDate;
	
	@ApiModelProperty(value = "APISubscription endDate")
	@Getter
	@Setter
	private String endDate;
	
	@ApiModelProperty(value = "APISubscription active")
	@Getter
	@Setter
	private Boolean active;

	
	
}
