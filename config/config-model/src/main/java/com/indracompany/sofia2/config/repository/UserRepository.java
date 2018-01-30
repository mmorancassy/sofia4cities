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
 * 2013 - 2018  SPAIN
 *
 * All rights reserved
 ******************************************************************************/
package com.indracompany.sofia2.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.indracompany.sofia2.config.model.User;

public interface UserRepository extends JpaRepository<User, String> {


	List<User> findByEmail(String email);
	
	User findByUserId(String userId);
	
	//@Query("SELECT o FROM User AS o WHERE (o.userId = :userId AND o.password = :password AND o.active = true")
	//User authenticate(@Param("userId")String userId,@Param("password")String password);

	User findByUserIdAndPasswordAndActiveTrue(String userId,String password);

	@Query("SELECT o FROM User AS o WHERE o.roleTypeId !='1'")
	List<User> findUsersNoAdmin();
	@Query("SELECT o FROM User AS o WHERE (o.userId LIKE %:userId% OR o.fullName LIKE %:fullName% OR o.email LIKE %:email% OR o.roleTypeId.name =:roleType)")
	List<User> findByUserIdOrFullNameOrEmailOrRoleType(@Param("userId")String userId, @Param("fullName")String fullName,@Param("email")String email, @Param("roleType")String roleType);
	@Query("SELECT o FROM User AS o WHERE (o.userId LIKE %:userId% OR o.fullName LIKE %:fullName% OR o.email LIKE %:email% OR o.roleTypeId.name =:roleType) AND (o.active=:#{#active})")
	List<User> findByUserIdOrFullNameOrEmailOrRoleTypeAndActive(@Param("userId")String userId, @Param("fullName")String fullName,@Param("email")String email, @Param("roleType")String roleType,@Param("active")boolean active);
}
