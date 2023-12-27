package com.backend.usersapp.Backend.UsersApp.auth.filters;

import static com.backend.usersapp.Backend.UsersApp.auth.TokenJwtConfig.*;

import com.backend.usersapp.Backend.UsersApp.auth.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

	public JwtValidationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain chain) throws IOException, ServletException {
		String header = request.getHeader(HEADER_AUTHORIZATION);

		if(header == null || !header.startsWith(PREFIX_TOKEN)){
			chain.doFilter(request, response);
			return;
		}

		String token = header.replace(PREFIX_TOKEN, "");

		try{
			Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();

			//authorities: roles que vienen en JSON
			Object authoritiesClaims = claims.get("authorities");
			String username = claims.getSubject();

			//convertimos el ObjectMapper a byte[]
			//SimpleGrantedAuth se guardan los roles
			Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
					.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
					.readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

			//pasamos los authorities
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
					authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(request, response);
		} catch (JwtException e){
			Map<String, String> body = new HashMap<>();
			body.put("error", e.getMessage());
			body.put("message", "El token no es válido");

			response.getWriter().write(new ObjectMapper().writeValueAsString(body));
			response.setStatus(403);
			response.setContentType("application/json");
		}

	}
}
