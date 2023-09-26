package com.tcit.vms.vms.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@Table(name="mascampus")
public class Campus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="campusname")
    private String campusName;
    @Column(name="createddate")
    private LocalDateTime createdDate;
    @Column(name="isactive")
    private boolean isActive=true;
}
