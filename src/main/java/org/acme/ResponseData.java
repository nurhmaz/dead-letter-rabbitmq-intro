package org.acme;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"responseCode", "message", "data"})
public class ResponseData<T> {
    private int responseCode;
    private String message;
    private T data;

    public static <T> ResponseData<T> success(T data) {
        ResponseData<T> obj = new ResponseData<>();
        obj.responseCode = HttpResponseStatus.OK.code();
        obj.data = data;
        obj.message = "Success";
        return obj;
    }

    public static ResponseData failed(int responseCode, String message) {
        ResponseData obj = new ResponseData();
        obj.responseCode = responseCode;
        obj.message = message;
        return obj;
    }
}
