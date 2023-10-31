package com.clyl.cloudlaw.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class SnowflakeIdGenerator {
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_MASK = (1L << SEQUENCE_BITS) - 1L;

    private final long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator() {
        long dataCenterId = getRandomValue(WORKER_ID_BITS);
        long machineId = getRandomValue(WORKER_ID_BITS);
        workerId = (dataCenterId << WORKER_ID_BITS) | machineId;
    }

    public synchronized String nextId() {
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Invalid System Clock");
        } else if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        long id = ((timestamp - 1288834974657L) << 22) | (workerId << 12) | sequence;
        return String.valueOf(id);
    }

    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        while (timestamp <= lastTimestamp) {
            timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        }
        return timestamp;
    }

    private static long getRandomValue(long bits) {
        return (long) (Math.random() * (1L << bits - 1L));
    }
}
