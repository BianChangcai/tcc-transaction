package org.mengyun.tcctransaction;

import org.mengyun.tcctransaction.api.*;

import java.io.Serializable;

/**
 * Created by changmingxie on 10/27/15.
 */
public class Participant implements Serializable {

    private static final long serialVersionUID = 4127729421281425247L;
    Class<? extends TransactionContextEditor> transactionContextEditorClass;
    private TransactionXid xid;
    private InvocationContext confirmInvocationContext;
    private InvocationContext cancelInvocationContext;
    private int status = ParticipantStatus.TRYING.getId();

    public Participant() {

    }

    public Participant(TransactionXid xid, InvocationContext confirmInvocationContext, InvocationContext cancelInvocationContext, Class<? extends TransactionContextEditor> transactionContextEditorClass) {
        this.xid = xid;
        this.confirmInvocationContext = confirmInvocationContext;
        this.cancelInvocationContext = cancelInvocationContext;
        this.transactionContextEditorClass = transactionContextEditorClass;
    }

    public Participant(InvocationContext confirmInvocationContext, InvocationContext cancelInvocationContext, Class<? extends TransactionContextEditor> transactionContextEditorClass) {
        this.confirmInvocationContext = confirmInvocationContext;
        this.cancelInvocationContext = cancelInvocationContext;
        this.transactionContextEditorClass = transactionContextEditorClass;
    }

    public void rollback(boolean force) {

        Terminator.invoke(new TransactionContext(xid, TransactionStatus.CANCELLING.getId(), force ? ParticipantStatus.TRY_SUCCESS.getId() : status), cancelInvocationContext, transactionContextEditorClass);
    }

    public void commit() {
        Terminator.invoke(new TransactionContext(xid, TransactionStatus.CONFIRMING.getId(), status), confirmInvocationContext, transactionContextEditorClass);
    }

    public TransactionXid getXid() {
        return xid;
    }

    public void setXid(TransactionXid xid) {
        this.xid = xid;
    }

    public InvocationContext getConfirmInvocationContext() {
        return confirmInvocationContext;
    }

    public InvocationContext getCancelInvocationContext() {
        return cancelInvocationContext;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status.getId();
    }

    public ParticipantStatus getStatus() {
        return ParticipantStatus.valueOf(this.status);
    }
}
