package com.blue.article.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.ConstantProcessor.assertArticleType;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;

/**
 * article insert param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class ArticleInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 40217575295999511L;

    private String title;

    private Integer type;

    private String content;

    private List<LinkInsertParam> links;

    public ArticleInsertParam() {
    }

    public ArticleInsertParam(String title, Integer type, String content, List<LinkInsertParam> links) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.links = links;
    }

    @Override
    public void asserts() {
        if (isBlank(this.title))
            throw new BlueException(BAD_REQUEST);

        assertArticleType(this.type, false);
        
        if (isBlank(this.content))
            throw new BlueException(BAD_REQUEST);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<LinkInsertParam> getLinks() {
        return links;
    }

    public void setLinks(List<LinkInsertParam> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ArticleInsertInfo{" +
                "title='" + title + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", links=" + links +
                '}';
    }

}
