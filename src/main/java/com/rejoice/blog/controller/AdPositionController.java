package com.rejoice.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rejoice.blog.entity.AdPosition;
import com.rejoice.blog.service.AdPositionService;

@RestController
@RequestMapping("/ad-position")
public class AdPositionController extends BaseController<AdPosition, AdPositionService> {

}
