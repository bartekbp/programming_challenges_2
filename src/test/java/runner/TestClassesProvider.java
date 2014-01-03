package runner;

import java.util.Arrays;
import java.util.List;

public class TestClassesProvider {

    @SuppressWarnings("unchecked")
    public List<Class<? extends Runnable>> provideClasses() {
        return Arrays.asList(arithmeticandalgebra.archaeologistsdilemma.Main.class,
                             arithmeticandalgebra.multiplicationgame.Main.class,
                             arithmeticandalgebra.ones.Main.class,
                             arithmeticandalgebra.pairsumoniousnumbers.Main.class,
                             arithmeticandalgebra.polynomialcoefficients.Main.class,
                             arithmeticandalgebra.primaryarithmetic.Main.class,
                             arithmeticandalgebra.reverseandadd.Main.class,
                             arithmeticandalgebra.sternbrocotnumbersystem.Main.class,
                             combinatorics.counting.Main.class,
                             combinatorics.expressions.Main.class,
                             combinatorics.howmanyfibs.Main.class,
                             combinatorics.howmanypiecesofland.Main.class,
                             football.Main.class,
                             shellsort.Main.class);
    }
}
