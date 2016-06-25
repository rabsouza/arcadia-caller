package br.com.battista.arcadia.caller.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@XmlRootElement
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Vendor extends BaseEntity {

    @Id
    private Long id;

    @Index
    @NotNull
    private String vendor;

    @Index
    private String state;

    @Index
    private String city;

    @NotNull
    private Double price;

    @NotNull
    @Index
    private Long codeProduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Object getPk() {
        return id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getCodeProduct() {
        return codeProduct;
    }

    public void setCodeProduct(Long codeProduct) {
        this.codeProduct = codeProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vendor)) return false;
        Vendor vendor1 = (Vendor) o;
        return Objects.equal(getVendor(), vendor1.getVendor()) &&
                       Objects.equal(getCodeProduct(), vendor1.getCodeProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVendor(), getCodeProduct());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                       .add("id", id)
                       .add("vendor", vendor)
                       .add("state", state)
                       .add("city", city)
                       .add("price", price)
                       .add("codeProduct", codeProduct)
                       .toString();
    }

    public Vendor id(Long id) {
        this.id = id;
        return this;
    }

    public Vendor vendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public Vendor price(Double price) {
        this.price = price;
        return this;
    }

    public Vendor codeProduct(Long codeProduct) {
        this.codeProduct = codeProduct;
        return this;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Vendor state(String state) {
        this.state = state;
        return this;
    }

    public Vendor city(String city) {
        this.city = city;
        return this;
    }
}
