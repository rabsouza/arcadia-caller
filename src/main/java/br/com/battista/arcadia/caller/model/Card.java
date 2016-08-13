package br.com.battista.arcadia.caller.model;

import static br.com.battista.arcadia.caller.constants.CacheConstant.DURATION_CACHE;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.battista.arcadia.caller.model.enuns.GroupCardEnum;
import br.com.battista.arcadia.caller.model.enuns.TypeCardEnum;
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
public class Card extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Index
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @Index
    @NotBlank
    @Size(min = 3, max = 10)
    private String key;

    @Index
    @NotNull
    private TypeCardEnum type;

    @Index
    @NotNull
    private GroupCardEnum group;

    @NotBlank
    private String typeEffect;

    @NotBlank
    private String groupEffect;

    private Integer cost;

    @Index
    private Boolean active = Boolean.TRUE;

    @Index
    private Boolean revise = Boolean.FALSE;

    @Index
    private Boolean denounce = Boolean.FALSE;

    @Index
    private Boolean deleted = Boolean.FALSE;


    @Override
    public Object getPk() {
        return getId();
    }
}
