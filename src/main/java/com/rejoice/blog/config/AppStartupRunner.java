package com.rejoice.blog.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.concurrent.VolitateVars;
import com.rejoice.blog.entity.Dictionary;
import com.rejoice.blog.service.DictionaryService;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private static final Logger LOG =
      LoggerFactory.getLogger(AppStartupRunner.class);
 
    @Autowired
    DictionaryService dictionaryService;
    
 
    @Override
    public void run(ApplicationArguments args) throws Exception {
    	Dictionary cons = new Dictionary();
    	cons.setCode(Constant.CODE_BATCH_POST_LOCK);
    	cons.setKey(Constant.DICT_KEY_DEFAULT);
    	Dictionary dictionary = dictionaryService.queryOne(cons);
    	VolitateVars.POST_BATCH_LOCK = dictionary.getValue();
    	LOG.info("init VolitateVars.POST_BATCH_LOCK == {}", VolitateVars.POST_BATCH_LOCK);
    }
}