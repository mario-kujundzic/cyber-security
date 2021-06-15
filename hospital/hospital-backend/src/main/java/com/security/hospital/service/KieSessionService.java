package com.security.hospital.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.security.hospital.dto.ActivatedLogAlarmDTO;
import com.security.hospital.dto.LogAlarmDTO;
import com.security.hospital.dto.LogMessageDTO;
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
	public Object getGlobal(String identifier) {
		return this.kieSession.getGlobal(identifier);
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

	public void removeLogAlarms() {
		ObjectFilter filter = new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				if (object.getClass().equals(LogAlarmDTO.class)) {
					return true;
				}
				return false;
			}
		};
		Collection<FactHandle> events = this.kieSession.getFactHandles(filter);
		for (FactHandle handle : events) {
			this.kieSession.delete(handle);
		}
	}

	public void removeLogMessages() {
		ObjectFilter filter = new ObjectFilter() {
			@Override
			public boolean accept(Object object) {
				if (object.getClass().equals(LogMessageDTO.class)) {
					return true;
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

	public ArrayList<ActivatedLogAlarmDTO> computeActivatedAlarms(
			HashMap<String, ArrayList<LogMessageDTO>> logMap,
			ArrayList<LogAlarmDTO> configuredAlarms) {
		setGlobal("activatedAlarms", new ArrayList<ActivatedLogAlarmDTO>());

		for (LogAlarmDTO alarm : configuredAlarms) {
			insert(alarm);
		}

		for (ArrayList<LogMessageDTO> list : logMap.values()) {
			for (LogMessageDTO log : list) {
				insert(log);
			}
		}

		setAgendaFocus("alarms");
		fireAllRules();

		ArrayList<ActivatedLogAlarmDTO> result = (ArrayList<ActivatedLogAlarmDTO>) getGlobal("activatedAlarms");

		removeLogAlarms();
		removeLogMessages();

		return result;
	}
}
