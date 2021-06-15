package com.security.hospital.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.security.hospital.dto.LogMessageDTO;
import com.security.hospital.enums.LogMessageType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "logs")
public class Log {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(name = "timestamp")
	private Date timestamp;

	@Column(name = "content")
	private String content;	
	
	@Column(name = "type")
	private LogMessageType type;

	@Column(name= "params")
	private Map<String, Object> params = new HashMap<>();

	public Log(LogMessageDTO message) {
		this.timestamp = message.getSdf().getCalendar().getTime();
		this.content = message.getContent();
		this.type = message.getType();
		this.params = message.getParams();
	}

}
