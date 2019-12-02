package com.lifesmile.reptile.task;


import com.lifesmile.reptile.config.SearchConfig;
import com.lifesmile.reptile.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SendEmailTask {

    @Autowired
    private SearchConfig searchConfig;

    @Autowired
    private SpiderService spiderService;

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 12)
    public void sendToShawn(){
        String city = searchConfig.getCity();
        List<String> regions = Arrays.asList(searchConfig.getRegions().split(","));
        spiderService.runCitySpider(city,regions);
    }
}
