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
package com.indracompany.sofia2.simulator.job;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.indracompany.sofia2.config.services.ontology.OntologyService;
import com.indracompany.sofia2.simulator.service.FieldRandomizerService;
import com.indracompany.sofia2.simulator.service.PersistenceService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeviceSimulatorJob {

	@Autowired
	OntologyService ontologyService;
	@Autowired
	private FieldRandomizerService fieldRandomizerService;
	@Autowired
	private PersistenceService persistenceService;

	private JsonNode schema;

	public void execute(JobExecutionContext context) throws JsonProcessingException, IOException {

		String user = context.getJobDetail().getJobDataMap().getString("userId");
		String json = context.getJobDetail().getJobDataMap().getString("json");
		String id = context.getJobDetail().getJobDataMap().getString("id");
		try {
			this.generateInstance(user, json);
		} catch (Exception e) {
			log.error("Error generating the ontology instance");
		}

	}

	public void generateInstance(String user, String json) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		JsonNode jsonInstance = mapper.readTree(json);

		String clientPlatform = jsonInstance.get("clientPlatform").asText();
		String clientPlatformInstance = jsonInstance.get("clientPlatformInstance").asText();
		String ontology = jsonInstance.get("ontology").asText();

		JsonNode ontologySchema = this.generateJsonSchema(ontology, user);
		JsonNode fieldAndValues = this.fieldRandomizerService.randomizeFields(jsonInstance.path("fields"),
				ontologySchema);

		log.debug("Inserted ontology: "+fieldAndValues.toString());
		this.persistenceService.insertOntologyInstance(fieldAndValues.toString(), ontology, user, clientPlatform,
				clientPlatformInstance);
	}

	public JsonNode generateJsonSchema(String ontology, String user) throws JsonProcessingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode ontologySchema = mapper
				.readTree(this.ontologyService.getOntologyByIdentification(ontology, user).getJsonSchema());
		this.schema = ontologySchema;
		JsonNode fieldsSchema = mapper.createObjectNode();

		if (!ontologySchema.path("datos").isMissingNode()) {

			Iterator<String> fields = ontologySchema.path("datos").path("properties").fieldNames();
			ontologySchema = ontologySchema.path("datos").path("properties");
			while (fields.hasNext()) {
				String field = fields.next();
				if (ontologySchema.path(field).get("type").asText().equals("string"))
					((ObjectNode) fieldsSchema).put(field, "");
				else if (ontologySchema.path(field).get("type").asText().equals("number"))
					((ObjectNode) fieldsSchema).put(field, 0);
				else if (ontologySchema.path(field).get("type").asText().equals("object")) {
					JsonNode object = this.createObjectNode(ontologySchema.path(field));
					((ObjectNode) fieldsSchema).set(field, object);
				} else if (ontologySchema.path(field).get("type").asText().equals("array")) {
					JsonNode object = this.createArrayNode(ontologySchema.path(field));
					JsonNode arrayNode = mapper.createArrayNode();
					((ArrayNode) arrayNode).add(object);
					((ObjectNode) fieldsSchema).set(field, object);
				}

			}
		}
		String context = mapper
				.readTree(this.ontologyService.getOntologyByIdentification(ontology, user).getJsonSchema())
				.get("properties").fields().next().getKey();
		return mapper.createObjectNode().set(context, fieldsSchema);

	}

	public JsonNode createObjectNode(JsonNode fieldNode) {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode objectNode = mapper.createObjectNode();

		if (!fieldNode.path("properties").isMissingNode()) {
			fieldNode = fieldNode.path("properties");
			Iterator<String> fields = fieldNode.fieldNames();

			while (fields.hasNext()) {
				String field = fields.next();
				if (fieldNode.path(field).get("type").asText().equals("string"))
					((ObjectNode) objectNode).put(field, "");
				else if (fieldNode.path(field).get("type").asText().equals("number"))
					((ObjectNode) objectNode).put(field, 0);
				else if (fieldNode.path(field).get("type").asText().equals("object")) {
					JsonNode object = this.createObjectNode(fieldNode.path(field));
					((ObjectNode) objectNode).set(field, object);
				} else if (fieldNode.path(field).get("type").asText().equals("array")) {
					JsonNode object = this.createArrayNode(fieldNode.path(field));
					JsonNode arrayNode = mapper.createArrayNode();
					((ArrayNode) arrayNode).add(object);
					((ObjectNode) objectNode).set(field, object);
				}

			}
		} else {
			Iterator<String> fields = fieldNode.fieldNames();
			while (fields.hasNext()) {
				String objectType = fields.next();
				if (objectType.equals("oneOf") || objectType.equals("anyOf")) {
					String property = fieldNode.get(objectType).get(0).get("$ref").asText().replace("#/", "");
					objectNode = this.createObjectNode(this.schema.path(property));
				}
			}

		}

		return objectNode;
	}

	public JsonNode createArrayNode(JsonNode fieldNode) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode objectNode = mapper.createObjectNode();

		if (!fieldNode.path("items").path("properties").isMissingNode()) {
			fieldNode = fieldNode.path("items").path("properties");
			Iterator<String> fields = fieldNode.fieldNames();

			while (fields.hasNext()) {
				String field = fields.next();
				if (fieldNode.path(field).get("type").asText().equals("string"))
					((ObjectNode) objectNode).put(field, "");
				else if (fieldNode.path(field).get("type").asText().equals("number"))
					((ObjectNode) objectNode).put(field, 0);
				else if (fieldNode.path(field).get("type").asText().equals("object")) {
					JsonNode object = this.createObjectNode(fieldNode.path(field));
					((ObjectNode) objectNode).set(field, object);
				}

			}

		} else if (!fieldNode.path("items").path("items").isMissingNode()) {

			fieldNode = fieldNode.path("items").path("items");
			Iterator<String> fields = fieldNode.fieldNames();

			while (fields.hasNext()) {
				String field = fields.next();
				if (fieldNode.path(field).get("type").asText().equals("string"))
					((ObjectNode) objectNode).put(field, "");
				else if (fieldNode.path(field).get("type").asText().equals("number"))
					((ObjectNode) objectNode).put(field, 0);
				else if (fieldNode.path(field).get("type").asText().equals("object")) {
					JsonNode object = this.createObjectNode(fieldNode.path(field));
					((ObjectNode) objectNode).set(field, object);
				}

			}

		} else if (fieldNode.path("items").isArray()) {
			fieldNode = fieldNode.path("items");
			int size = fieldNode.size();

			for (int i = 0; i < size; i++) {

				if (fieldNode.path(i).get("type").asText().equals("string"))
					((ObjectNode) objectNode).put(String.valueOf(i), "");
				else if (fieldNode.path(i).get("type").asText().equals("number"))
					((ObjectNode) objectNode).put(String.valueOf(i), 0);
				else if (fieldNode.path(i).get("type").asText().equals("object")) {
					JsonNode object = this.createObjectNode(fieldNode.path(i));
					((ObjectNode) objectNode).set(String.valueOf(i), object);
				}

			}

		}

		return objectNode;
	}

}
