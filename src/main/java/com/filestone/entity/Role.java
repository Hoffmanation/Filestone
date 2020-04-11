package com.filestone.entity;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * This Entity class will be use to store the user-role information in the DB that will be validate and authenticate by Spring-Security
 * @author Hoffman
 *
 */
@Entity
@Table(name = "role")
public class Role {
    private UUID id;
    private String name;
    private Set<Users> users;

    public Role() {
		// TODO Auto-generated constructor stub
	}
    
    
    public Role(String name) {
		super();
		this.name = name;
	}


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles")
    public Set<Users> getUsers() {
        return users;
    }

    public void setUsers(Set<Users> users) {
        this.users = users;
    }
}