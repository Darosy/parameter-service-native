package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.McpParameterDetail;
import id.co.bni.parameter.entity.McpParameterDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface McpParameterDetailRepo extends JpaRepository<McpParameterDetail, McpParameterDetailId> {
    @Query("select km from MCP_PARAMETER_DETAIL km where km.mcpId = ?1")
    List<McpParameterDetail> findByMcpId(String mcpId);
}
