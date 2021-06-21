package com.uit.realestate.constant;

public interface MessageCode {
    interface User {
        String USER_WRONG = "message.user.Wrong";
        String NOT_FOUND = "message.user.NotFound";
        String EXIST = "message.user.Exist";
    }
    interface Token {
        String INVALID_TOKEN = "message.token.InvalidToken";
        String NOT_PERMISSION = "message.token.NotPermission";
    }
    interface Category {
        String NOT_FOUND = "message.category.NotFound";
        String EXIST = "message.category.Exist";
    }
    interface Apartment {
        String NOT_FOUND = "message.apartment.NotFound";
        String NOT_PENDING = "message.apartment.NotPending";
    }

    interface Address {
        String INVALID = "message.address.Invalid";
    }
}
