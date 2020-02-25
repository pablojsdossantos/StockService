package stk.stock.messaging;

import cqk.messaging.Message;
import cqk.messaging.OutgoingChannel;
import io.quarkus.vertx.ConsumeEvent;
import io.reactivex.Flowable;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import stk.stock.messaging.commands.ComputeHistoryVarCmd;

/**
 *
 * @author Pablo JS dos Santos
 */
@ApplicationScoped
public class StockOutputChannel {
    public static final String COMPUTE_HISTORY_VAR = "COMPUTE-HISTORY-VAR";
    private static final String HISTORY_VAR_COMPUTATION_CONFIG = "HISTORY-VAR-COMPUTATION";

    private OutgoingChannel<ComputeHistoryVarCmd> computHistoryVarChannel;

    public StockOutputChannel() {
        this.computHistoryVarChannel = new OutgoingChannel<>();
    }

    @ConsumeEvent(COMPUTE_HISTORY_VAR)
    public void requestHisoryVarComputation(ComputeHistoryVarCmd command) {
        Message<ComputeHistoryVarCmd> message = Message.from(command);
        this.computHistoryVarChannel.push(message);
    }

    @Outgoing(HISTORY_VAR_COMPUTATION_CONFIG)
    public Flowable<String> historyVarCmdOutput() {
        return this.computHistoryVarChannel.getOutput();
    }
}
