package com.codehunter.khonggiantruyen.domain;

import com.codehunter.khonggiantruyen.domain.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "publish_date")
    private Instant publishDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "type")
    private String type;

    @Column(name = "total_chapter")
    private Long totalChapter;

    @OneToOne
    @JoinColumn(unique = true)
    private Author author;

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "downloads", "product" }, allowSetters = true)
    private Set<ResourceDownload> resourceDownloads = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "product" }, allowSetters = true)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "product" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "rating")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "rating" }, allowSetters = true)
    private Set<Rating> ratings = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_product__category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Product imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Instant getPublishDate() {
        return this.publishDate;
    }

    public Product publishDate(Instant publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public Product status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public Product type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotalChapter() {
        return this.totalChapter;
    }

    public Product totalChapter(Long totalChapter) {
        this.setTotalChapter(totalChapter);
        return this;
    }

    public void setTotalChapter(Long totalChapter) {
        this.totalChapter = totalChapter;
    }

    public Author getAuthor() {
        return this.author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Product author(Author author) {
        this.setAuthor(author);
        return this;
    }

    public Set<ResourceDownload> getResourceDownloads() {
        return this.resourceDownloads;
    }

    public void setResourceDownloads(Set<ResourceDownload> resourceDownloads) {
        if (this.resourceDownloads != null) {
            this.resourceDownloads.forEach(i -> i.setProduct(null));
        }
        if (resourceDownloads != null) {
            resourceDownloads.forEach(i -> i.setProduct(this));
        }
        this.resourceDownloads = resourceDownloads;
    }

    public Product resourceDownloads(Set<ResourceDownload> resourceDownloads) {
        this.setResourceDownloads(resourceDownloads);
        return this;
    }

    public Product addResourceDownload(ResourceDownload resourceDownload) {
        this.resourceDownloads.add(resourceDownload);
        resourceDownload.setProduct(this);
        return this;
    }

    public Product removeResourceDownload(ResourceDownload resourceDownload) {
        this.resourceDownloads.remove(resourceDownload);
        resourceDownload.setProduct(null);
        return this;
    }

    public Set<Like> getLikes() {
        return this.likes;
    }

    public void setLikes(Set<Like> likes) {
        if (this.likes != null) {
            this.likes.forEach(i -> i.setProduct(null));
        }
        if (likes != null) {
            likes.forEach(i -> i.setProduct(this));
        }
        this.likes = likes;
    }

    public Product likes(Set<Like> likes) {
        this.setLikes(likes);
        return this;
    }

    public Product addLike(Like like) {
        this.likes.add(like);
        like.setProduct(this);
        return this;
    }

    public Product removeLike(Like like) {
        this.likes.remove(like);
        like.setProduct(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setProduct(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setProduct(this));
        }
        this.comments = comments;
    }

    public Product comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Product addComment(Comment comment) {
        this.comments.add(comment);
        comment.setProduct(this);
        return this;
    }

    public Product removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setProduct(null);
        return this;
    }

    public Set<Rating> getRatings() {
        return this.ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        if (this.ratings != null) {
            this.ratings.forEach(i -> i.setRating(null));
        }
        if (ratings != null) {
            ratings.forEach(i -> i.setRating(this));
        }
        this.ratings = ratings;
    }

    public Product ratings(Set<Rating> ratings) {
        this.setRatings(ratings);
        return this;
    }

    public Product addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setRating(this);
        return this;
    }

    public Product removeRating(Rating rating) {
        this.ratings.remove(rating);
        rating.setRating(null);
        return this;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Product categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Product addCategory(Category category) {
        this.categories.add(category);
        category.getProducts().add(this);
        return this;
    }

    public Product removeCategory(Category category) {
        this.categories.remove(category);
        category.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", totalChapter=" + getTotalChapter() +
            "}";
    }
}
