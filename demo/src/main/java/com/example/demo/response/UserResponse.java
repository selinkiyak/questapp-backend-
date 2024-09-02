package com.example.demo.response;

import com.example.demo.entity.User;

public class UserResponse {
    private Long id;
    private String userName;
    private String surname; // Soyisim
    private String birthDate; // Doğum Tarihi
    private String zodiac; // Burç
    private byte[] profileImage; // Profil Fotoğrafı
    private Object avatar; // Avatar

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.userName = entity.getUserName();
        this.surname = entity.getSurname();
        this.birthDate = entity.getBirthDate();
        this.zodiac = entity.getZodiac();
        this.profileImage = entity.getProfileImage();
        this.avatar = entity.getAvatar();
    }

    // Getter ve Setter metodları

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }



    public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public void setAvatar(Object avatar) {
		this.avatar = avatar;
	}

	public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
