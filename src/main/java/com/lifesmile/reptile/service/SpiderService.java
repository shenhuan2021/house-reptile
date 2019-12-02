package com.lifesmile.reptile.service;

import com.lifesmile.reptile.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.util.List;

@Service
public class SpiderService {

    private final String cityUrl = "https://www.ke.com/city/";
    private final String regionUrl = "https://%s.ke.com/ershoufang/";

    @Autowired
    private CityProcessor cityProcessor;

    @Autowired
    private RegionProcessor regionProcessor;

    @Autowired
    private StreetProcessor streetProcessor;

    @Autowired
    private HouseProcessor houseProcessor;


    /**
     * 爬取指定城市的二手房
     */
    @Async
    public void runCitySpider(String cityName, List<String> regions) {
        String city = Cache.cityCache.get(cityName);
        if (StringUtils.isEmpty(city)) {
            Spider.create(cityProcessor).addUrl(cityUrl).thread(1).run();
            city = Cache.cityCache.get(cityName);
        }
        if (city == null) {
            throw new NullPointerException("Not find this city");
        }

        String url = String.format(regionUrl, city);
        Spider.create(regionProcessor).addUrl(url).thread(1).run();
        if (Cache.regionCache.size() < 1) {
            throw new NullPointerException("not find this region!");
        }

        //根据区域名获取该区域下所有街道
        //String url = "https://" + city.getBriefName() + ".lianjia.com/ershoufang/";
        for (String reg : Cache.regionCache.keySet()) {
            if (regions.contains(reg)) {
                String regionUrl = url + Cache.regionCache.get(reg);
                Spider.create(streetProcessor).addUrl(regionUrl).thread(1).run();
            }
        }
        for (String name : Cache.streetCache.keySet()) {
            String street = Cache.streetCache.get(name);
            String streetUrl = url + street + "/pg1";
            houseProcessor.setCity(cityName);
            houseProcessor.setRegion(name);
            Spider.create(houseProcessor).addUrl(streetUrl).thread(1).run();
        }
    }


}
