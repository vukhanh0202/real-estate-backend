package com.uit.realestate.service;

import com.uit.realestate.utils.MessageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class AbstractBaseService<Input, Output> implements IService<Input, Output> {

    /**
     * Message Helper.
     */
    @Autowired
    protected MessageHelper messageHelper;

    @Override
    @Transactional
    public Output execute(Input input) {
        try {
            preExecute(input);
            return doing(input);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Output execute() {
        return this.execute(null);
    }

    public void preExecute(Input input) {}

    public abstract Output doing(Input input);
}
