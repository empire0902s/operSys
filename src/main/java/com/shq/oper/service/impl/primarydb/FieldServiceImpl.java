package com.shq.oper.service.impl.primarydb;

import com.shq.oper.mapper.primarydb.DictMapper;
import com.shq.oper.model.domain.primarydb.Dict;
import com.shq.oper.model.domain.primarydb.MessageSend;
import com.shq.oper.model.domain.primarydb.ticket.Field;
import com.shq.oper.service.primarydb.DictService;
import com.shq.oper.service.primarydb.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("fieldService")
public class FieldServiceImpl  extends BaseServiceImpl<Field, Long> implements FieldService {


}
