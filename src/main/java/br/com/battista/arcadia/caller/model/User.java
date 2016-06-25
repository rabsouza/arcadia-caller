package br.com.battista.arcadia.caller.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Builder
@Data
@ToString(includeFieldNames = true)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @NonNull
    private String user;
    @NonNull
    private String mail;
    private String urlAvatar;
    private String token;

    @Override
    public Object getPk() {
        return getId();
    }
}
