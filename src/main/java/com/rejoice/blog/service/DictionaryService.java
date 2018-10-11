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
		//fresh batch_post_lock
		if(Constant.CODE_BATCH_POST_LOCK.equals(t.getCode())
				&& Constant.DICT_KEY_DEFAULT.equals(t.getKey())
				) {
			VolitateVars.POST_BATCH_LOCK = t.getValue();
		}
	}
}