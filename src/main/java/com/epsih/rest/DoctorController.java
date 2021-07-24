package com.epsih.rest;

import com.epsih.constants.Endpoints;
import com.epsih.dto.AcceptMeetingRequestDto;
import com.epsih.dto.CloseMeetingDto;
import com.epsih.model.user.Doctor;
import com.epsih.model.meeting.Meeting;
import com.epsih.model.meeting.Review;
import com.epsih.model.meeting.Termin;
import com.epsih.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(Endpoints.DOCTOR_ROOT)
public class DoctorController {

   private final DoctorService doctorService;

   @GetMapping(Endpoints.DOCTOR_ME)
   public Doctor getCurrentDoctor() {
      return doctorService.getCurrentDoctor();
   }

   @GetMapping(Endpoints.DOCTOR_MEETINGS)
   public List<Meeting> getMyMeetings() {
      return doctorService.getMyMeetings();
   }

   @GetMapping(Endpoints.DOCTOR_MEETING_ID)
   public Meeting getMyMeetingById(@PathVariable("id") Long id) {
      return doctorService.getMyMeeting(id);
   }

   @GetMapping(Endpoints.DOCTOR_MEETING_TERMINS)
   public List<Termin> getTerminsForMeeting(@PathVariable("id") Long meetingId) {
      return doctorService.getMeetingTermins(meetingId);
   }

   @GetMapping(Endpoints.DOCTOR_REVIEWS)
   public List<Review> getMyReviews() {
      return doctorService.getMyReviews();
   }

   @PostMapping(Endpoints.DOCTOR_ACCEPT_MEETING_REQUEST)
   public Meeting acceptMeetingRequest(@RequestBody @Valid AcceptMeetingRequestDto acceptMeetingRequestDto) {
      return doctorService.acceptMeetingRequest(acceptMeetingRequestDto);
   }

   @PostMapping(Endpoints.DOCTOR_CLOSE_MEETING)
   public void closeMeeting(@RequestBody @Valid CloseMeetingDto closeMeetingDto) {
      doctorService.closeMeeting(closeMeetingDto);
   }
}
