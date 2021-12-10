package com.codehunter.khonggiantruyen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Download.
 */
@Entity
@Table(name = "download")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Download implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "download_date")
    private Instant downloadDate;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "downloads", "product" }, allowSetters = true)
    private ResourceDownload resource;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Download id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDownloadDate() {
        return this.downloadDate;
    }

    public Download downloadDate(Instant downloadDate) {
        this.setDownloadDate(downloadDate);
        return this;
    }

    public void setDownloadDate(Instant downloadDate) {
        this.downloadDate = downloadDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Download user(User user) {
        this.setUser(user);
        return this;
    }

    public ResourceDownload getResource() {
        return this.resource;
    }

    public void setResource(ResourceDownload resourceDownload) {
        this.resource = resourceDownload;
    }

    public Download resource(ResourceDownload resourceDownload) {
        this.setResource(resourceDownload);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Download)) {
            return false;
        }
        return id != null && id.equals(((Download) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Download{" +
            "id=" + getId() +
            ", downloadDate='" + getDownloadDate() + "'" +
            "}";
    }
}
