package me.mikholsky.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {
	@Id
	@Column(name = "authority")
	private String authority;

	@Override
	public String getAuthority() {
		return authority;
	}
}
