package com.justgifit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = "com.justgifit")
public class JustGifItProperties {

    private File gifLocation;

    private boolean optimize;

    private boolean createResultdir;

    public File getGifLocation() {
        return gifLocation;
    }

    public JustGifItProperties setGifLocation(File gifLocation) {
        this.gifLocation = gifLocation;
        return this;
    }

    public boolean isOptimize() {
        return optimize;
    }

    public JustGifItProperties setOptimize(boolean optimize) {
        this.optimize = optimize;
        return this;
    }

    public boolean isCreateResultdir() {
        return createResultdir;
    }

    public JustGifItProperties setCreateResultdir(boolean createResultdir) {
        this.createResultdir = createResultdir;
        return this;
    }
}
