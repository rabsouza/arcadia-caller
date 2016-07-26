package br.com.battista.arcadia.caller.model;

import java.io.Serializable;

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
public class KeyCampaign implements Serializable {

    public static final String PREFIX_KEY = "AQ-CB-";
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    @Index
    @NotBlank
    private Long index;

    @Index
    @NotBlank
    private String key;

}
