package com.lifesmile.reptile.runner;

import com.lifesmile.reptile.utils.IOUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class SpiderRunner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws IOException {
        IOUtil.readToCache();
    }
}
