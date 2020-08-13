package com.upgrad.quora.service.entity;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*CREATE TABLE IF NOT EXISTS QUESTION(id SERIAL,uuid VARCHAR(200) NOT NULL,
    content VARCHAR(500) NOT NULL,
    date TIMESTAMP NOT NULL ,
    user_id INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE);
 */

@Entity
@Table(name = "question", schema = "public")
public class QuestionEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "content")
  @NotNull
  @Size(max = 500)
  private String content;

  @Column(name = "\"date\"") //to be checked later escape for reserved keyword in postgresql
  @NotNull
  private ZonedDateTime createdDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UserEntity user;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    QuestionEntity that = (QuestionEntity) o;

    return new EqualsBuilder()
        .append(id, that.id)
        .append(uuid, that.uuid)
        .append(content, that.content)
        .append(createdDate, that.createdDate)
        .append(user, that.user)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(uuid)
        .append(content)
        .append(createdDate)
        .append(user)
        .toHashCode();
  }
}
