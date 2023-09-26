package com.tcit.vms.vms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder

@Table(name="visitaccompany")
public class VisitAccompany implements Serializable {
    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "visitid", referencedColumnName = "id")
    private Visit visit;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "visitorid", referencedColumnName = "id")
    private Visitor visitor;
    @Column(name="isactive")
    private boolean isActive=true;
}
