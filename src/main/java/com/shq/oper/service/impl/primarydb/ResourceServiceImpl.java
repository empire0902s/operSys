package com.shq.oper.service.impl.primarydb;

import com.github.pagehelper.Page;
import com.shq.oper.model.domain.primarydb.Resource;
import com.shq.oper.model.dto.SelectValueData;
import com.shq.oper.service.primarydb.ResourceService;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *@author ltg
 *@date 2018/4/18 11:41
 */
@Service("resourceService")
public class ResourceServiceImpl extends BaseServiceImpl<Resource, Long> implements ResourceService {

    @Override
    public  String getSortStr(Page<String> remarkList,long id){
        String asciiStr = SumStrAscii(id+"")+""; String temp="";
        List<String> newList=new ArrayList<>();
        newList.add("0");
        remarkList.forEach(
                p->{
                    if (p!=null){
                        newList.add(p);
                    }
                }
        );

        String remarkMax=newList.stream().max(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
//                return Integer.parseInt(o1)-Integer.parseInt(o2);
            }
        }).get();

        temp=remarkMax.contains(asciiStr+"")? remarkMax.replace(asciiStr,""):"0";

        return asciiStr+(Integer.parseInt(temp)+1);
    }


    /**
     * 字符串转ascii码
     *@author ltg
     *@date 2018/4/20 15:47
     * @params:[str]
     * @return: int
     */
    private  int SumStrAscii(String str){

        byte[] bytestr = str.getBytes();
        int sum = 0;
        for(int i=0;i<bytestr.length;i++){
            sum += bytestr[i];
        }
        return sum;
    }

}
