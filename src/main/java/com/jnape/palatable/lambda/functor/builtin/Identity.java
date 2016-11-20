package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functor.applicative.Applicative;

import java.util.function.Function;

/**
 * An applicative functor over some value of type <code>A</code> that can be mapped over and retrieved later.
 *
 * @param <A> the value type
 */
public final class Identity<A> implements Applicative<A, Identity> {

    private final A a;

    public Identity(A a) {
        this.a = a;
    }

    /**
     * Retrieve the value.
     *
     * @return the value
     */
    public A runIdentity() {
        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> Identity<B> fmap(Function<? super A, ? extends B> fn) {
        return (Identity<B>) Applicative.super.fmap(fn);
    }

    @Override
    public <B> Identity<B> pure(B b) {
        return new Identity<>(b);
    }

    @Override
    public <B> Applicative<B, Identity> sequence(Applicative<Function<? super A, ? extends B>, Identity> appFn) {
        return new Identity<>(((Identity<Function<? super A, ? extends B>>) appFn).a.apply(a));
    }

    @Override
    public <B> Identity<B> discardL(Applicative<B, Identity> appB) {
        return (Identity<B>) Applicative.super.discardL(appB);
    }

    @Override
    public <B> Identity<A> discardR(Applicative<B, Identity> appB) {
        return (Identity<A>) Applicative.super.discardR(appB);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Identity && this.a.equals(((Identity) other).a);
    }

    @Override
    public int hashCode() {
        return a.hashCode();
    }

    @Override
    public String toString() {
        return "Identity{" +
                "a=" + a +
                '}';
    }
}
