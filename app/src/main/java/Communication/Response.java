package Communication;

/**
 * Created by דביר בן שבת on 28/12/2017.
 */

public class Response<T> {
    T data;
    String ans;

    public T getData() {
        return data;
    }


    public String getAns() {
        return ans;
    }

    public Response(T data, String ans) {
        this.data = data;
        this.ans = ans;
    }

}

