package com.shq.oper.service.primarydb;

import com.shq.oper.model.domain.primarydb.MessageExcel;
import com.shq.oper.model.dto.Msg;
import org.springframework.web.multipart.MultipartFile;

public interface MessageExcelService extends BaseService<MessageExcel, Long> {

    Msg<String> uploadFile(MultipartFile file, String username, String filePath) throws Exception;

    Msg<String> sendMessage(String  batch,String admin);

//    Msg<String> sendMessageByMQ(String  batch,String admin);


}
