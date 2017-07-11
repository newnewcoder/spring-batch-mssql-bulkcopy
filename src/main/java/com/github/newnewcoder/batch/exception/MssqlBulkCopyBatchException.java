package com.github.newnewcoder.batch.exception;

public class MssqlBulkCopyBatchException extends Exception {

    public MssqlBulkCopyBatchException() {
        super();
    }

    public MssqlBulkCopyBatchException(String message) {
        super(message);
    }

    public MssqlBulkCopyBatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MssqlBulkCopyBatchException(Throwable cause) {
        super(cause);
    }
}
