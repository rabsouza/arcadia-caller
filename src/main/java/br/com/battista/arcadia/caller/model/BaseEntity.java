package br.com.battista.arcadia.caller.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.googlecode.objectify.annotation.Index;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import lombok.Data;

@Data
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity implements Serializable {

    private Date createdAt;

    @Index
    private Date updatedAt;

    @Index
    private Long version;

    @JsonIgnore
    public abstract Object getPk();

    public BaseEntity version(final Long version) {
        this.version = version;
        return this;
    }

    public void initEntity() {
        createdAt = new Date();
        updatedAt = createdAt;
        version = EntityConstant.DEFAULT_VERSION;
    }

    public void updateEntity() {
        updatedAt = new Date();
        version++;
    }

}
