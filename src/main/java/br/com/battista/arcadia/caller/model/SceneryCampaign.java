package br.com.battista.arcadia.caller.model;

import java.io.Serializable;
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
public class SceneryCampaign extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Index
    @NotBlank
    @Size(min = 5, max = 50)
    private String name;

    @Index
    @NotNull
    private Scenery scenery;

    @Index
    private String winner;

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
