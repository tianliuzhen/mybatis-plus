package com.aaa.mybatisplus.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 描述
 *
 * @author 田留振(liuzhen.tian @ haoxiaec.com)
 * @version 1.0
 * @date 2020/1/14
 */
@Data
public class PageDto {

    @ApiModelProperty(name="current",value="当前页",required=true)
    private Integer current;
    private Integer size;

}
