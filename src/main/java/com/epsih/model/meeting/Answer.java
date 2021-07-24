package com.epsih.model.meeting;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "answer")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Answer {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "pk_question")
   private Long id;

   @Column(nullable = true, length = 3000)
   private String content;

   @ManyToOne(fetch = FetchType.EAGER)
   @NotNull
   @JoinColumn(name = "fk_question")
   @JsonIgnoreProperties("hibernateLazyInitializer")
   private Question question;

}
