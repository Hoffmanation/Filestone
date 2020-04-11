package com.filestone.service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.filestone.entity.Users;


/**
 * A Jpa-Repository-Interface for the {@link Users} DAO-Layer service
 * @author Hoffman
 *
 */
@RepositoryRestResource(path = "/User", collectionResourceRel = "User")
public interface UserRepository extends JpaRepository<Users, UUID> {

	@RestResource(exported = false)
	@Query("SELECT u FROM Users AS u WHERE u.username = :username AND u.password = :password")
	public Users findByUsernameAndPassword(@Param("username") String username,@Param("password") String password);
	
	@RestResource(exported = false)
	@Query("SELECT u FROM Users AS u WHERE u.username = :username")
	public Users findByUsername(@Param("username") String username);

	@RestResource(exported = false)
	@Query("SELECT u FROM Users AS u WHERE u.username = :username AND u.password = :password")
	public boolean login(@Param("username") String name, @Param("password") String password);

	@RestResource(exported = false)
	@Query("SELECT u FROM Users  AS u WHERE u.id = :id")
	public Users getUserById(@Param("id") UUID id);

	@RestResource(exported = false)
	@Query("SELECT u FROM Users  AS u")
	public List<Users> getAllUsers();
}
