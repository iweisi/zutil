package com.zcj.util.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdWorker {

	private final long workerId;
	private final static long twepoch = 1361753741828L;
	private long sequence = 0L;
	private final static long workerIdBits = 4L;
	private final static long maxWorkerId = -1L ^ -1L << workerIdBits;
	private final static long sequenceBits = 10L;

	private final static long workerIdShift = sequenceBits;
	private final static long timestampLeftShift = sequenceBits + workerIdBits;
	private final static long sequenceMask = -1L ^ -1L << sequenceBits;

	private long lastTimestamp = -1L;

	private static final Logger logger = LoggerFactory.getLogger(IdWorker.class);

	public IdWorker(final long workerId) {
		super();
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
					maxWorkerId));
		}
		this.workerId = workerId;
	}

	public synchronized long nextId() {
		long timestamp = this.timeGen();
		if (this.lastTimestamp == timestamp) {
			this.sequence = (this.sequence + 1) & sequenceMask;
			if (this.sequence == 0) {
				timestamp = this.tilNextMillis(this.lastTimestamp);
			}
		} else {
			this.sequence = 0;
		}
		if (timestamp < this.lastTimestamp) {
			logger.warn("时间戳获取异常：上次使用的时间戳为" + lastTimestamp + "；本次获取的时间戳为" + timestamp + "；相差"
					+ (this.lastTimestamp - timestamp) + "毫秒（起因：可能是系统时间被调整过；后果：小概率会导致生成的ID重复）");
		}
		this.lastTimestamp = timestamp;
		long nextId = ((timestamp - twepoch << timestampLeftShift)) | (this.workerId << workerIdShift)
				| (this.sequence);
		return nextId;
	}

	private long tilNextMillis(final long lastTimestamp) {
		long timestamp = this.timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = this.timeGen();
		}
		return timestamp;
	}

	private long timeGen() {
		return System.currentTimeMillis();
	}

}
