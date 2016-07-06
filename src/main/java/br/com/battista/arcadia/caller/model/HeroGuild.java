package br.com.battista.arcadia.caller.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

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
public class HeroGuild extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    private Hero hero;

    private Card card1;

    private Card card2;

    private Card card3;

    private Card card4;

    @Override
    public Object getPk() {
        return getId();
    }
}
