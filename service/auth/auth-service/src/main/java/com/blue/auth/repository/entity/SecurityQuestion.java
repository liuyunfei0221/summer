package com.blue.auth.repository.entity;

import java.io.Serializable;

/**
 * security question
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class SecurityQuestion implements Serializable {

    private static final long serialVersionUID = 3498615791644659235L;

    private Long id;

    /**
     * member id
     */
    private Long memberId;

    /**
     * question
     */
    private String question;

    /**
     * answer
     */
    private String answer;

    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SecurityQuestion{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", createTime=" + createTime +
                '}';
    }

}