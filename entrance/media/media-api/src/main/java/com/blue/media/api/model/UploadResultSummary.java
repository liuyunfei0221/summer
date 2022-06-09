package com.blue.media.api.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * attachment info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class UploadResultSummary implements Serializable {

    private static final long serialVersionUID = 8565872235189137023L;

    private List<AttachmentUploadInfo> success = new LinkedList<>();

    private List<FileUploadResult> fail = new LinkedList<>();

    public UploadResultSummary() {
    }

    public UploadResultSummary(List<AttachmentUploadInfo> success, List<FileUploadResult> fail) {
        this.success = success;
        this.fail = fail;
    }

    public List<AttachmentUploadInfo> getSuccess() {
        return success;
    }

    public void setSuccess(List<AttachmentUploadInfo> success) {
        this.success = success;
    }

    public List<FileUploadResult> getFail() {
        return fail;
    }

    public void setFail(List<FileUploadResult> fail) {
        this.fail = fail;
    }

    @Override
    public String toString() {
        return "UploadResultInfo{" +
                "success=" + success +
                ", fail=" + fail +
                '}';
    }

}
