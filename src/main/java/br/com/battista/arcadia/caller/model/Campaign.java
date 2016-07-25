package br.com.battista.arcadia.caller.model;

import static br.com.battista.arcadia.caller.constants.CacheConstant.DURATION_CACHE;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(includeFieldNames = true, callSuper = true)
@EqualsAndHashCode(of = {"name"}, callSuper = false)
@Cache(expirationSeconds = DURATION_CACHE)
public class Campaign extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String alias;

    @NotNull
    private Date when;

    @Index
    private String usernameGuild1;

    @Index
    private String usernameGuild2;

    @Index
    private String usernameGuild3;

    @Index
    private String usernameGuild4;

    @Index
    @NotBlank
    @Size(min = 5, max = 50)
    private String key;

    private SceneryCampaign scenery1;

    private SceneryCampaign scenery2;

    private SceneryCampaign scenery3;

    private SceneryCampaign scenery4;

    private SceneryCampaign scenery5;

    private SceneryCampaign scenery6;

    @Index
    @NotNull
    private User created;

    @Index
    private String usernameCreated;

    private Boolean active = Boolean.TRUE;

    private Boolean completed = Boolean.FALSE;

    private Boolean deleted = Boolean.FALSE;

    @Override
    public Object getPk() {
        return getId();
    }
}
