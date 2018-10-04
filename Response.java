package httpwebserver.util;

import java.util.List;

public class Response {

    private String httpVersion;
    private String typeId;
    private String typeDescription;
    private List<String> head;
    private String datas;

    public Response(String httpVersion, String typeId, String typeDescription, List<String> head, String str) {
        this.httpVersion = httpVersion;
        this.typeId = typeId;
        this.typeDescription = typeDescription;
        this.head = head;
        this.datas = str;
    }

    @Override
    public String toString() {
        String ret;
        ret = this.httpVersion + " " + this.typeId + " " + this.typeDescription + "\r\n";
        ret = head.stream().map((s) -> s).reduce(ret, String::concat);
        ret += "\r\n";
        return ret;
    }

    public void setHead(List<String> head) {
        this.head = head;
    }

    public void setDatas(String str) {
        this.datas = str;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public String getTypeId() {
        return typeId;
    }

    public List<String> getHead() {
        return head;
    }

    public String getDatas() {
        return datas;
    }
}
