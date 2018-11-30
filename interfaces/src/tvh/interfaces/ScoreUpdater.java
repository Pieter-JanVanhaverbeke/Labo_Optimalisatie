package tvh.interfaces;

import tvh.dataClasses.Location;

import java.util.ArrayList;

public interface ScoreUpdater {
    void locations(ArrayList<Location> locations);
    void done();
    void updateScore(int score, long iteration);
}
