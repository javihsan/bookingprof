package com.diloso.weblogin.aut;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.google.appengine.api.users.User;

public class AccountsAuthenticationProvider implements AuthenticationProvider {
	
	protected UserRegistry userRegistry;
	protected List usersAuthenticatedAdmin;
	
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		boolean isAuto = false;
		User googleUser = (User) authentication.getPrincipal();
		String userMail = googleUser.getEmail().toLowerCase();
		// Nos aseguramos que el usuario esté registrado
		Long firmId = (Long)authentication.getCredentials();
		AppUser user = userRegistry.findUser(userMail,firmId);
		if (user != null && user.isEnabled()) {
						
			isAuto = true;

		} else if (getUsersAuthenticatedAdmin().contains(userMail)){
			
				// SOLO PARA CREAR USUARIOS ESPECIALES
			
				Set<AppRole> roles = EnumSet.noneOf(AppRole.class);
				roles.add(AppRole.ADMIN);
				isAuto = true;
				user = new AppUser(userMail, roles, true, null);
				if (userRegistry.findUser(userMail,null)==null){
					// Usuario no registado. Lo registramos si cumple con la seguridad
					userRegistry.registerUser(user);
				}	
		}

		if (isAuto) {
			// Traspasamos la información de user a userAuthentication, 
			// que es  la verdadera Authentication
			Collection<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
			Set<AppRole> rolesUser = user.getAuthorities();
			for (AppRole r : rolesUser) {
				roles.add(r);
			}

			UserAuthentication userAuthentication = new UserAuthentication(
					authentication.getName(),  
					roles,
					authentication.getCredentials(),
					authentication.getDetails(), // aqui va el path
					user, 
					true);

			return userAuthentication;

		} else {
			throw new DisabledException("Account is disabled.");
		}

	}

	public final boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class
				.isAssignableFrom(authentication);
	}

	public void setUserRegistry(UserRegistry userRegistry) {
		this.userRegistry = userRegistry;
	}

	public List getUsersAuthenticatedAdmin() {
		return usersAuthenticatedAdmin;
	}

	public void setUsersAuthenticatedAdmin(List usersAuthenticatedAdmin) {
		this.usersAuthenticatedAdmin = usersAuthenticatedAdmin;
	}
	
	
}
