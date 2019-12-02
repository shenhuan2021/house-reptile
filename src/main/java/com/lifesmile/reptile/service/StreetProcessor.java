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
@Component
@Slf4j
public class StreetProcessor implements PageProcessor {


    @Override
    public void process(Page page) {
        try {
            log.info("=============process(street)================");
            // 部分二：定义如何抽取页面信息，并保存下来
            if (!page.getHtml().xpath("//div[@data-role='ershoufang']").match()) {
                page.setSkip(true);
            }
            List<Selectable> streets = page.getHtml().xpath("//div[@data-role='ershoufang']/div[2]/a").nodes();
            streets.forEach(e -> {
                String streetName = e.xpath("a/text()").toString();
                String streetUrl = e.xpath("a/@href").toString();
                Cache.streetCache.put(streetName, StringUtils.substringBetween(streetUrl, "/ershoufang/", "/"));
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
