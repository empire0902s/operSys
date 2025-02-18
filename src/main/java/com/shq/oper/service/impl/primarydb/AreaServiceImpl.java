package com.shq.oper.service.impl.primarydb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.shq.oper.mapper.primarydb.AreaMapper;
import com.shq.oper.model.domain.primarydb.Area;
import com.shq.oper.service.primarydb.AreaService;
@Service("areaService")
public class AreaServiceImpl extends BaseServiceImpl<Area, Long> implements AreaService {
	
	@Autowired
	private AreaMapper areaMapper;
	
	@Override
    public  String getSortStr(Page<Area> areass,String code){

       long size=areass.size()+1;
        int asciiStr = SumStrAscii(code);
        return size+""+asciiStr;
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


	@Override
	public List<Area> selectArea(Area areas) {
		return areaMapper.selectArea(areas);
	}
	
}
