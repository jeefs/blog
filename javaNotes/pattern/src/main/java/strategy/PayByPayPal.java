package strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * paypal策略，实现支付接口
 */
public class PayByPayPal implements PayStrategy {
    private static final Map<String,String> DATA_BASE = new HashMap<>();
    private final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private String email;
    private String password;
    private boolean signedIn;

    static {
        DATA_BASE.put("admin","admin@qq.com");
        DATA_BASE.put("mike","mike@qq.com");
    }

    /**
     *收集客户数据
     */
    @Override
    public void collectPaymentDetails() {
        try {
           System.out.print("输入用户邮箱: ");
           email = READER.readLine();
           System.out.print("输入用户密码");
           password = READER.readLine();
//           if(verify()) {
//
//           }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean verify() {
        setSignedIn(email.equals(DATA_BASE.get(password)));
        return signedIn;
    }

    @Override
    public boolean pay(int paymentAmount) {
        if(signedIn) {
            System.out.println("Paying " + paymentAmount + " using PayPal.");
            return true;
        } else {
            return false;
        }
    }
    private void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }
}
