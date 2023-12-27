package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.request.ChannelParameterRequest;
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
@Entity(name = "CHANNEL_PARAMETER")
@IdClass(ChannelParameterId.class)
public class ChannelParameter {
    @Id
    @Column(name = "CHANNEL_ID", nullable = false, length = 20)
    private String channelId;

    @Id
    @Column(name = "SYSTEM_ID", nullable = false, length = 30)
    private String systemId;

    @Column(name = "BRANCH", nullable = false, length = 10)
    private String branch;

    @Column(name = "TELLER", nullable = false, length = 10)
    private String teller;

    @Column(name = "OVERRIDE_FLAG", nullable = false, length = 2)
    private String overrideFlag;

    @Column(name = "TANDEM_FLAG", nullable = false, length = 2)
    private String tandemFlag;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public ChannelParameterRequest toChannelParameterResponse() {
        return ChannelParameterRequest.builder()
                .channelId(channelId)
                .systemId(systemId)
                .branch(branch)
                .teller(teller)
                .overrideFlag(overrideFlag)
                .tandemFlag(tandemFlag)
                .build();
    }
}
