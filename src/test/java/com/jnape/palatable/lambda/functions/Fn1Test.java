package com.jnape.palatable.lambda.functions;

import com.jnape.palatable.lambda.functor.applicative.Applicative;
import com.jnape.palatable.traitor.annotations.TestTraits;
import com.jnape.palatable.traitor.runners.Traits;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import testsupport.traits.ApplicativeLaws;
import testsupport.traits.FunctorLaws;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(Traits.class)
public class Fn1Test {

    @TestTraits({FunctorLaws.class, ApplicativeLaws.class})
    public Fn1<Integer, ?> testSubject() {
        return new EqualityCapableFn1<>(i -> i, new Random()::nextInt);
    }

    @Test
    public void functorProperties() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;
        Fn1<Integer, String> toString = Object::toString;

        assertThat(add2.fmap(toString).apply(2), is(toString.apply(add2.apply(2))));
    }

    @Test
    public void profunctorProperties() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;

        assertEquals((Integer) 3, add2.<String>diMapL(Integer::parseInt).apply("1"));
        assertEquals("3", add2.diMapR(Object::toString).apply(1));
        assertEquals("3", add2.<String, String>diMap(Integer::parseInt, Object::toString).apply("1"));
    }

    @Test
    public void applicativeProperties() {
        Fn1<Integer, String> toString = Object::toString;
        Fn1<Integer, Function<String, Integer>> add = i -> s -> i + Integer.valueOf(s);

        assertEquals((Integer) 2, toString.sequence(add).apply(1));
    }

    @Test
    public void thenIsJustAnAliasForFmap() {
        Fn1<Integer, Integer> add2 = integer -> integer + 2;
        Fn1<Integer, String> toString = Object::toString;

        MatcherAssert.assertThat(add2.then(toString).apply(2), is(toString.apply(add2.apply(2))));
    }

    @Test
    public void adapt() {
        Function<String, Integer> parseInt = Integer::parseInt;
        assertEquals((Integer) 1, Fn1.adapt(parseInt).apply("1"));
    }

    private static final class EqualityCapableFn1<A, B> implements Fn1<A, B> {
        private final Fn1<A, B>   delegate;
        private final Supplier<A> asGenerator;

        public EqualityCapableFn1(Fn1<A, B> delegate, Supplier<A> asGenerator) {
            this.delegate = delegate;
            this.asGenerator = asGenerator;
        }

        @Override
        public B apply(A a) {
            return delegate.apply(a);
        }

        @Override
        public <C> Fn1<A, C> fmap(Function<? super B, ? extends C> f) {
            return new EqualityCapableFn1<>(delegate.fmap(f), asGenerator);
        }

        @Override
        public <C> Fn1<A, C> pure(C c) {
            return new EqualityCapableFn1<>(delegate.pure(c), asGenerator);
        }

        @Override
        public <C> Fn1<A, C> sequence(Applicative<? extends Function<? super B, ? extends C>, Fn1<A, ?>> appFn) {
            return new EqualityCapableFn1<>(delegate.sequence(appFn), asGenerator);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (obj instanceof Fn1) {
                A a = asGenerator.get();
                return this.apply(a).equals(((Fn1<A, B>) obj).apply(a));
            }
            return false;
        }
    }
}
