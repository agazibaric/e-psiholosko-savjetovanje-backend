package com.epsih.service;

import com.epsih.dto.MeetingRequestDto;
import com.epsih.exceptions.NotFoundException;
import com.epsih.exceptions.ServerErrorException;
import com.epsih.exceptions.UnauthorizedException;
import com.epsih.model.meeting.*;
import com.epsih.model.service.BusinessService;
import com.epsih.model.user.Doctor;
import com.epsih.model.user.Patient;
import com.epsih.repository.*;
import com.epsih.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientService {

   private final PatientRepository patientRepository;
   private final MeetingRepository meetingRepository;
   private final BusinessServiceRepository businessServiceRepository;
   private final DoctorRepository doctorRepository;
   private final QuestionRepository questionRepository;
   private final MeetingRequestRepository meetingRequestRepository;

   public Patient getCurrentPatient() {
      return SecurityUtils.getCurrentUsername()
         .flatMap(patientRepository::findOneByUser_Username)
         .orElseThrow(() -> new ServerErrorException("Can not find patient assigned to currently signed in user"));
   }

   public List<Meeting> getMyMeetings() {
      return getCurrentPatient().getMeetings();
   }

   public Meeting getMyMeeting(Long id) {
      Meeting meeting = meetingRepository.findById(id)
         .orElseThrow(() -> new NotFoundException("Meeting does not exist"));
      if (!meeting.getPatient().getId().equals(getCurrentPatient().getId()))
         throw new UnauthorizedException("Unauthorized to access the resource");
      return meeting;
   }

   public List<Termin> getMeetingTermins(Long meetingId) {
      Meeting meeting = meetingRepository.findById(meetingId)
         .orElseThrow(() -> new NotFoundException("Meeting does not exist"));
      if (!meeting.getPatient().getId().equals(getCurrentPatient().getId()))
         throw new UnauthorizedException("Unauthorized to access the resource");
      return meeting.getTermins();
   }

   public List<Review> getMyReviews() {
      return getCurrentPatient().getReviews();
   }

   @Transactional
   public MeetingRequest addMeetingRequest(MeetingRequestDto meetingRequestDto) {
      BusinessService service = businessServiceRepository.getOne(meetingRequestDto.getServiceID());
      List<Doctor> doctors = doctorRepository.findAllById(meetingRequestDto.getDoctors());
      List<Answer> answers = meetingRequestDto.getAnswers().stream()
         .map(a -> Answer.builder()
            .content(a.getContent())
            .question(questionRepository.getOne(a.getQuestionId()))
            .build()).collect(Collectors.toList());

      MeetingRequest meetingRequest = MeetingRequest.builder()
         .service(service)
         .doctors(doctors)
         .answers(answers)
         .patient(getCurrentPatient())
         .build();

      return meetingRequestRepository.save(meetingRequest);
   }

}
