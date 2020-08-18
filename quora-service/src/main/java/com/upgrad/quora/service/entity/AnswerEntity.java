package com.upgrad.quora.service.entity;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "answer", schema = "public")
@NamedQueries(
    {
        @NamedQuery(name = "answerByUuid", query = "select a from AnswerEntity a where a.uuid "
            + "= :uuid"),
        @NamedQuery(name = "allAnswersByQuestion", query =
            "select a from AnswerEntity a where a.question.id "
                + "= :question_id")
    }
)
public class AnswerEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "ans")
  @NotNull
  @Size(max = 255)
  private String ans;

  @Column(name = "\"date\"")
  @NotNull
  private ZonedDateTime postedDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "question_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private QuestionEntity question;

  public long getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getAns() {
    return ans;
  }

  public void setAns(String ans) {
    this.ans = ans;
  }

  public ZonedDateTime getPostedDate() {
    return postedDate;
  }

  public void setPostedDate(ZonedDateTime postedDate) {
    this.postedDate = postedDate;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public QuestionEntity getQuestion() {
    return question;
  }

  public void setQuestion(QuestionEntity question) {
    this.question = question;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AnswerEntity that = (AnswerEntity) o;

    return new EqualsBuilder()
        .append(id, that.id)
        .append(uuid, that.uuid)
        .append(ans, that.ans)
        .append(postedDate, that.postedDate)
        .append(user, that.user)
        .append(question, that.question)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(uuid)
        .append(ans)
        .append(postedDate)
        .append(user)
        .append(question)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("uuid", uuid)
        .append("ans", ans)
        .append("postedDate", postedDate)
        .append("user", user)
        .append("question", question)
        .toString();
  }
}
