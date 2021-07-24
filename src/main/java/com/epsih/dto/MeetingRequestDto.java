package com.epsih.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRequestDto {

   @NotNull
   private Long serviceID;
   @NotEmpty
   private List<Long> doctors;
   @NotEmpty
   private List<AnswerDto> answers;

}
