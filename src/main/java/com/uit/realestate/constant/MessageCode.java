package com.uit.realestate.constant;

public interface MessageCode {
    interface User {
        String USER_WRONG = "message.user.Wrong";
        String NOT_FOUND = "message.user.NotFound";
    }
    interface Token {
        String INVALID_TOKEN = "message.token.InvalidToken";
        String NOT_PERMISSION = "message.token.NotPermission";
    }
    interface Category {
        String NOT_FOUND = "message.category.NotFound";
    }
    interface Apartment {
        String NOT_FOUND = "message.apartment.NotFound";
        String NOT_PENDING = "message.apartment.NotPending";
    }
}
