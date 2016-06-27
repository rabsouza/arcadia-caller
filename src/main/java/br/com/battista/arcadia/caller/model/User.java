package br.com.battista.arcadia.caller.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = {"user"}, callSuper = false)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Index
    @NotNull
    @Size(min = 5, max = 30)
    private String user;

    @Index
    @NotNull
    @Email
    @Size(min = 5, max = 30)
    private String mail;

    @URL
    private String urlAvatar;

    @Index
    @Size(min = 30, max = 30)
    private String token;

    @Override
    public Object getPk() {
        return getId();
    }

}
