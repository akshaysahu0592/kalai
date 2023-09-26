package com.tcit.vms.vms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@Table(name="visitortype")
public class VisitorType implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="visitortype")
    private String name;
    @Column(name="createddate")
    private LocalDateTime createdDate;
    @Column(name="isactive")
    private boolean isActive=true;


}
