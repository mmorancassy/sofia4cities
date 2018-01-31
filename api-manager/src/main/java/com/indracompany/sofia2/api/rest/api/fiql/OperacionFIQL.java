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
package com.indracompany.sofia2.api.rest.api.fiql;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.indracompany.sofia2.api.rest.api.dto.OperacionDTO;
import com.indracompany.sofia2.config.model.ApiOperation;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public final class OperacionFIQL {
	
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String PUT = "PUT";
	private static final String DELETE = "DELETE";
	

	
	static Locale locale = LocaleContextHolder.getLocale();
	
	private OperacionFIQL() {
		throw new AssertionError("Instantiating utility class...");
	}

	public static List<OperacionDTO> toOperacionDTO(List<ApiOperation> operaciones) {
		List<OperacionDTO> operacionesDTO = new ArrayList<OperacionDTO>();
		for (ApiOperation operacion : operaciones) {
			operacionesDTO.add(toOperacionDTO(operacion));
		}
		return operacionesDTO;
	}
	
	public static OperacionDTO toOperacionDTO(ApiOperation operacion) {
		OperacionDTO operacionDTO = new OperacionDTO();
		
		operacionDTO.setDescripcion(operacion.getDescription());
		operacionDTO.setEndpoint(operacion.getEndpoint());
		operacionDTO.setIdentificacion(operacion.getIdentification());
		operacionDTO.setOperacion(operacion.getOperation());
		operacionDTO.setPath(operacion.getPath());
		
		// Se copian los headers
		operacionDTO.setHeaders(HeaderFIQL.toHeaderDTO(operacion.getApiheaders()));
		// Se copian los queryparams
		operacionDTO.setQueryParams(QueryParameterFIQL.toQueryParamDTO(operacion.getApiqueryparameters()));
		
		return operacionDTO;
	}

	public static ApiOperation copyProperties(OperacionDTO operacionDTO) {
		ApiOperation operacion = new ApiOperation();

		if (!isValidOperacion(operacionDTO.getOperacion())){
			Object parametros[]={operacionDTO.getIdentificacion()};
			throw new IllegalArgumentException("com.indra.sofia2.web.api.services.HeaderWrongOperacion");
		}
		
		operacion.setDescription(operacionDTO.getDescripcion());
		operacion.setEndpoint(operacionDTO.getEndpoint());
		operacion.setIdentification(operacionDTO.getIdentificacion());
		operacion.setOperation(operacionDTO.getOperacion());
		operacion.setPath(operacionDTO.getPath());
		
		
		return operacion;
	}

	private static boolean isValidOperacion(String operacion) {
		return (operacion.equalsIgnoreCase(GET)||
				operacion.equalsIgnoreCase(POST)||
				operacion.equalsIgnoreCase(PUT)||
				operacion.equalsIgnoreCase(DELETE));
	}
}
