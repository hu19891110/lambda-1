package com.jnape.palatable.lambda.functor.applicative;

import com.jnape.palatable.lambda.functions.builtin.fn1.Constantly;
import com.jnape.palatable.lambda.functor.Functor;

import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Constantly.constantly;
import static java.util.function.Function.identity;

/**
 * An interface representing applicative functors - functors that can have their results combined with other
 * functors of the same instance in a context-free manner.
 * <p>
 * The same rules that apply to <code>Functor</code> apply to <code>Applicative</code>, along with the following
 * additional 4 laws:
 * <ul>
 * <li> identity: <code>applicative.sequence(pureId).equals(applicative)</code> must be <code>true</code></li>
 * <li> composition: for instances <code>u</code>, <code>v</code>, and <code>w</code> of the same
 * <code>Applicative</code> type,
 * <code>w.sequence(v.sequence(u.sequence((w|v|u).pure(x -&gt; x::compose))).equals((w.sequence(v)).sequence(u))</code>
 * must be <code>true</code></li>
 * <li> homomorphism: <code>applicative.pure(x).sequence(applicative.pure(f)).equals(applicative.pure(f.apply(x)))</code>
 * must be <code>true</code></li>
 * <li> interchange: <code>applicative.pure(y).sequence(u).equals(u.sequence(applicative.pure(f -&gt;
 * f.apply(y))))</code>
 * must be <code>true</code></li>
 * </ul>
 * As with <code>Functor</code>, <code>Applicative</code> instances that do not satisfy all of the functor laws, as
 * well as the above applicative laws, are not well-behaved and often break down in surprising ways.
 * <p>
 * For more information, read about
 * <a href="https://en.wikipedia.org/wiki/Applicative_functor" target="_top">Applicative Functors</a>.
 *
 * @param <A>   The type of the parameter
 * @param <App> The unification parameter to more tightly type-constrain Applicatives to themselves
 */
public interface Applicative<A, App extends Applicative> extends Functor<A, App> {

    /**
     * Lift a value into this <code>Applicative</code> type.
     *
     * @param b   the value
     * @param <B> the type of the parameter
     * @return an instance of this Applicative over b
     */
    <B> Applicative<B, App> pure(B b);

    /**
     * Apply a function wrapped in an instance of this <code>Applicative</code> to the value wrapped in this
     * <code>Applicative</code> instance, returning an <code>Applicative</code> over the result.
     *
     * @param <B>   the new parameter type
     * @param appFn the function to apply
     * @return an instance of this Applicative over the result of applying appFn to this Applicative's value
     */
    <B> Applicative<B, App> sequence(Applicative<? extends Function<? super A, ? extends B>, App> appFn);

    /**
     * Sequence both this <code>Applicative</code> and <code>appB</code>, discarding this <code>Applicative's</code>
     * result and returning <code>appB</code>. This is generally useful for sequentially performing side-effects.
     *
     * @param appB the other Applicative
     * @param <B>  the type of the returned Applicative's parameter
     * @return appB
     */
    default <B> Applicative<B, App> discardL(Applicative<B, App> appB) {
        return appB.sequence(sequence(pure(constantly(identity()))));
    }

    /**
     * Sequence both this <code>Applicative</code> and <code>appB</code>, discarding <code>appB's</code> result and
     * returning this <code>Applicative</code>. This is generally useful for sequentially performing side-effects.
     *
     * @param appB the other Applicative
     * @param <B>  the type of appB's parameter
     * @return this Applicative
     */
    default <B> Applicative<A, App> discardR(Applicative<B, App> appB) {
        return appB.sequence(sequence(pure(Constantly.<A, B>constantly().fmap(f -> f))));
    }

    @Override
    default <B> Applicative<B, App> fmap(Function<? super A, ? extends B> fn) {
        return sequence(pure(fn));
    }
}
