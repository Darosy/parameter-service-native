package id.co.bni.parameter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "MCP_PARAMETER_FEE")
@IdClass(McpParameterFeeId.class)
public class McpParameterFee {
//    @Id
//    @GeneratedValue(generator = "system-uuid2")
//    @GenericGenerator(name = "system-uuid2", strategy = "uuid2")
//    private String id;
    @Id
    @Column(name = "MCP_ID", nullable = false, length = 50)
    private String mcpId;
    @Id
    @Column(name = "CURRENCY", nullable = false, length = 10)
    private String currency;
    @Column(name = "FEE", nullable = false, precision = 10, scale = 2)
    private BigDecimal fee;
}
