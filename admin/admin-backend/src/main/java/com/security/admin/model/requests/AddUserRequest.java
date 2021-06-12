package com.security.admin.model.requests;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.security.admin.dto.AddUserRequestDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests_add")
@Data
@NoArgsConstructor
public class AddUserRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username")
	private String username;

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "role")
	private String role;

	@Column(name = "admin_id")
	private Long adminId;
	
	@Column(name = "admin_email")
	private String adminEmail;
	
	@Column(name = "hospital_name")
	private String hospitalName;

	private RequestStatus status;
	
	public AddUserRequest(AddUserRequestDTO dto) {
		this.username = dto.getUsername();
		this.name = dto.getName();
		this.surname = dto.getSurname();
		this.role = dto.getRole();
		this.adminId = dto.getAdminId();
		this.adminEmail = dto.getAdminEmail();
		this.hospitalName = dto.getHospitalName();
		this.status = RequestStatus.PENDING;
	}
}
