package br.com.battista.arcadia.caller.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import br.com.battista.arcadia.caller.model.enuns.NameGuildEnum;
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
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Guild extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private Integer victories = 0;

    private Integer defeats = 0;

    @Index
    @NotNull
    private NameGuildEnum name;

    @Index
    @NotNull
    private User user;

    @NotNull
    private HeroGuild hero1;

    @NotNull
    private HeroGuild hero2;

    @NotNull
    private HeroGuild hero3;

    @Override
    public Object getPk() {
        return getId();
    }
}
