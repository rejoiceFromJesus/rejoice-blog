package com.rejoice.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.entity.Dictionary;
import com.rejoice.blog.service.DictionaryService;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController<Dictionary, DictionaryService> {

}
