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
public class RegionProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        try {
            log.info("=============process(Region)================");
            // 部分二：定义如何抽取页面信息，并保存下来
            if (!page.getHtml().xpath("//div[@data-role='ershoufang']").match()) {
                page.setSkip(true);
            }
            List<Selectable> regions = page.getHtml().xpath("//div[@data-role='ershoufang']/div[1]/a").nodes();
            regions.forEach(e -> {
                String regionName = e.xpath("a/text()").toString();
                String regionUrl = e.xpath("a/@href").toString();
                Cache.regionCache.put(regionName, StringUtils.substringBetween(regionUrl, "/ershoufang/", "/"));
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
