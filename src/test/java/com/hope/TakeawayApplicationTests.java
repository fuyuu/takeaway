package com.hope;

import com.hope.service.MerchantService;
import com.hope.util.RedisIdWorker;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class TakeawayApplicationTests {

	@Test
	void contextLoads() {
	}

	@Resource
	private MerchantService merchantService;
	@Resource
	private RedisIdWorker redisIdWorker;

	private ExecutorService es = Executors.newFixedThreadPool(500);
	@Test
	void saveShop2Cache() throws InterruptedException {
		merchantService.saveRedisData(52L,10L);
	}

	@Test
	void redisIdWorker() throws InterruptedException {
		Runnable runnable = () -> {
			for (int i = 0; i < 100; i++) {
				long id = redisIdWorker.nextId("order");
				System.out.println("id="+id);
			};
		};
		for (int i = 0; i < 500; i++) {
			es.submit(runnable);
		}
		es.shutdown();
		while (!es.isTerminated()) {
			Thread.sleep(100);
		}
	}
}
