/*
 * Copyright © 2017 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mercateo.rest.jersey.utils.listing;

import com.mercateo.common.rest.schemagen.IgnoreInRestSchema;

import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class SearchQueryParameterBean {

    @Getter
    @Setter
    @IgnoreInRestSchema
    private int defaultLimit = 20;

    @IgnoreInRestSchema
    private int defaultOffset = 0;

    @QueryParam("offset")
    @DefaultValue("0")
    @Min(0)
    private Integer offset;

    @QueryParam("limit")
    @Min(0)
    private Integer limit;

    public SearchQueryParameterBean(int offset, int limit) {
        super();
        checkOffset(offset);
        this.offset = Integer.valueOf(offset);
        checkLimit(limit);
        this.limit = Integer.valueOf(limit);
    }

    public int getOffset() {
        if (offset == null) {
            return defaultOffset;
        }
        return offset.intValue();
    }

    public void setOffset(int offset) {
        checkOffset(offset);
        this.offset = Integer.valueOf(offset);
    }

    public int getLimit() {
        if (limit == null) {
            return defaultLimit;
        }
        return limit.intValue();
    }

    public void setLimit(int limit) {
        checkLimit(limit);
        this.limit = Integer.valueOf(limit);
    }

    private void checkLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit must be equal or greather than 0");
        }
    }

    private void checkOffset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset must be equal or greather than 0");
        }
    }

}
