package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.GatewayParameterChannel;
import id.co.bni.parameter.entity.GatewayParameterChannelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GatewayParameterChannelRepo extends JpaRepository<GatewayParameterChannel, GatewayParameterChannelId> {
    @Query("select km from GATEWAY_PARAMETER_CHANNEL km where km.transCode = ?1")
    GatewayParameterChannel findByTransCode(String transCode);

    @Query("select km from GATEWAY_PARAMETER_CHANNEL km where km.transCode = ?1 and km.systemIdOrMcpId = ?2 and km.paymentType = ?3")
    GatewayParameterChannel findByTransCodeAndSystemIdOrMcpIdAndPaymentType(String transCode, String systemIdOrMcpId, String paymentType);
}
