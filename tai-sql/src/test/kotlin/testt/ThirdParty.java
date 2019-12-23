package testt;

import java.util.ArrayList;
import java.util.List;

public class ThirdParty {

    public static final String TAG = "ThirdParty";

    private List<Callback> callbacks = new ArrayList<>();

    public void addCallback(Callback callback) {
        d(TAG, "addCallback: " + callback);

        callbacks.add(callback);
    }

    private void d(String tag, String s) {
        System.out.println(tag + ": " + s);
    }

    public void removeCallback(Callback callback) {
        d(TAG, "removeCallback: " + callback);

        callbacks.remove(callback);
    }

    public void printState() {
        d("ThirdParty", "Callbacks count" + callbacks.size());
    }

    interface Callback {
        void onValueChanged(int value);
    }
}
