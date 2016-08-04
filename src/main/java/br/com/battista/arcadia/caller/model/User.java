package br.com.battista.arcadia.caller.model;

import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Index
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(includeFieldNames = true, callSuper = true, exclude = {"token"})
@EqualsAndHashCode(of = {"username"}, callSuper = false)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Index
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

    @Index
    @NotNull
    private ProfileAppConstant profile;

    @Index
    @Size(min = 30, max = 50)
    @JsonIgnore
    private String token;

    @Index
    private Set<String> friends;

    @Override
    public Object getPk() {
        return getId();
    }

    public void addFriends(Set<String> friends) {
        if (this.friends == null) {
            this.friends = Sets.newLinkedHashSet();
        }
        this.friends.clear();
        this.friends.addAll(friends);
    }
}
