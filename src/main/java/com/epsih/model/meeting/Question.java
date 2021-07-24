package com.epsih.model.meeting;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.epsih.model.service.BusinessService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "question")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class   Question {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "pk_question")
   private Long id;

   @Column(length = 3000)
   @NotNull
   private String content;

   @Column
   @NotNull
   private Boolean isMultiSelect;

   @Column
   @NotNull
   private Boolean isMandatory;

   @OneToOne
   @JoinColumn(name = "fk_answer_options", referencedColumnName = "pk_answer_options", nullable = true)
   private AnswerOptions answerOptions;

   @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
   @JsonIgnore
   private List<Answer> answers;

   @ManyToMany(fetch = FetchType.LAZY, mappedBy = "questions", cascade = CascadeType.MERGE)
   @JsonIgnore
   private List<BusinessService> services;

}
