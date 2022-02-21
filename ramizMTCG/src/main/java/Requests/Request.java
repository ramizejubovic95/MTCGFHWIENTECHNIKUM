package Requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class Request {
    private String method;
    private String content;
    private String header;
    private String route;
    private int contentLength;

    public void print()
    {
        System.out.println("METHODE: " + this.getMethod());
        System.out.println("Route: " + this.getRoute());
        System.out.println("contentLength: " + this.getContentLength());
        System.out.println("CONTENT: " + this.getContent());
    }

}

