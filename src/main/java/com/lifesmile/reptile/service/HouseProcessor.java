package com.lifesmile.reptile.service;

import com.lifesmile.reptile.Constants;
import com.lifesmile.reptile.cache.Cache;
import com.lifesmile.reptile.config.EmailConfig;
import com.lifesmile.reptile.config.SearchConfig;
import com.lifesmile.reptile.entity.House;
import com.lifesmile.reptile.utils.IOUtil;
import com.lifesmile.reptile.utils.StringUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;


@Slf4j
@Component
public class HouseProcessor implements PageProcessor {


    @Setter
    private String city;
    @Setter
    private String region;
    @Setter
    int count = 1;

    @Autowired
    private EmailConfig emailConfig;

    @Autowired
    private SearchConfig searchConfig;

    @Autowired
    private EmailService emailService;

    @Override
    public void process(Page page) {
        try {
            log.info("=============process(house)================");
            if (!page.getHtml().xpath("//ul[@class='sellListContent']").match()) {
                page.setSkip(true);
            } else {
                int total = Integer.valueOf(page.getHtml().xpath("//div[@class='resultDes clear']/h2/span/text()").toString().trim());
                int totalPage = total / 30 + 1;
                if ((count < totalPage) && (count <= 100)) {
                    count++;
                    List<Selectable> targets = page.getHtml().xpath("//li[@class='clear").nodes();
                    targets.forEach(e -> {
                        try {
                            House house = bulidHouse(e);
                            filterAndSendEmail(house);
                        } catch (Exception ex) {
                            log.error("Function process() >> targets.forEach() Exception,details:", ex);
                        }
                    });
                    // 部分三：从页面发现后续的url地址来抓取
                    int index = page.getUrl().toString().indexOf("pg");
                    String newPage = page.getUrl().toString().substring(0, index) + "pg" + count + "/";
                    page.addTargetRequest(newPage);
                } else {
                    page.setSkip(true);
                }
            }
        } catch (Exception eee) {
            log.error("Function process() Exception,details:", eee);
        }
    }

    private void filterAndSendEmail(House house) throws Exception {
        String[] keyword = searchConfig.getKeyword().split(",");
        for (int i = 0; i <keyword.length ; i++) {
            String key = keyword[i];
            if (house.getTitle().contains(key) || house.getStreet().contains(key)){
                if (Cache.recordCache.get(house.getId()) == null){
                    IOUtil.outFile(house.getId(), "record.txt");
                    String[] split = emailConfig.getToAddr().split(",");
                    for (int j = 0; j < split.length; j++) {
                        emailService.sendMail(split[j], house);
                    }
                    Cache.recordCache.put(house.getId(),"1");
                    return;
                }
            }
        }
    }


    @Override
    public Site getSite() {
        return Constants.site;
    }

    private House bulidHouse(Selectable e){
        House house = new House();
        String title = e.xpath("//div[@class='title']/a[1]/text()").toString();
        String url = e.xpath("//a[@class='VIEWDATA CLICKDATA maidian-detail']/@href").toString();
        String image = null;
        if (e.xpath("//img[@class='lj-lazy']/@data-original").match()) {
            image = e.xpath("//img[@class='lj-lazy']/@data-original").toString();
        }
        String s = e.xpath("//div[@class='houseInfo']/text()").toString();
        String community = e.xpath("//div[@class='houseInfo']/a/text()").toString();
        String floor = e.xpath("//div[@class='positionInfo']/text()").toString();
        String street = e.xpath("//div[@class='positionInfo']/a[1]/text()").toString();
        String totolPrice = e.xpath("//div[@class='totalPrice']/span[1]/text()").toString();
        String averagePrice = StringUtils.strip(StringUtils.strip(e.xpath("//div[@class='unitPrice']/span[1]/text()").toString(), "单价"), "元/平米");
        String followInfo = e.xpath("//div[@class='followInfo']/text()").toString();
        String[] sl = followInfo.split("/");
        String watch = StringUtil.collectStringNumber(sl[0]);
        //现在取消了带看次数
        //String view = StringUtil.collectStringNumber(sl[1]);
        String releaseDate = sl[1];
        String ss = StringUtils.strip(s, "|");
        //String[] houseInfo = StringUtils.split(ss, "|");
        //String roomCount = houseInfo[0];
        house.setInfo(ss);
        //Double houseArea = Double.valueOf(houseInfo[3].split("平米")[0]);
       // String towards = houseInfo[4];

        house.setId(StringUtil.collectStringNumber(url));
        house.setTitle(title);
        house.setUrl(url);
        house.setCommunity(community);
        house.setStreet(street);
        house.setRegion(region);
        house.setCity(city);

        house.setTotalPrice(Double.valueOf(totolPrice));
        house.setAveragePrice(Double.valueOf(averagePrice));
        house.setImage(image);
        house.setWatch(Integer.valueOf(watch));
        //house.setView(Integer.valueOf(view));
        house.setReleaseDate(releaseDate);
       // house.setRoomCount(houseInfo[2]);
        //house.setHouseArea(houseArea);
        //house.setTowards(towards);
       // house.setDecoration(decoration);
      //  house.setElevator(houseInfo[0]);
       // house.setCreateDate(houseInfo[1]);
        house.setFloor(floor);
        return house;
    }

}
