package com.shq.oper.model.dto;

import java.io.Serializable;

import org.apache.ibatis.session.RowBounds;


/**
 * 分页bean
 * @author tjun
 */
@lombok.NoArgsConstructor
@lombok.Data
public class PageDto extends RowBounds implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public PageDto(int page,int limit) {
		super();
		this.page = page;
		this.limit = limit;
				
	}
	
	/**
	 * 页数，当前页
	 */
	private int page = 1;
	private int limit = 10;

    /**
     * 总数
     */
    private long totalNums;
    /**
     * 总页数
     */
    private int pageAllNums;
    
    public int getOffset() {
        if(page >= 1){
            return (page-1) * limit;
        }
        return 0;
    }
}
