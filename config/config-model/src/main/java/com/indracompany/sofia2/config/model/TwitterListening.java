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
package com.indracompany.sofia2.config.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Configurable;

import com.indracompany.sofia2.config.model.base.AuditableEntityWithUUID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TWITTER_LISTENING")
@Configurable
public class TwitterListening extends AuditableEntityWithUUID {

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "ONTOLOGY_ID", referencedColumnName = "ID", nullable = false)
	@Getter
	@Setter
	private Ontology ontology;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
	@Getter
	@Setter
	private User user;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "CONFIGURATION_ID", referencedColumnName = "ID")
	@Getter
	@Setter
	private Configuration configuration;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "TOKEN_ID", referencedColumnName = "ID")
	@Getter
	@Setter
	private Token token;

	@Column(name = "IDENTIFICATOR", length = 50, nullable = false)
	@NotNull
	@Getter
	@Setter
	private String identificator;

	@Column(name = "dateFrom", length = 100, nullable = false)
	@NotNull
	@Getter
	@Setter
	private String dateFrom;

	@Column(name = "dateTo", length = 100, nullable = false)
	@NotNull
	@Getter
	@Setter
	private String dateTo;

	@Column(name = "topics", length = 512, nullable = false)
	@NotNull
	@Getter
	@Setter
	private String topics;

	@Column(name = "cron", length = 100)
	@Getter
	@Setter
	private String cron;

}