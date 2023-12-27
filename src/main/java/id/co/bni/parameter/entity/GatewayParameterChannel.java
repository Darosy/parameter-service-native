package id.co.bni.parameter.entity;

import id.co.bni.parameter.dto.request.GatewayParameterRequest;
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
@Entity(name = "GATEWAY_PARAMETER_CHANNEL")
@IdClass(GatewayParameterChannelId.class)
public class GatewayParameterChannel {
    @Id
    @Column(name = "TRANS_CODE", nullable = false, length = 20)
    private String transCode;

    @Id
    @Column(name = "SYSTEM_ID_OR_MCP_ID", nullable = false, length = 50)
    private String systemIdOrMcpId;

    @Id
    @Column(name = "PAYMENT_TYPE", nullable = false, length = 50)
    private String paymentType;

    @Column(name = "URL", length = 200)
    private String url;

    @Column(nullable = false)
    private Boolean isUsingProxy;

    @Column(name = "PROXY_IP", length = 20)
    private String proxyIp;

    @Column(name = "PROXY_PORT", length = 10)
    private String proxyPort;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public GatewayParameterRequest toGatewayParameterResponse() {
        return GatewayParameterRequest.builder()
                .transCode(transCode)
                .systemIdOrMcpId(systemIdOrMcpId)
                .paymentType(paymentType)
                .url(url)
                .isUsingProxy(isUsingProxy)
                .proxyIp(proxyIp)
                .proxyPort(proxyPort)
                .build();
    }
}
