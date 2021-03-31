package com.security.admin.dto;

//import javax.validation.constraints.NotBlank;

public class UserTokenStateDTO {
	
	private Long id;
//    @NotBlank(message = "Access token is required!")
    private String accessToken;
    private Long expiresIn;

    public UserTokenStateDTO() {
        this.accessToken = null;
        this.expiresIn = null;
    }

    public UserTokenStateDTO(Long id, String accessToken, long expiresIn) {
    	this.id = id;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
