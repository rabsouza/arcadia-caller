package br.com.battista.arcadia.caller.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.battista.arcadia.caller.model.enuns.DifficultySceneryEnum;
import br.com.battista.arcadia.caller.model.enuns.LocationSceneryEnum;
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
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class Scenery extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Index
    @NotBlank
    @Size(min = 5, max = 50)
    private String name;

    private String urlSymbol;

    private String wonTitle;

    private Card wonReward;

    private DifficultySceneryEnum difficulty;

    private List<String> benefitTitles;

    @NotNull
    private LocationSceneryEnum location;

    private Boolean active = Boolean.TRUE;

    private Boolean revise = Boolean.FALSE;

    private Boolean denounce = Boolean.FALSE;

    private Boolean deleted = Boolean.FALSE;

    @Override
    public Object getPk() {
        return getId();
    }
}
