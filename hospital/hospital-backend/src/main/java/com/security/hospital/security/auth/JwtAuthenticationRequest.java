package com.security.hospital.security.auth;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtAuthenticationRequest {

	@NonNull
	private String username;
	@NonNull
	private String password;

}
