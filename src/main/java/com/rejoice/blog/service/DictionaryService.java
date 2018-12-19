package com.rejoice.blog.service;

import org.springframework.stereotype.Service;

import com.rejoice.blog.common.constant.Constant;
import com.rejoice.blog.concurrent.VolitateVars;
import com.rejoice.blog.entity.Dictionary;

@Service
public class DictionaryService extends BaseService<Dictionary> {

	@Override
	public void updateByIdSelective(Dictionary t) {
		super.updateByIdSelective(t);
		if(Constant.DICT_CODE_REPLACE_STRING.equals(t.getCode())
				&& Constant.DICT_KEY_FILE_NAME.equals(t.getKey())
				) {
			VolitateVars.REPLACE_STRING_OF_FILE_NAME = t.getValue().split(",");
		}
	}

	public Dictionary queryOneByCodeAndKey(String code, String key) {
		Dictionary cons = new Dictionary();
		cons.setCode(code);
		cons.setKey(key);
		return this.queryOne(cons);
	}
}
