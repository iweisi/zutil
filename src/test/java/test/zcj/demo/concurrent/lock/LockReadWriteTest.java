package test.zcj.demo.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁-线程同步
 * 
 * @author ZCJ
 * @data 2012-10-25
 */
public class LockReadWriteTest {
	
	Integer data;
	ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	void processCachedData() {
		rwl.readLock().lock();
		if (data == null) {
			rwl.readLock().unlock();
			rwl.writeLock().lock();
			if (data == null) {
				data = 1;
			}
			rwl.readLock().lock();
			rwl.writeLock().unlock();
		}

		// use(data);
		rwl.readLock().unlock();
	}
}
