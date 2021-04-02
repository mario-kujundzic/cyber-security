package com.security.admin.security.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class JwtAuthenticationRequest {
	@NonNull
	private String username;
	@NonNull
	private String password;
}
