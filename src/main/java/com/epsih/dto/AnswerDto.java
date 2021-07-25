package com.epsih.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

   @NotNull
   @Size(min = 0, max = 1500)
   private String content;

   @NotNull
   private Long questionId;

}
