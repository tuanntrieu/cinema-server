package com.doan.cinemaserver.util;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.seat.UpdateSeatStatusRequestDto;
import com.doan.cinemaserver.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisKeyExpirationListenerUtil implements MessageListener {
    private final SeatService seatService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("schedule")) {
            Map<String, Long> ids = parseScheduleSeatKey(expiredKey);

            Long scheduleId = ids.get("scheduleId");
            Long seatId = ids.get("seatId");

            UpdateSeatStatusRequestDto updateSeatStatusRequestDto = UpdateSeatStatusRequestDto.builder()
                    .scheduleId(scheduleId)
                    .seatId(seatId)
                    .build();
            CommonResponseDto commonResponseDto = seatService.unHoldSeat(updateSeatStatusRequestDto);
            messagingTemplate.convertAndSend("/topic/seat-expired/" + scheduleId, seatId);
        }
    }

    public Map<String, Long> parseScheduleSeatKey(String key) {
        String[] parts = key.split("[:-]");
        Map<String, Long> result = new HashMap<>();
        result.put("scheduleId", Long.parseLong(parts[1]));
        result.put("seatId", Long.parseLong(parts[3]));
        return result;
    }
}
