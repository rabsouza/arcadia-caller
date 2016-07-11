package br.com.battista.arcadia.caller.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

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
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = {"name"}, callSuper = false)
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

    @NotNull
    private Integer life;

    private Boolean active = Boolean.TRUE;

    private Boolean revise = Boolean.FALSE;

    private Boolean denounce = Boolean.FALSE;

    @Override
    public Object getPk() {
        return getId();
    }
}
