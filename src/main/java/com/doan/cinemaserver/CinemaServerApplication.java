package com.doan.cinemaserver;

import com.doan.cinemaserver.constant.RoleConstant;
import com.doan.cinemaserver.constant.RoomTypeEnum;
import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.domain.entity.Role;
import com.doan.cinemaserver.domain.entity.RoomType;
import com.doan.cinemaserver.domain.entity.SeatPrice;
import com.doan.cinemaserver.repository.MovieTypeRepository;
import com.doan.cinemaserver.repository.RoleRepository;
import com.doan.cinemaserver.repository.RoomTypeRepository;
import com.doan.cinemaserver.repository.SeatPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableCaching
@EnableAsync
@RequiredArgsConstructor
public class CinemaServerApplication {
    private final RoleRepository roleRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final SeatPriceRepository seatPriceRepository;

    public static void main(String[] args) {
        Environment env = SpringApplication.run(CinemaServerApplication.class, args).getEnvironment();
        String appName = env.getProperty("spring.application.name");
        if (appName != null) {
            appName = appName.toUpperCase();
        }
        String port = env.getProperty("server.port");
        log.info("-------------------------START {} Application------------------------------", appName);
        log.info("   Application         : {}", appName);
        log.info("   Url swagger-ui      : http://localhost:{}/swagger-ui.html", port);
        log.info("-------------------------START SUCCESS {} Application----------------------", appName);
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            if (!roleRepository.existsByCode(RoleConstant.ROLE_ADMIN)) {
                roleRepository.save(Role.builder().code(RoleConstant.ROLE_ADMIN).name(RoleConstant.ROLE_ADMIN.getRoleName()).build());
            }
            if (!roleRepository.existsByCode(RoleConstant.ROLE_USER)) {
                roleRepository.save(Role.builder().code(RoleConstant.ROLE_USER).name(RoleConstant.ROLE_USER.getRoleName()).build());
            }
            if (!seatPriceRepository.existsAllBySeatType(SeatType.STANDARD)) {
                seatPriceRepository.save(SeatPrice.builder()
                        .seatType(SeatType.STANDARD)
                        .weekdayPrice(40000L)
                        .weekendPrice(60000L)
                        .build());
            }
            if (!seatPriceRepository.existsAllBySeatType(SeatType.VIP)) {
                seatPriceRepository.save(SeatPrice.builder()
                        .seatType(SeatType.VIP)
                        .weekdayPrice(50000L)
                        .weekendPrice(75000L)
                        .build());
            }
            if (!seatPriceRepository.existsAllBySeatType(SeatType.COUPLE)) {
                seatPriceRepository.save(SeatPrice.builder()
                        .seatType(SeatType.COUPLE)
                        .weekdayPrice(100000L)
                        .weekendPrice(150000L)
                        .build());
            }
            if(!roomTypeRepository.existsByRoomType(RoomTypeEnum._2D)){
                roomTypeRepository.save(RoomType.builder()
                                .roomType(RoomTypeEnum._2D)
                                .surcharge(0L)
                        .build());
            }
            if(!roomTypeRepository.existsByRoomType(RoomTypeEnum._3D)){
                roomTypeRepository.save(RoomType.builder()
                        .roomType(RoomTypeEnum._3D)
                        .surcharge(0L)
                        .build());
            }
            if(!roomTypeRepository.existsByRoomType(RoomTypeEnum._4D)){
                roomTypeRepository.save(RoomType.builder()
                        .roomType(RoomTypeEnum._4D)
                        .surcharge(0L)
                        .build());
            }
            if(!roomTypeRepository.existsByRoomType(RoomTypeEnum._4D)){
                roomTypeRepository.save(RoomType.builder()
                        .roomType(RoomTypeEnum._4D)
                        .surcharge(0L)
                        .build());
            }

        };
    }


}
