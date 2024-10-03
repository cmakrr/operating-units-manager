package com.rnpc.operatingunit.model;

import com.rnpc.operatingunit.enums.OperatingRoomStatus;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLInetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

import java.util.Objects;

@Getter
@Setter
@Entity
public class OperatingRoom {
    @Id
    @Column(name = "or_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "or_name", nullable = false, unique = true)
    private String name;
    @Type(PostgreSQLInetType.class)
    @Column(name = "or_ip", unique = true, columnDefinition = "inet")
    private Inet ip;
    @Column(name = "or_status")
    private OperatingRoomStatus status;
    @OneToOne
    @JoinColumn(name = "or_operation_id")
    private Operation currentOperation;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        OperatingRoom operatingRoom = (OperatingRoom) obj;

        return StringUtils.equalsIgnoreCase(name, operatingRoom.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name != null ? name.toLowerCase() : null);
    }

}
