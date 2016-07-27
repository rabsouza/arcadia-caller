package br.com.battista.arcadia.caller.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Campaign extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String alias;

    @NotNull
    private Date when;

    @Index
    private String guild01;

    @Index
    private Guild heroesGuild01;

    @Index
    private String guild02;

    @Index
    private Guild heroesGuild02;

    @Index
    private String guild03;

    @Index
    private Guild heroesGuild03;

    @Index
    private String guild04;

    @Index
    private Guild heroesGuild04;

    @Index
    private String key;

    private SceneryCampaign scenery1;

    private SceneryCampaign scenery2;

    private SceneryCampaign scenery3;

    private SceneryCampaign scenery4;

    private SceneryCampaign scenery5;

    private SceneryCampaign scenery6;

    @Index
    @NotBlank
    @Size(min = 5, max = 30)
    private String created;

    @Index
    private List<String> winner;

    @Index
    private List<String> leastDeaths;

    @Index
    private List<String> mostCoins;

    @Index
    private List<String> wonReward;

    @Index
    private List<String> wonTitle;

    @Index
    private Boolean active = Boolean.TRUE;

    @Index
    private Boolean completed = Boolean.FALSE;

    @Index
    private Boolean deleted = Boolean.FALSE;

    @Override
    public Object getPk() {
        return getId();
    }
}
