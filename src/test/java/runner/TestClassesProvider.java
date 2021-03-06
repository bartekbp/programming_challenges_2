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
                            numbertheory.repackaging.Main.class,
                            backtracking.littlebishops.Main.class,
                            backtracking.puzzleproblem.Main.class,
                            backtracking.queue.Main.class,
                            backtracking.servicingstations.Main.class,
                            backtracking.tugofwar.Main.class,
                            backtracking.gardenofeden.Main.class,
                            backtracking.colorhash.Main.class,
                            backtracking.biggersquareplease.Main.class,
                            graphs.bicoloring.Main.class,
                            graphs.playingwithwheels.Main.class,
                            graphs.thetouristguide.Main.class,
                            graphs.editstepladders.Main.class,
                            graphs.towerofcubes.Main.class,
                            graphs.fromdusktilldawn.Main.class,
                            graphs.hanoitowertroublesagain.Main.class,
                            graphs.freckles.Main.class,
                            graphs.necklace.Main.class,
                            graphs.firestation.Main.class,
                            graphs.war.Main.class,
                            graphs.railroads.Main.class,
                            graphs.touristguide.Main.class,
                            graphs.thegranddinner.Main.class,
                            graphs.theproblemwiththeproblemsetter.Main.class,
                            dynamicprogramming.isbiggersmarter.Main.class,
                            dynamicprogramming.distinctsubsequences.Main.class,
                            dynamicprogramming.weightsandmeasures.Main.class,
                            dynamicprogramming.unidirectionaltsp.Main.class,
                            dynamicprogramming.cuttingsticks.Main.class,
                            dynamicprogramming.ferryloading.Main.class,
                            dynamicprogramming.chopsticks.Main.class,
                            dynamicprogramming.adventuresinmoving.Main.class,
                            graphs.slashmaze.Main.class);
    }

    public Class<?> singleTestClass() {
        return graphs.slashmaze.Main.class;
    }
}
