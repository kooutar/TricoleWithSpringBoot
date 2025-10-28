package com.kaoutar.testSpring.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FournisseurDTO {

    private Long id;
    @NotBlank(message = "The company name is mandatory")
    private String societe;
    @NotBlank(message = "The adresse name is mandatory")
    private String adresse;
    @NotBlank(message = "The contact name is mandatory")
    private String contact;
    @NotBlank(message = "The email  is mandatory")
    @Email(message = "the email value must be valid ")
    private String email;
    @NotBlank(message = "The phone number   is mandatory")
    @Pattern(regexp = "^[0-9]{10}$", message = "Le phone number must contain  10 numbers")
    private String telephone;
    @NotBlank(message = "The city   is mandatory")
    private String ville;
    @NotBlank(message = "The ice  is mandatory")
    @Size(min = 10, max = 15, message = "L'ICE must contain value between 10 and 15")
    private String ice;


}
