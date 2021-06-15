package com.security.hospital.model.requests;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.security.hospital.dto.ModifyUserRequestDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests_modify")
@Data
@NoArgsConstructor
public class ModifyUserRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username")
	private String username;

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "new_user_role")
	private String newRole;
	
	@Column(name = "current_user_role")
	private String currentRole;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "admin_id")
	private Long adminId;
	
	@Column(name = "admin_email")
	private String adminEmail;
	
	@Column(name = "hospital_name")
	private String hospitalName;

	private RequestStatus status;
	
	public ModifyUserRequest(ModifyUserRequestDTO dto) {
		this.username = dto.getUsername();
		this.name = dto.getName();
		this.surname = dto.getSurname();
		this.currentRole = dto.getCurrentRole();
		this.newRole = dto.getNewRole();
		this.userId = dto.getUserId();
		this.adminId = dto.getAdminId();
		this.adminEmail = dto.getAdminEmail();
		this.hospitalName = dto.getHospitalName();
		this.status = RequestStatus.PENDING;
	}
}
