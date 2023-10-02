package com.tcit.vms.vms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString
@Builder
@Table(name="visits")
@JsonIgnoreProperties
public class Visit implements Serializable {
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "visitorid")
    private Visitor visitor;
    @Column(name="dateofvisit")
    private LocalDateTime dateOfVisit;
    private String duration;
    @OneToOne()
    @JoinColumn(name = "campusid", referencedColumnName = "id")
    private Campus campus;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "departmentid", referencedColumnName = "id")
    private Department department;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staffid", referencedColumnName = "id")
    private Staff staff;
    private String agenda;
    @Column(name="accompanycount")
    private Integer accompanyCount;
    @Column(name="approvedbyhost")
    private Integer approvedByHost;
    @Column(name="approvedbysecurity")
    private Integer approvedBySecurity;
    @Column(name="approvedbyhosttime")
    private LocalDateTime approvedByHostTime;
    @Column(name="approvedbysecuritytime")
    private LocalDateTime approvedBySecurityTime;
    @Column(name="checkin")
    private LocalDateTime checkIn;
    @Column(name="checkout")
    private LocalDateTime checkOut;
    private String status;
    @Column(name="isbiometricverified")
    private Boolean isBioMetricVerified;
    @Column(name="createddate")
    private LocalDateTime createdDate;
    @Column(name="isactive")
    private boolean isActive=true;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reasonid")
    private Reason reason;
    private String comments;
    @Column(name="approvedbysecurityid")
    private String approvedBySecurityId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "visitaccompany",
            joinColumns = @JoinColumn(name = "Visitid"),
            inverseJoinColumns = @JoinColumn(name = "Visitorid")
    )
    private List<Visitor> accompanies;
}
