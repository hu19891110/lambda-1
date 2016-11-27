package com.jnape.palatable.lambda.functor.builtin;

import com.jnape.palatable.lambda.functor.Bifunctor;
import com.jnape.palatable.lambda.functor.applicative.Applicative;

import java.util.function.Function;

/**
 * A (surprisingly useful) applicative functor over some phantom type <code>B</code>, retaining a value of type
 * <code>A</code> that can be retrieved later. This is useful in situations where it is desirable to retain constant
 * information throughout arbitrary functor transformations, such that at the end of the chain, regardless of how
 * <code>B</code> has been altered, <code>A</code> is still pristine and retrievable.
 *
 * @param <A> the left parameter type, and the type of the stored value
 * @param <B> the right (phantom) parameter type
 */
public final class Const<A, B> implements Applicative<B, Const<A, ?>>, Bifunctor<A, B, Const<?, ?>> {

    private final A a;

    public Const(A a) {
        this.a = a;
    }

    /**
     * Retrieve the stored value.
     *
     * @return the value
     */
    public A runConst() {
        return a;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> fmap(Function<? super B, ? extends C> fn) {
        return (Const<A, C>) Applicative.super.fmap(fn);
    }

    /**
     * Return this <code>Const</code> unaltered, but type-cast to <code>Const&lt;A, C&gt;</code>.
     *
     * @param c   the ignored input value
     * @param <C> the new right parameter type
     * @return a Const over A (the same value) and C (the new phantom parameter)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> pure(C c) {
        return (Const<A, C>) this;
    }

    /**
     * Sequence the right parameter. Note that because <code>B</code> is never actually a known quantity outside of a
     * type signature, this is effectively a no-op that serves only to alter <code>Const's</code> type signature.
     *
     * @param <C>   the new right parameter type
     * @param appFn the mapping function
     * @return a Const over A (the same value) and C (the new phantom parameter)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> sequence(Applicative<? extends Function<? super B, ? extends C>, Const<A, ?>> appFn) {
        return (Const<A, C>) this;
    }

    @Override
    public <C> Const<A, C> discardL(Applicative<C, Const<A, ?>> appB) {
        return (Const<A, C>) Applicative.super.discardL(appB);
    }

    @Override
    public <C> Const<A, B> discardR(Applicative<C, Const<A, ?>> appB) {
        return (Const<A, B>) Applicative.super.discardR(appB);
    }

    /**
     * Covariantly map over the left parameter type (the value).
     *
     * @param fn  the mapping function
     * @param <Z> the new left parameter type (the value)
     * @return a Const over Z (the new value) and B (the same phantom parameter)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <Z> Const<Z, B> biMapL(Function<? super A, ? extends Z> fn) {
        return (Const<Z, B>) Bifunctor.super.biMapL(fn);
    }

    /**
     * Covariantly map over the right parameter (phantom) type.
     *
     * @param fn  the mapping function
     * @param <C> the new right parameter (phantom) type
     * @return a Const over A (the same value) and C (the new phantom parameter)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <C> Const<A, C> biMapR(Function<? super B, ? extends C> fn) {
        return (Const<A, C>) Bifunctor.super.biMapR(fn);
    }

    /**
     * Bifunctor's biMap, specialized for <code>Const</code>.
     *
     * @param lFn the left parameter mapping function
     * @param rFn the right parameter mapping function
     * @param <C> the new left parameter type
     * @param <D> the new right parameter type
     * @return a Const over C (the new value) and D (the new phantom parameter)
     */
    @Override
    public <C, D> Const<C, D> biMap(Function<? super A, ? extends C> lFn,
                                    Function<? super B, ? extends D> rFn) {
        return new Const<>(lFn.apply(a));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Const && this.a.equals(((Const) other).a);
    }

    @Override
    public int hashCode() {
        return a.hashCode();
    }

    @Override
    public String toString() {
        return "Const{" +
                "a=" + a +
                '}';
    }
}
