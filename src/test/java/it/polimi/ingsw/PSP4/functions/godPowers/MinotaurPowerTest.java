package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.model.GameState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MinotaurPowerTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsed_standardMove() {}

    @Test
    public void powerUsed_punchEnemyWorker() {}

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
