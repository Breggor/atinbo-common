/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atinbo.mybatis.support;

import com.atinbo.entity.BaseDomain;
import com.atinbo.mybatis.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatisplus自定义填充
 *
 * @author breggor
 */
@Slf4j
//@Component
public class MetaObjectHandlerAdapter implements MetaObjectHandler {

    /**
     * 新增时自动填充默认数据
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof BaseDomain) {
            setFieldValByName("createTime", new Date(), metaObject, FieldFill.DEFAULT);
            setFieldValByName("isDeleted", BaseEntity.UN_DELETED, metaObject, FieldFill.DEFAULT);
        }
    }

    /**
     * 修改时自动填充默认数据
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.getOriginalObject() instanceof BaseDomain) {
             setFieldValByName("updateTime", new Date(), metaObject, FieldFill.DEFAULT);
        }
    }

}
