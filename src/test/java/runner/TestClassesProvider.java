package runner;

import java.util.Arrays;
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
                             sorting.football.Main.class,
                             sorting.shellsort.Main.class,
                             numbertheory.lightmorelight.Main.class,
                             numbertheory.carmichaelnumbers.Main.class,
                             numbertheory.euclidproblem.Main.class,
                             numbertheory.factovisors.Main.class,
                             numbertheory.summationoffourprimes.Main.class,
                             numbertheory.smithnumbers.Main.class,
                             numbertheory.marbles.Main.class,
                             numbertheory.repackaging.Main.class);
    }

    public Class<?> singleTestClass() {
        return numbertheory.repackaging.Main.class;
    }
}
