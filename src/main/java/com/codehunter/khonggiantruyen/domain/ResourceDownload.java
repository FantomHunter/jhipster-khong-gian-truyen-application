package com.codehunter.khonggiantruyen.domain;

import com.codehunter.khonggiantruyen.domain.enumeration.FormatType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ResourceDownload.
 */
@Entity
@Table(name = "resource_download")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResourceDownload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private FormatType format;

    @OneToMany(mappedBy = "resource")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "resource" }, allowSetters = true)
    private Set<Download> downloads = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "author", "resourceDownloads", "likes", "comments", "ratings", "categories" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResourceDownload id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public ResourceDownload url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FormatType getFormat() {
        return this.format;
    }

    public ResourceDownload format(FormatType format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(FormatType format) {
        this.format = format;
    }

    public Set<Download> getDownloads() {
        return this.downloads;
    }

    public void setDownloads(Set<Download> downloads) {
        if (this.downloads != null) {
            this.downloads.forEach(i -> i.setResource(null));
        }
        if (downloads != null) {
            downloads.forEach(i -> i.setResource(this));
        }
        this.downloads = downloads;
    }

    public ResourceDownload downloads(Set<Download> downloads) {
        this.setDownloads(downloads);
        return this;
    }

    public ResourceDownload addDownload(Download download) {
        this.downloads.add(download);
        download.setResource(this);
        return this;
    }

    public ResourceDownload removeDownload(Download download) {
        this.downloads.remove(download);
        download.setResource(null);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ResourceDownload product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceDownload)) {
            return false;
        }
        return id != null && id.equals(((ResourceDownload) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceDownload{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", format='" + getFormat() + "'" +
            "}";
    }
}
