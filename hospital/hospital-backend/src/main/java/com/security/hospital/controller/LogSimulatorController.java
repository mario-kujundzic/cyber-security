package com.security.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.hospital.service.LogSimulator;

@RestController
@RequestMapping(value = "/api/simulator", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogSimulatorController {
	
	@Autowired
	private LogSimulator service;
	
	@PostMapping()
	public ResponseEntity<Object> confirmUserAdd(@RequestBody String state) {
		service.simulate(state);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
