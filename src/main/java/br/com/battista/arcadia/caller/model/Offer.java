package br.com.battista.arcadia.caller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@XmlRootElement
@Entity
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    @Index
    @NotNull
    @Size(min = 5, max = 50)
    private String name;
    @Index
    @NotNull
    private String category;

    @Ignore
    private List<Vendor> vendors;

    @Ignore
    private Double averagePrice;

    @Index
    @NotNull
    private String brand;

    @Index
    private Boolean revise = Boolean.FALSE;

    @Index
    private Boolean denounce = Boolean.FALSE;

    @Index
    @NotNull
    private Long codeProduct;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAveragePrice() {
        if (vendors != null && !vendors.isEmpty()) {
            averagePrice = caluleteAveragePrice(vendors);
        }
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    private Double caluleteAveragePrice(List<Vendor> vendors) {
        if (CollectionUtils.isEmpty(vendors)) {
            return 0d;
        }

        List<Vendor> vendorsSum = new ArrayList<>(vendors);

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);

        CollectionUtils.filter(vendorsSum, new Predicate<Vendor>() {
            @Override
            public boolean evaluate(Vendor vendor) {
                if (vendor == null || vendor.getUpdatedAt() == null || vendor.getPrice() == null) {
                    return Boolean.FALSE;
                }
                return vendor.getUpdatedAt().getTime() >= calendar.getTimeInMillis();
            }
        });

        if (vendorsSum.isEmpty()) {
            return vendors.get(0).getPrice();
        } else {
            Double averagePrice = 0d;
            for (Vendor vendor : vendorsSum) {
                averagePrice += vendor.getPrice();
            }
            return averagePrice / vendorsSum.size();
        }
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    public Boolean getRevise() {
        return revise;
    }

    public void setRevise(Boolean revise) {
        this.revise = revise;
    }

    public Boolean getDenounce() {
        return denounce;
    }

    public void setDenounce(Boolean denounce) {
        this.denounce = denounce;
    }

    public Offer id(Long id) {
        this.id = id;
        return this;
    }

    public Offer name(String name) {
        this.name = name;
        return this;
    }

    public Offer category(String category) {
        this.category = category;
        return this;
    }

    public Offer averagePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
        return this;
    }

    public Offer brand(String brand) {
        this.brand = brand;
        return this;
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
        if (!(o instanceof Offer)) return false;
        Offer offer = (Offer) o;
        return Objects.equal(getCodeProduct(), offer.getCodeProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCodeProduct());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                       .add("id", id)
                       .add("name", name)
                       .add("category", category)
                       .add("vendors", vendors)
                       .add("averagePrice", averagePrice)
                       .add("brand", brand)
                       .add("revise", revise)
                       .add("denounce", denounce)
                       .add("codeProduct", codeProduct)
                       .toString();
    }

    @Override
    public Object getPk() {
        return getId();
    }

    public Offer codeProduct(Long codeProduct) {
        this.codeProduct = codeProduct;
        return this;
    }

    public Offer revise(Boolean revise) {
        this.revise = revise;
        return this;
    }

    public Offer denounce(Boolean denounce) {
        this.denounce = denounce;
        return this;
    }

    public Offer vendors(List<Vendor> vendors) {
        this.vendors = vendors;
        return this;
    }
}
