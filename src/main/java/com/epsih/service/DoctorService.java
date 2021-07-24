package com.epsih.service;

import com.epsih.dto.AcceptMeetingRequestDto;
import com.epsih.dto.CloseMeetingDto;
import com.epsih.dto.MeetingRequestDto;
import com.epsih.exceptions.BadRequestException;
import com.epsih.exceptions.NotFoundException;
import com.epsih.exceptions.ServerErrorException;
import com.epsih.exceptions.UnauthorizedException;
import com.epsih.model.meeting.MeetingRequest;
import com.epsih.model.user.Doctor;
import com.epsih.model.meeting.Meeting;
import com.epsih.model.meeting.Review;
import com.epsih.model.meeting.Termin;
import com.epsih.repository.DoctorRepository;
import com.epsih.repository.MeetingRepository;
import com.epsih.repository.MeetingRequestRepository;
import com.epsih.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class DoctorService {

   private final DoctorRepository doctorRepository;
   private final MeetingRepository meetingRepository;
   private final MeetingRequestRepository meetingRequestRepository;

   public Doctor getCurrentDoctor() {
      return SecurityUtils.getCurrentUsername()
         .flatMap(doctorRepository::findOneByUser_Username)
         .orElseThrow(() -> new ServerErrorException("Something went wrong!"));
   }

   public List<Meeting> getMyMeetings() {
      return getCurrentDoctor().getMeetings();
   }

   public Meeting getMyMeeting(Long id) {
      Meeting meeting = meetingRepository.findById(id)
         .orElseThrow(() -> new NotFoundException("Meeting does not exist"));
      if (!meeting.getDoctor().getId().equals(getCurrentDoctor().getId()))
         throw new UnauthorizedException("Unauthorized to access the resource");
      return meeting;
   }

   public List<Termin> getMeetingTermins(Long meetingId) {
      Meeting meeting = meetingRepository.findById(meetingId)
         .orElseThrow(() -> new NotFoundException("Meeting does not exist"));
      if (!meeting.getDoctor().getId().equals(getCurrentDoctor().getId()))
         throw new UnauthorizedException("Unauthorized to access the resource");
      return meeting.getTermins();
   }

   public List<Review> getMyReviews() {
      return getCurrentDoctor().getReviews();
   }

   public Meeting acceptMeetingRequest(AcceptMeetingRequestDto acceptMeetingRequestDto) {
      MeetingRequest meetingRequest = meetingRequestRepository.getOne(acceptMeetingRequestDto.getMeetingRequestId());
      Meeting meeting = Meeting.builder()
         .meetingRequest(meetingRequest)
         .doctor(getCurrentDoctor())
         .patient(meetingRequest.getPatient())
         .service(meetingRequest.getService())
         .isActive(true)
         .description("")
         .messages(new LinkedList<>())
         .termins(new LinkedList<>())
         .build();
      return meetingRepository.save(meeting);
   }

   public void closeMeeting(CloseMeetingDto closeMeetingDto) {
      Meeting meeting = meetingRepository.findById(closeMeetingDto.getMeetingId())
         .orElseThrow(() -> new BadRequestException("Meeting with given ID does not exist"));
      meeting.setIsActive(false);
      meetingRepository.save(meeting);
   }

}
