package com.epsih.model.meeting;

import com.epsih.model.service.BusinessService;
import com.epsih.model.user.Doctor;
import com.epsih.model.user.Patient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequest {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "pk_meeting_request")
   private Long id;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "fk_patient", referencedColumnName = "pk_patient")
   private Patient patient;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "fk_service", referencedColumnName = "pk_service")
   @JsonIgnoreProperties("hibernateLazyInitializer")
   private BusinessService service;

   @ManyToMany(cascade = CascadeType.ALL)
   @JoinTable(name = "meeting_request_doctor",
      joinColumns = @JoinColumn(name = "fk_meeting_request"),
      inverseJoinColumns = @JoinColumn(name = "fk_doctor"))
   private List<Doctor> doctors;

   @ManyToMany(cascade = CascadeType.ALL)
   @JoinTable(name = "meeting_request_answer",
      joinColumns = @JoinColumn(name = "fk_meeting_request"),
      inverseJoinColumns = @JoinColumn(name = "fk_answer"))
   private List<Answer> answers;

}
