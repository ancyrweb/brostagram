package com.rewieer.brostagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RequestStatus<Result> {
    public enum Status { LOADING, SUCCESS, ERROR };

    @Nullable public String message;
    @Nullable public Result data;
    @NonNull public Status status;

    private RequestStatus(@NonNull Status status, @Nullable String message, @Nullable Result data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> RequestStatus<T> loading(@Nullable T data) {
        return new RequestStatus<>(Status.LOADING, null, data);
    }
    public static <T> RequestStatus<T> success(@NonNull T data) {
        return new RequestStatus<>(Status.SUCCESS, null, data);
    }
    public static <T> RequestStatus<T> error(@NonNull String message, @Nullable T data) {
        return new RequestStatus<>(Status.ERROR, message, data);
    }
}
