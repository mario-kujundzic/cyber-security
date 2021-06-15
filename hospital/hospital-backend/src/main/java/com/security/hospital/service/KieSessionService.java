package com.security.hospital.service;

import java.util.Collection;

import org.drools.core.ObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.security.hospital.events.DosEvent;
import com.security.hospital.events.InvalidLoginEvent;
import com.security.hospital.events.MaliciousLoginEvent;


@Service
public class KieSessionService {

	private KieContainer kieContainer;
	private KieSession kieSession;

	@Autowired
	public KieSessionService(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
		this.kieSession = this.kieContainer.newKieSession("rulesSession");
	}

	public void insert(Object o) {
		this.kieSession.insert(o);
	}

	public void fireAllRules() {
		this.kieSession.fireAllRules();
	}

	public void dispose() {
		this.kieSession.dispose();
	}

	public void setGlobal(String identifier, Object value) {
		this.kieSession.setGlobal(identifier, value);
	}

	public void setAgendaFocus(String groupName) {
		this.kieSession.getAgenda().getAgendaGroup(groupName).setFocus();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection<Object> getObjectsFromSession(final Class factClass) {
		ObjectFilter filter = new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				return object.getClass().equals(factClass);
			}
		};

		Collection<Object> results = (Collection<Object>) kieSession.getObjects(filter);
		return results;
	}
	
	public void removeLoginEvents(String username) {
		ObjectFilter filter = new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				if (object.getClass().equals(InvalidLoginEvent.class)) {
					InvalidLoginEvent event = (InvalidLoginEvent) object;
					return event.getUsername().equals(username);
				}
				return false;
			}
		};
		Collection<FactHandle> events = this.kieSession.getFactHandles(filter);
		for (FactHandle handle : events) {
			this.kieSession.delete(handle);			
		}
	}
	
	public void removeMaliciousLoginEvents(String IPAddress) {
		ObjectFilter filter = new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				if (object.getClass().equals(MaliciousLoginEvent.class)) {
					MaliciousLoginEvent event = (MaliciousLoginEvent) object;
					return event.getIPAddress().equals(IPAddress);
				}
				return false;
			}
		};
		Collection<FactHandle> events = this.kieSession.getFactHandles(filter);
		for (FactHandle handle : events) {
			this.kieSession.delete(handle);			
		}
	}
	
	public void removeDosEvents(String IPAddress) {
		ObjectFilter filter = new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				if (object.getClass().equals(DosEvent.class)) {
					DosEvent event = (DosEvent) object;
					return event.getIPAddress().equals(IPAddress);
				}
				return false;
			}
		};
		Collection<FactHandle> events = this.kieSession.getFactHandles(filter);
		for (FactHandle handle : events) {
			this.kieSession.delete(handle);			
		}
	}

}
