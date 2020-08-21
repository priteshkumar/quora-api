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
@Table(name = "user_auth", schema = "public")
@NamedQueries({
    @NamedQuery(name = "userAuthTokenByAccessToken", query = "select ut from UserAuthEntity ut where ut.accessToken = :accessToken ")
})
public class UserAuthEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UserEntity user;

  @Column(name = "ACCESS_TOKEN")
  @NotNull
  @Size(max = 500)
  private String accessToken;

  @Column(name = "EXPIRES_AT")
  @NotNull
  private ZonedDateTime expiresAt;

  @Column(name = "LOGIN_AT")
  @NotNull
  private ZonedDateTime loginAt;

  @Column(name = "LOGOUT_AT")
  private ZonedDateTime logoutAt;

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

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public ZonedDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(ZonedDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public ZonedDateTime getLoginAt() {
    return loginAt;
  }

  public void setLoginAt(ZonedDateTime loginAt) {
    this.loginAt = loginAt;
  }

  public ZonedDateTime getLogoutAt() {
    return logoutAt;
  }

  public void setLogoutAt(ZonedDateTime logoutAt) {
    this.logoutAt = logoutAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserAuthEntity that = (UserAuthEntity) o;

    return new EqualsBuilder()
        .append(id, that.id)
        .append(uuid, that.uuid)
        .append(user, that.user)
        .append(accessToken, that.accessToken)
        .append(expiresAt, that.expiresAt)
        .append(loginAt, that.loginAt)
        .append(logoutAt, that.logoutAt)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(uuid)
        .append(user)
        .append(accessToken)
        .append(expiresAt)
        .append(loginAt)
        .append(logoutAt)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("uuid", uuid)
        .append("user", user)
        .append("accessToken", accessToken)
        .append("expiresAt", expiresAt)
        .append("loginAt", loginAt)
        .append("logoutAt", logoutAt)
        .toString();
  }

}
