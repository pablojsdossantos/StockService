package stk.core.api;

import cqk.api.AbstractRestApi;
import cqk.api.login.InternalServiceLoginController;
import cqk.api.login.LoginLocator;
import javax.inject.Inject;

/**
 *
 * @author Pablo JS dos Santos
 */
public class StkRestApi extends AbstractRestApi {

    @Inject
    InternalServiceLoginController loginController;

    @Override
    public LoginLocator getLoginLocator() {
        return this.loginController;
    }
}
