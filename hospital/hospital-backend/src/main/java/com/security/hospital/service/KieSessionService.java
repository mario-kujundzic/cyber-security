package com.security.hospital.service;

import java.util.Collection;

import org.drools.core.ObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
	
	
	// primer sklanjanja eventova iz sesije
//	public void removeLoginEvents(Long userId) {
//		ObjectFilter filter = new ObjectFilter() {
//			@Override
//			public boolean accept(Object object) {
//				if (object.getClass().equals(InvalidLoginEvent.class)) {
//					InvalidLoginEvent event = (InvalidLoginEvent) object;
//					return event.getJobSeekerId().equals(userId);
//				}
//				return false;
//			}
//		};
//		Collection<FactHandle> events = this.kieSession.getFactHandles(filter);
//		for (FactHandle handle : events) {
//			this.kieSession.delete(handle);			
//		}
//	}

}
