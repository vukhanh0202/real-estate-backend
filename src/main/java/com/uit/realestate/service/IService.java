package com.uit.realestate.service;

public interface IService<Input, Output> {
    Output execute(final Input input);
    Output execute();
}
