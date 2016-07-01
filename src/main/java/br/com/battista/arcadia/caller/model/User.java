package br.com.battista.arcadia.caller.model;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(includeFieldNames = true, exclude = {"token"})
@EqualsAndHashCode(of = {"username"}, callSuper = false)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Index
    @NotBlank
    @Size(min = 5, max = 30)
    private String username;

    @Index
    @NotBlank
    @Email
    @Size(min = 5, max = 30)
    private String mail;

    @URL
    private String urlAvatar;

    @JsonIgnore
    @Index
    @Size(min = 30, max = 30)
    private String token;

    @Override
    public Object getPk() {
        return getId();
    }
}
