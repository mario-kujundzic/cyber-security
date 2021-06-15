package com.security.hospital.events;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Role(Role.Type.EVENT)
@Expires("5m")
@Data
@Getter
@Setter
@RequiredArgsConstructor
public class StatusLoginEvent {
	
	@NonNull
	private String username;
	private boolean attack;
	private boolean dos;

}
