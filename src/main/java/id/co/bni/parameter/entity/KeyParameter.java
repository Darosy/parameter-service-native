package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.request.KeyParameterRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "KEY_PARAMETER")
public class KeyParameter {
    @Id
    @Column(name = "KEY_", nullable = false, length = 100)
    private String key;
    @Column(name = "VALUE_", length = 200)
    private String value;
    @Column(name = "DESC_", length = 200)
    private String desc;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public KeyParameterRequest toKeyParameterResponse() {
        return KeyParameterRequest.builder()
                .key(key)
                .value(value)
                .desc(desc)
                .build();
    }
}
