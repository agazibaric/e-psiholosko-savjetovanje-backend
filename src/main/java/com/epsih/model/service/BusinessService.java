package com.epsih.model.service;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.epsih.model.meeting.MeetingRequest;
import com.epsih.model.user.Doctor;
import com.epsih.model.meeting.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "business_service", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "fk_category"}))
@Builder
public class BusinessService {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "pk_service")
   private Long id;

   @NotNull
   private String name;

   @NotNull
   private String description;

   @NotNull
   private Float price;

   @ManyToMany(fetch = FetchType.LAZY, mappedBy = "services", cascade = CascadeType.ALL)
   @JsonIgnore
   private List<Doctor> doctors;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "fk_category")
   @JsonIgnoreProperties("services")
   private BusinessCategory category;

   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(name = "service_question",
      joinColumns = @JoinColumn(name = "fk_service"),
      inverseJoinColumns = @JoinColumn(name = "fk_question"))
   @JsonIgnore
   private List<Question> questions;

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL)
   @JsonIgnore
   private List<MeetingRequest> meetingRequests;

}
