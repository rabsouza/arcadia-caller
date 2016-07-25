package br.com.battista.arcadia.caller.model;

import static br.com.battista.arcadia.caller.constants.CacheConstant.DURATION_CACHE;

import java.io.Serializable;

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
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Cache(expirationSeconds = DURATION_CACHE)
public class SceneryCampaign extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Index
    @NotBlank
    @Size(min = 5, max = 50)
    private String nameScenery;

    private Scenery scenery;

    private String usernameWinner;

    private String usernameLeastDeaths;

    private String usernameMostCoins;

    private String usernameWonReward;

    private String usernameWonTitle;

    private Boolean active = Boolean.TRUE;

    private Boolean completed = Boolean.FALSE;

    private Boolean deleted = Boolean.FALSE;

    @Override
    public Object getPk() {
        return getId();
    }
}
