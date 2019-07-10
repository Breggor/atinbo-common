//
// Source status recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.atinbo.support.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Handler {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected Handler prev;
    protected Handler next;
    protected EnumUrlPatternType patternType;
    protected List<String> patternConsts;

    public Handler() {
        this.patternType = EnumUrlPatternType.CONTAIN;
        this.patternConsts = Collections.singletonList("/*");
    }

    public Handler(String... patternConsts) {
        this.patternType = EnumUrlPatternType.CONTAIN;
        this.patternConsts = Collections.singletonList("/*");
        this.patternConsts = Arrays.asList(patternConsts);
    }

    public Handler(EnumUrlPatternType patternType, String... patternConsts) {
        this.patternType = EnumUrlPatternType.CONTAIN;
        this.patternConsts = Collections.singletonList("/*");
        this.patternType = patternType;
        this.patternConsts = Arrays.asList(patternConsts);
    }

    public abstract void handle(String var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception;

    public Handler except(String... patternType) {
        this.patternType = EnumUrlPatternType.EXCEPT;
        this.patternConsts = Arrays.asList(patternType);
        return this;
    }

    public Handler contain(String... patternType) {
        this.patternType = EnumUrlPatternType.CONTAIN;
        this.patternConsts = Arrays.asList(patternType);
        return this;
    }
}
