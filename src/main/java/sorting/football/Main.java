package sorting.football;

import java.io.IOException;
import java.util.*;

public class Main implements Runnable {
    static String readLn(int maxLength) {

        byte line[] = new byte[maxLength];
        int length = 0;
        int input = -1;
        try {
            while (length < maxLength) {
                input = System.in.read();
                if ((input < 0) || (input == '\n')) {
                    break;
                }

                line[length++] += input;
            }

            if ((input < 0) && (length == 0)) {
                return null;
            }

            return new String(line, 0, length);
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String args[]) {
        Main myWork = new Main();
        myWork.run();
    }

    public void run() {
        new Football().run();
    }
}

class Football implements Runnable {
    public void run() {
        String firstLine = Main.readLn(80);
        final int N = Integer.valueOf(firstLine);
        for(int tournamentDescriptionNumber = 0; tournamentDescriptionNumber < N; tournamentDescriptionNumber++) {
            String tournamentName = Main.readLn(101);
            String secondTournamentLine = Main.readLn(10);
            final int T = Integer.valueOf(secondTournamentLine);
            List<String> teams = new ArrayList<String>(T);

            for(int i = 0; i < T; i++) {
                String teamName = Main.readLn(31);
                teams.add(teamName);
            }

            String lineAfterTeams = Main.readLn(5);
            final int G = Integer.valueOf(lineAfterTeams);
            List<String> gamesPlayed = new ArrayList<String>(G);
            for(int i = 0; i < G; i++) {
                String gamePlayed = Main.readLn(100);
                gamesPlayed.add(gamePlayed);
            }

            List<String> results = calculateResults(teams, gamesPlayed);
            System.out.println(tournamentName);
            for(String line : results) {
                System.out.println(line);
            }

            if(tournamentDescriptionNumber != N - 1) {
                System.out.println();
            }
        }
    }

    List<String> calculateResults(List<String> teams, List<String> gamesPlayed) {
        Map<String, Result> results = new HashMap<String, Result>();
        for(String name : teams) {
            results.put(name, new Result(name));
        }

        for(String line : gamesPlayed) {
            String teamsAndResult[] = line.split("#");
            String team1 = teamsAndResult[0];
            String team2 = teamsAndResult[2];
            String result[] = teamsAndResult[1].split("@");
            int goals1 = Integer.valueOf(result[0]);
            int goals2 = Integer.valueOf(result[1]);

            addResults(team1, goals1, goals2, team2, results);
        }

        List<Result> listResults = new ArrayList<Result>(results.values());
        Collections.sort(listResults, Collections.reverseOrder(Result.getComparator()));

        return format(listResults);
    }

    private List<String> format(List<Result> listResults) {
        List<String> formattedResults = new ArrayList<String>(listResults.size());

        for(int i = 0; i < listResults.size(); i++) {
            Result result = listResults.get(i);
            formattedResults.add(String.format("%d) %s", i + 1, result));
        }

        return formattedResults;
    }

    private void addResults(String team1, int goals1, int goals2, String team2, Map<String, Result> results) {
        Result team1Result = results.get(team1);
        Result team2Result = results.get(team2);
        team1Result.addGoalsScored(goals1);
        team1Result.addGoalsLost(goals2);

        team2Result.addGoalsScored(goals2);
        team2Result.addGoalsLost(goals1);

        if(goals1 > goals2) {
            team1Result.addWin();
            team2Result.addLoss();
        } else if(goals2 > goals1) {
            team2Result.addWin();
            team1Result.addLoss();
        } else {
            team2Result.addTie();
            team1Result.addTie();
        }
    }


}


class Result {
    private String teamName;
    private int points;
    private int played;
    private int wins;
    private int ties;
    private int losses;
    private int goalsScored;
    private int goalsLost;

    public Result(String name) {
        this.teamName = name;
    }

    public void addWin() {
        points += 3;
        wins += 1;
        played += 1;
    }

    public void addTie() {
        points += 1;
        ties += 1;
        played += 1;
    }

    public void addLoss() {
        losses += 1;
        played += 1;
    }

    public void addGoalsScored(int i) {
        this.goalsScored += i;
    }

    public void addGoalsLost(int i) {
        this.goalsLost += i;
    }

    private int goalDiff() {
        return goalsScored - goalsLost;
    }

    public static Comparator<Result> getComparator() {
        return new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                if(o1.points > o2.points) {
                    return 1;
                } else if(o2.points > o1.points) {
                    return -1;
                }

                if(o1.wins > o2.wins) {
                    return 1;
                } else if(o2.wins > o1.wins) {
                    return -1;
                }

                if(o1.goalDiff() > o2.goalDiff()) {
                    return 1;
                } else if(o2.goalDiff() > o1.goalDiff()) {
                    return -1;
                }

                if(o1.goalsScored > o2.goalsScored) {
                    return 1;
                } else if(o2.goalsScored > o1.goalsScored) {
                    return -1;
                }

                if(o1.played < o2.played) {
                    return 1;
                } else if(o2.played < o1.played) {
                    return -1;
                }

                return o2.teamName.compareToIgnoreCase(o1.teamName);

            }

            @Override
            public boolean equals(Object obj) {
                return this == obj;
            }
        };
    }

    @Override
    public String toString() {
        return String.format("%s %dp, %dg (%d-%d-%d), %dgd (%d-%d)",
                teamName,
                points,
                played,
                wins,
                ties,
                losses,
                goalDiff(),
                goalsScored,
                goalsLost);
    }
}