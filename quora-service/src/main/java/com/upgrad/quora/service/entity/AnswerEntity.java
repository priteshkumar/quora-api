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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

//CREATE TABLE IF NOT EXISTS ANSWER(
// id SERIAL,uuid VARCHAR(200) NOT NULL,
// ans VARCHAR(255) NOT NULL,
// date TIMESTAMP NOT NULL ,
// user_id INTEGER NOT NULL,
// question_id INTEGER NOT NULL ,
// PRIMARY KEY(id),
// FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE, FOREIGN KEY (question_id) REFERENCES QUESTION(id) ON DELETE CASCADE);
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
  private long id;

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

  public void setId(long id) {
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
}
