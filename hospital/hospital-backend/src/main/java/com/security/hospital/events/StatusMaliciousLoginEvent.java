package com.security.hospital.events;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

import lombok.Getter;
import lombok.Setter;

@Role(Role.Type.EVENT)
@Expires("120d")
@Getter
@Setter
public class StatusMaliciousLoginEvent {
	
	private String IPAddress;
	private boolean attack;
	
	public StatusMaliciousLoginEvent(String IPAddress) {
		this.IPAddress = IPAddress;
	}
}
