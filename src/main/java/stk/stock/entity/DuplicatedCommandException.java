package stk.stock.entity;

import etk.exceptions.UncheckedException;

/**
 *
 * @author Pablo JS dos Santos
 */
public class DuplicatedCommandException extends UncheckedException {

    public DuplicatedCommandException(String message) {
        super("SSEDCE11", message);
    }
}
