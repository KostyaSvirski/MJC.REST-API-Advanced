package com.epam.esm.validator;

public abstract class PreparedValidatorChain<T> {

    private PreparedValidatorChain<T> nextLink;

    public PreparedValidatorChain<T> linkWith(PreparedValidatorChain<T> nextLink) {
        this.nextLink = nextLink;
        return nextLink;
    }

    public abstract boolean validate(T bean);

    protected boolean checkNextLink(T bean) {
        if(nextLink == null) {
            return true;
        }
        return nextLink.validate(bean);
    }

}
