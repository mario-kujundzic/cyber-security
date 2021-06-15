package com.security.hospital.model.devices;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_types")
@Data
@NoArgsConstructor
public class MessageType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(name = "param_name")
	private String paramName;
	
	@Column(name = "message_data_type")
	private MessageDataType messageDataType;
}
