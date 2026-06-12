package com.moa.backend.welfare.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WelfareScheduler{
	private final WelfareBatchService welfareBatchService;
	
	@Scheduled(cron = "0 0 0 * * *")
	public void scheduleFecth() {
		welfareBatchService.fetchAndSaveAll(1);
	}
} 