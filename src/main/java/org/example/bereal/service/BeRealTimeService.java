package org.example.bereal.service;

import org.example.bereal.model.BeRealTime;
import org.example.bereal.repository.BeRealTimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

@Service
public class BeRealTimeService {

    private static final Logger log = LoggerFactory.getLogger(BeRealTimeService.class);
    private final BeRealTimeRepository beRealTimeRepository;
    private final Random random = new Random();

    public BeRealTimeService(BeRealTimeRepository beRealTimeRepository) {
        this.beRealTimeRepository = beRealTimeRepository;
    }

    /**
     * Каждый день в 00:01 генерирует случайное время BeReal на этот день
     */
    @Scheduled(cron = "0 1 0 * * *") // Каждый день в 00:01
    public void generateDailyBeRealTime() {
        LocalDate today = LocalDate.now();

        // Генерируем случайное время между 9:00 и 23:00
        int hour = 9 + random.nextInt(14); // 9-22
        int minute = random.nextInt(60);

        LocalDateTime beRealTime = LocalDateTime.of(today, LocalTime.of(hour, minute));

        BeRealTime entity = new BeRealTime(beRealTime);
        beRealTimeRepository.save(entity);

        log.info("Generated BeReal time for today: {}", beRealTime);
    }

    /**
     * Получает время BeReal на сегодня
     */
    public LocalDateTime getTodayBeRealTime() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return beRealTimeRepository
                .findTopByNotificationTimeBetweenOrderByNotificationTimeDesc(startOfDay, endOfDay)
                .map(BeRealTime::getNotificationTime)
                .orElseGet(() -> {
                    // Если не найдено, создаём на лету
                    log.warn("No BeReal time found for today, generating now");
                    generateDailyBeRealTime();
                    return getTodayBeRealTime();
                });
    }
}