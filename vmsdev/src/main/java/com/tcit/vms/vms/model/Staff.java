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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
@Builder
@Table(name="staff")
@JsonIgnoreProperties
public class Staff implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message="staffName is Mandatory" )
    @Size(min = 3, max = 40 )
    @Column(name="staffname")
    private String staffName;
    @Pattern(regexp = "^\\+971\\d{9}$", message = "Invalid UAE mobile number format. It should start with +971 followed by 9 digits.")
    @Column(name="mobileno")
    private String mobileNo;
    @Column (unique = true)
    @Email(message = "Email must follow the formatter: ***@***")
    private String email;
    @Column(name="profpicture")
    private String profPicture;
    private String password;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "departmentid", referencedColumnName = "id")
    private Department department;
    private String designation;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "roleid", referencedColumnName = "id")
    private Role role;
    @Column(name="createddate")
    private LocalDateTime createdDate;
    @Column(name="isactive")
    private boolean isActive=true;
}
