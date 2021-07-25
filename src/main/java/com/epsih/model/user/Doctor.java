package com.epsih.model.user;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.epsih.model.meeting.Meeting;
import com.epsih.model.meeting.Review;
import com.epsih.model.service.BusinessService;
import com.epsih.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "doctor")
public class Doctor {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "pk_doctor")
   private Long id;

   @OneToOne
   @JoinColumn(name = "fk_user", referencedColumnName = "pk_user")
   private User user;

   @Column(nullable = true)
   private String biography;

   @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(name = "doctor_service",
      joinColumns = @JoinColumn(name = "fk_doctor"),
      inverseJoinColumns = @JoinColumn(name = "fk_service"))
   @JsonIgnore
   private Set<BusinessService> services;

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor", cascade = CascadeType.ALL)
   @JsonIgnore
   private List<Meeting> meetings;

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor", cascade = CascadeType.ALL)
   @JsonIgnore
   private List<Review> reviews;

}
