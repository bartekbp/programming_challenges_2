package runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TestClassesProvider {

    @SuppressWarnings("unchecked")
    public List<Class<?>> allTestClasses() {
        return Arrays.asList((Class<?>) arithmeticandalgebra.archaeologistsdilemma.Main.class,
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
                             combinatorics.completetreelabeling.Main.class,
                             combinatorics.thepriestmathematician.Main.class,
                             combinatorics.selfdescribingsequence.Main.class,
                             combinatorics.steps.Main.class,
                             football.Main.class,
                             shellsort.Main.class);
    }

    public Class<?> singleTestClass() {
        return combinatorics.steps.Main.class;
    }
}
