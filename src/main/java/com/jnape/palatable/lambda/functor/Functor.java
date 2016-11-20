package com.jnape.palatable.lambda.functor;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.function.Function;

/**
 * An interface for the generic covariant functorial operation <code>map</code> over some parameter <code>A</code>.
 * Functors are foundational to many of the classes provided by this library; generally, anything that can be thought
 * of as "mappable" is an instance of at least this interface.
 * <p>
 * For a functor to be predictable (read: "well-behaved"), it must satisfy two laws:
 * <ul>
 * <li> identity: <code>functor.fmap(Function.identity()).equals(functor)</code> must be <code>true</code></li>
 * <li> composition: <code>functor.fmap(f.compose(g)).equals(functor.fmap(g).fmap(f))</code> must be
 * <code>true</code></li>
 * </ul>
 * Functors that do not guarantee both of these laws for all possible values cannot be confidently reasoned
 * about in the general case. Also note that because of the particularly low bar that Java's approach to equality sets,
 * all bets are off when a functor introduces side-effects, so this should almost always be avoided.
 * <p>
 * For more information, read about <a href="https://en.wikipedia.org/wiki/Functor" target="_top">Functors</a>.
 *
 * @param <A> The type of the parameter
 * @param <F> The unification parameter to more tightly type-constrain Functors to themselves
 * @see Bifunctor
 * @see Profunctor
 * @see Fn1
 * @see com.jnape.palatable.lambda.adt.hlist.Tuple2
 * @see com.jnape.palatable.lambda.adt.Either
 */
@FunctionalInterface
public interface Functor<A, F extends Functor> {

    /**
     * Covariantly transmute this functor's parameter using the given mapping function. Generally this method is
     * specialized to return an instance of the class implementing <code>Functor</code>.
     *
     * @param <B> the new parameter type
     * @param fn  the mapping function
     * @return a functor over B (the new parameter type)
     */
    <B> Functor<B, F> fmap(Function<? super A, ? extends B> fn);
}
