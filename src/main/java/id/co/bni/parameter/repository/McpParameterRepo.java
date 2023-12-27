package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.McpParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface McpParameterRepo extends JpaRepository<McpParameter, String> {
    @Query("select km from MCP_PARAMETER km where km.mcpId = ?1")
    McpParameter findByMcpId(String mcpId);
}
