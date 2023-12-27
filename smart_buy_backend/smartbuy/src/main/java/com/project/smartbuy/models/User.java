package com.project.smartbuy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fullname", length = 100)
  private String fullName;

  @Column(name = "phone_number", length = 15, nullable = false)
  private String phoneNumber;

  @Column(name = "address", length = 200)
  private String address;

  @Column(name = "password", length = 100, nullable = false)
  private String password;

  private boolean active;

  @Column(name = "date_of_birth")
  private Date dateOfBirth;

  @Column(name = "facebook_account_id")
  private int facebookAccountId;

  @Column(name = "google_account_id")
  private int googleAccountId;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  public void setRole(Role role) {
  }
}
