package com.tcit.vms.vms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@ToString
@Builder
@Table(name="visitor")
@JsonIgnoreProperties
public class Visitor implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Size(min = 3, max = 40 )
    @NotNull(message="name is Mandatory" )
    private String name;
    @Column (name="mobileno", unique = false)
    @Pattern(regexp = "^\\+971\\d{9}$", message = "Invalid UAE mobile number format. It should start with +971 followed by 9 digits.")
    private String mobileNo;
    @Email(message = "Email must follow the formatter: ***@***")
    private String email;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "visitortypeid", referencedColumnName = "id")
    private VisitorType visitorType;
    private String cryptograph;
    @Column(name="profpicture")
    private String profPicture;
    @Column(name="emiratesid")
    private String emiratesId;
    @Column(name="createddate")
    private LocalDateTime createdDate;
    @Column(name="isactive")
    private boolean isActive=true;


}
