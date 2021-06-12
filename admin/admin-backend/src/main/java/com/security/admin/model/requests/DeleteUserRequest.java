package com.security.admin.model.requests;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.security.admin.dto.DeleteUserRequestDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requests_delete")
@Data
@NoArgsConstructor
public class DeleteUserRequest {

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
	
	@Column(name = "reason")
	private String reason;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "admin_id")
	private Long adminId;
	
	@Column(name = "admin_email")
	private String adminEmail;
	
	@Column(name = "hospital_name")
	private String hospitalName;

	private RequestStatus status;
	
	public DeleteUserRequest(DeleteUserRequestDTO dto) {
		this.username = dto.getUsername();
		this.name = dto.getName();
		this.surname = dto.getSurname();
		this.reason = dto.getReason();
		this.userId = dto.getUserId();
		this.adminId = dto.getAdminId();
		this.adminEmail = dto.getAdminEmail();
		this.hospitalName = dto.getHospitalName();
		this.role = dto.getRole();
		this.status = RequestStatus.PENDING;
	}
}
