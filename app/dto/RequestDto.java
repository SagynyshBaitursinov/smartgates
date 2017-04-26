package dto;

import models.Request;

/**
 * Created by sagynysh on 4/26/17.
 */
public class RequestDto {
    public String fullname;
    public String reason;
    public String status;
    public RequestDto(Request request) {
        this.fullname = request.fullname;
        this.reason = request.reason;
        this.status = request.status.toString();
    }
}
