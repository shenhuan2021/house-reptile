package com.lifesmile.reptile.service;

import com.lifesmile.reptile.Constants;
import com.lifesmile.reptile.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Slf4j
@Component
public class CityProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        try {
            log.info("=============process(city)================");
            if (!page.getHtml().xpath("//div[@class='city_list']").match()) {
                page.setSkip(true);
            }
            List<Selectable> provinces = page.getHtml().xpath("//div[@class='city_province']").nodes();
            provinces.forEach(e -> {
                List<Selectable> citys = e.xpath("//ul[1]/li").nodes();
                citys.forEach(f -> {
                    String cityName = f.xpath("//a/text()").toString();
                    String cityUrl = f.xpath("//a/@href").toString();
                    Cache.cityCache.put(cityName, StringUtils.substringBetween(cityUrl, "//", "."));
                });
            });
        } catch (Exception eee) {
            log.error(eee.getMessage(), eee);
        }
    }

    @Override
    public Site getSite() {
        return Constants.site;
    }


}
