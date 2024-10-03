package com.rnpc.operatingunit.model;

import com.rnpc.operatingunit.enums.OperationInfoColumnName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

@Getter
@Setter
@Entity
public class OperationInfoColumn {
    @Id
    @Column(name = "oic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter(AccessLevel.NONE)
    @Column(name = "oic_name", unique = true, nullable = false, updatable = false)
    private OperationInfoColumnName name;
    @Column(name = "oic_column_name", unique = true, nullable = false)
    private String columnName;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;

        OperationInfoColumn operationInfoColumn = (OperationInfoColumn) obj;

        return Objects.equals(name, operationInfoColumn.getName())
                && StringUtils.equalsIgnoreCase(columnName, operationInfoColumn.getColumnName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                columnName != null ? columnName.toLowerCase() : null,
                name
        );
    }

}
