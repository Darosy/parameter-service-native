package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.McpParameterFee;
import id.co.bni.parameter.entity.McpParameterFeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface McpParameterFeeRepo extends JpaRepository<McpParameterFee, McpParameterFeeId> {
    @Query("select km from MCP_PARAMETER_FEE km where km.mcpId = ?1")
    List<McpParameterFee> findByMcpId(String mcpId);
}
