package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.ChannelParameter;
import id.co.bni.parameter.entity.ChannelParameterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelParameterRepo extends JpaRepository<ChannelParameter, ChannelParameterId> {
    @Query("select km from CHANNEL_PARAMETER km where km.channelId = ?1 and km.systemId = ?2")
    ChannelParameter findByChannelIdAndSystemId(String channelId, String systemId);
}
