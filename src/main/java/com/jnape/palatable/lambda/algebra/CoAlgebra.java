package com.jnape.palatable.lambda.algebra;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functor.Functor;

public interface CoAlgebra<A, FA extends Functor<A>> extends Fn1<FA, A> {
}
