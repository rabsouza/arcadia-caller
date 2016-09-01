package br.com.battista.arcadia.caller.model;

import static br.com.battista.arcadia.caller.constants.CacheConstant.DURATION_CACHE;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.battista.arcadia.caller.model.enuns.GroupHeroEnum;
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
public class Hero extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Index
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @URL
    private String urlPhoto;

    @NotNull
    private Integer defense;

    @Index
    @NotBlank
    private String ability;

    @Index
    @NotNull
    private GroupHeroEnum group;

    @NotNull
    private Integer life;

    @Index
    private Boolean active = Boolean.TRUE;

    @Index
    private Boolean revise = Boolean.FALSE;

    @Index
    private Boolean denounce = Boolean.FALSE;

    @Index
    private Boolean deleted = Boolean.FALSE;

    @Index
    private String locale;

    @Override
    public Object getPk() {
        return getId();
    }
}
