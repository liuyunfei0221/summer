package com.blue.media.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * media deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "files")
public class FileDeploy {

    private String handlerType;

    private String attrName;

    private List<String> validTypes;

    private List<Character> invalidPres;

    private int nameLenThreshold;

    private long allFileSizeThreshold;

    private long singleFileSizeThreshold;

    private long currentSizeThreshold;

    private String descPath;

    public FileDeploy() {
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public List<String> getValidTypes() {
        return validTypes;
    }

    public void setValidTypes(List<String> validTypes) {
        this.validTypes = validTypes;
    }

    public List<Character> getInvalidPres() {
        return invalidPres;
    }

    public void setInvalidPres(List<Character> invalidPres) {
        this.invalidPres = invalidPres;
    }

    public int getNameLenThreshold() {
        return nameLenThreshold;
    }

    public void setNameLenThreshold(int nameLenThreshold) {
        this.nameLenThreshold = nameLenThreshold;
    }

    public long getAllFileSizeThreshold() {
        return allFileSizeThreshold;
    }

    public void setAllFileSizeThreshold(long allFileSizeThreshold) {
        this.allFileSizeThreshold = allFileSizeThreshold;
    }

    public long getSingleFileSizeThreshold() {
        return singleFileSizeThreshold;
    }

    public void setSingleFileSizeThreshold(long singleFileSizeThreshold) {
        this.singleFileSizeThreshold = singleFileSizeThreshold;
    }

    public long getCurrentSizeThreshold() {
        return currentSizeThreshold;
    }

    public void setCurrentSizeThreshold(long currentSizeThreshold) {
        this.currentSizeThreshold = currentSizeThreshold;
    }

    public String getDescPath() {
        return descPath;
    }

    public void setDescPath(String descPath) {
        this.descPath = descPath;
    }

    @Override
    public String toString() {
        return "FileDeploy{" +
                "handlerType='" + handlerType + '\'' +
                ", attrName='" + attrName + '\'' +
                ", validTypes=" + validTypes +
                ", invalidPres=" + invalidPres +
                ", nameLenThreshold=" + nameLenThreshold +
                ", allFileSizeThreshold=" + allFileSizeThreshold +
                ", singleFileSizeThreshold=" + singleFileSizeThreshold +
                ", currentSizeThreshold=" + currentSizeThreshold +
                ", descPath='" + descPath + '\'' +
                '}';
    }

}
