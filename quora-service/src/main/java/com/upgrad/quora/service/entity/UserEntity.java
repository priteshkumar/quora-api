package com.upgrad.quora.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringExclude;

//CREATE TABLE IF NOT EXISTS USERS(id SERIAL,
// uuid VARCHAR(200) NOT NULL ,
// firstName VARCHAR(30) NOT NULL ,
// lastName VARCHAR(30) NOT NULL ,
// userName VARCHAR(30) UNIQUE NOT NULL,
// email VARCHAR(50) UNIQUE NOT NULL ,
// password VARCHAR(255) NOT NULL,
// salt VARCHAR(200) NOT NULL ,
// country VARCHAR(30) ,
// aboutMe VARCHAR(50),
// dob VARCHAR(30),
// role VARCHAR(30),
// contactNumber VARCHAR(30),
// PRIMARY KEY (id));
@Entity
@Table(name = "users",schema = "public")
@NamedQueries(
    {
        @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid = :uuid"),
        @NamedQuery(name = "userByUserName", query = "select u from UserEntity u where u"
            + ".userName = :userName"),
        @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u"
            + ".email =:email")
    }
)
public class UserEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "uuid")
  @NotNull
  @Size(max = 200)
  private String uuid;

  @Column(name = "firstname")
  @NotNull
  @Size(max = 30)
  private String firstName;

  @Column(name = "lastname")
  @NotNull
  @Size(max = 30)
  private String lastName;

  @Column(name = "username", unique = true)
  @NotNull
  @Size(max = 30)
  private String userName;

  @Column(name = "email", unique = true)
  @NotNull
  @Size(max = 50)
  private String email;

  @ToStringExclude
  @Column(name = "password")
  @NotNull
  @Size(max = 255)
  private String password;

  @Column(name = "salt")
  @NotNull
  @Size(max = 200)
  private String salt;

  @Column(name = "country")
  @Size(max = 30)
  private String country;

  @Column(name = "aboutme")
  @Size(max = 50)
  private String aboutMe;

  @Column(name = "dob")
  @Size(max = 30)
  private String dob;

  @Column(name = "role")
  @Size(max = 30)
  private String role;

  @Column(name = "contactnumber")
  @Size(max = 30)
  private String contactNumber;

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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("uuid", uuid)
        .append("firstName", firstName)
        .append("lastName", lastName)
        .append("userName", userName)
        .append("email", email)
        .append("password", password)
        .append("salt", salt)
        .append("country", country)
        .append("aboutMe", aboutMe)
        .append("dob", dob)
        .append("role", role)
        .append("contactNumber", contactNumber)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserEntity that = (UserEntity) o;

    return new EqualsBuilder()
        .append(id, that.id)
        .append(uuid, that.uuid)
        .append(firstName, that.firstName)
        .append(lastName, that.lastName)
        .append(userName, that.userName)
        .append(email, that.email)
        .append(password, that.password)
        .append(salt, that.salt)
        .append(country, that.country)
        .append(aboutMe, that.aboutMe)
        .append(dob, that.dob)
        .append(role, that.role)
        .append(contactNumber, that.contactNumber)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(id)
        .append(uuid)
        .append(firstName)
        .append(lastName)
        .append(userName)
        .append(email)
        .append(password)
        .append(salt)
        .append(country)
        .append(aboutMe)
        .append(dob)
        .append(role)
        .append(contactNumber)
        .toHashCode();
  }

}
