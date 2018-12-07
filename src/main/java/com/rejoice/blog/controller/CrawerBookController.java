package com.rejoice.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.entity.CrawerBook;
import com.rejoice.blog.service.CrawerBookService;

@RestController
@RequestMapping("/crawer-book")
public class CrawerBookController extends BaseController<CrawerBook, CrawerBookService>{

}
