package org.revo.auth.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * Created by ashraf on 17/04/17.
 */
@Document
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseUser {
    @Id
    private String id;
    @NotBlank
    private String name;
    private String imageUrl;
    @NotBlank
    private String phone;
    private String info;
    @Email
    @NotBlank
    private String email;
    @JsonProperty(access = WRITE_ONLY)
    @NotBlank
    private String password;
    @JsonProperty(access = WRITE_ONLY)
    @Transient
    private String currentPassword;
    @CreatedDate
    private Date createdDate = new Date();

    @Transient
    @JsonProperty(access = WRITE_ONLY)
    private MultipartFile image;

    @Transient
    private UserInfo userInfo;


    @Override
    @JsonProperty(access = READ_ONLY)
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                " ,createdDate='" + getCreatedDate() + '\'' +
                " ,name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", info='" + info + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}