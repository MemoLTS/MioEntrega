package com.caso3.monitor.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.caso3.monitor.service.MonitorService;

@Component
public class MonitorScheduler {

    @Autowired
    private MonitorService monitorService;

    @Scheduled(fixedRate = 60000)
    public void ejecutarMonitoreo() {
        monitorService.revisarServicios();
    }

}