package com.epsih.model.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "answer_options")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnswerOptions {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "pk_answer_options")
   private Long id;

   @ElementCollection(targetClass = String.class)
   @CollectionTable(name = "options_values", joinColumns = @JoinColumn(name = "pk_answer_options"))
   @Column(name = "fk_options_values")
   private List<String> options;

   @Column(nullable = false)
   private Boolean hasOther;

}
