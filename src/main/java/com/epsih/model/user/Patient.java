package com.epsih.model.user;

import java.util.List;

import javax.persistence.*;

import com.epsih.model.meeting.Meeting;
import com.epsih.model.meeting.MeetingRequest;
import com.epsih.model.meeting.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "patient")
public class Patient {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "pk_patient")
   private Long id;

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "fk_user", referencedColumnName = "pk_user")
   private User user;

   private String diagnosis;

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL)
   @JsonIgnore
   private List<Meeting> meetings;

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL)
   @JsonIgnore
   private List<MeetingRequest> meetingRequests;

   @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.ALL)
   @JsonIgnore
   private List<Review> reviews;

}
